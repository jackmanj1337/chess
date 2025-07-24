package server;

import com.google.gson.Gson;
import dataaccess.DAOManager;
import dataaccess.DataAccessException;
import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.GameDAI;
import handlers.PlayerSession;
import model.AuthData;
import model.GameData;
import service.WebSocketService;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebsocketServer {
    private static final Gson GSON = new Gson();

    private static final Map<Integer, Set<PlayerSession>> gameSessions = new ConcurrentHashMap<>();
    private static final Map<Session, PlayerSession> sessionToPlayer = new ConcurrentHashMap<>();


    @OnWebSocketConnect
    public void onConnect(Session session) {
        session.setIdleTimeout(600000); // 600000 ms = 10 minutes
        System.out.println("WebSocket Connected: " + session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) throws DataAccessException {
        System.out.println("WebSocket Closed: " + session + ", Reason: " + reason);

        PlayerSession playerSession = sessionToPlayer.get(session);
        if (playerSession != null) {
            WebSocketService.handleLeave(playerSession);  // new method below
            removeUserFromGame(playerSession);
        }
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Received message: " + message);
        UserGameCommand request = GSON.fromJson(message, UserGameCommand.class);

        PlayerSession playerSession = new PlayerSession(session, request.getAuthToken(), request.getGameID());

        switch (request.getCommandType()) {
            case CONNECT -> WebSocketService.handleConnect(playerSession);
            case MAKE_MOVE -> WebSocketService.handleMakeMove(request, playerSession);
            case RESIGN -> WebSocketService.handleResign(playerSession);
            case LEAVE -> WebSocketService.handleLeave(playerSession);
        }
    }

    public static void sendToPlayer(PlayerSession playerSession, ServerMessage response) {
        try {
            playerSession.session().getRemote().sendString(GSON.toJson(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void broadcastToGame(int gameID, ServerMessage message, PlayerSession origin) {
        String json = GSON.toJson(message);
        Set<PlayerSession> sessions = gameSessions.getOrDefault(gameID, Set.of());

        String senderToken = null;
        if (origin != null) {
            senderToken = origin.authToken();
        }

        for (PlayerSession s : sessions) {
            if (s.session().isOpen() && !Objects.equals(s.authToken(), senderToken)) {
                try {
                    s.session().getRemote().sendString(json);
                } catch (Exception e) {
                    System.err.println("Error sending message: " + e.getMessage());
                }
            }
        }
    }

    public static void connectUserToGame(PlayerSession session) {
        gameSessions
                .computeIfAbsent(session.gameID(), k -> new HashSet<>())
                .add(session);
        sessionToPlayer.put(session.session(), session);
    }


    public static void removeUserFromGame(PlayerSession session) {
        try {
            int gameID = session.gameID();
            Session socket = session.session();
            String authToken = session.authToken();

            // Remove from in-memory session maps
            Set<PlayerSession> sessions = gameSessions.get(gameID);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    gameSessions.remove(gameID); // optional cleanup
                }
            }
            sessionToPlayer.remove(socket);

            // Fetch user identity
            AuthDAI auths = DAOManager.auths;
            AuthData authData = auths.getAuthFromToken(authToken);
            String username = (authData != null) ? authData.username() : null;

            if (username != null) {
                // Fetch and update game if this user was a player
                GameDAI games = DAOManager.games;
                GameData game = games.getGame(gameID);

                if (game != null) {
                    String white = game.whiteUsername();
                    String black = game.blackUsername();
                    boolean changed = false;

                    if (username.equals(white)) {
                        white = null;
                        changed = true;
                    }
                    if (username.equals(black)) {
                        black = null;
                        changed = true;
                    }

                    if (changed) {
                        GameData updated = new GameData(gameID, white, black, game.gameName(), game.game());
                        games.updateGameData(updated);
                    }
                }
            }

            // Close session if open
            if (socket != null && socket.isOpen()) {
                socket.close(1000, "User disconnected or left the game");
            }

            // Optional: Notify remaining users
            if (username != null) {
                ServerMessage message = ServerMessage.newNotification(username + " has left the game");
                broadcastToGame(gameID, message, session);
            }

        } catch (Exception e) {
            System.err.println("Error removing user from game: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
