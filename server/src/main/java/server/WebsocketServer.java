package server;

import com.google.gson.Gson;
import handlers.PlayerSession;
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
    public void onClose(Session session, int statusCode, String reason) {
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
        gameSessions.getOrDefault(session.gameID(), Set.of()).remove(session);
        sessionToPlayer.remove(session.session());
        if (session.session() != null && session.session().isOpen()) {
            try {
                session.session().close(200, "player left the game");
            } catch (Exception e) {
                System.err.println("Failed to close session: " + e.getMessage());
            }
        }
    }


}
