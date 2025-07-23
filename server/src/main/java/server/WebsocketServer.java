package server;

import com.google.gson.Gson;
import handlers.PlayerSession;
import service.WebSocketService;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebsocketServer {
    private static final Gson GSON = new Gson();

    private static final Map<Integer, Set<PlayerSession>> gameSessions = new ConcurrentHashMap<>();


    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket Connected: " + session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("WebSocket Closed: " + session + ", Reason: " + reason);
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

        ServerMessage response = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);


        sendToPlayer(playerSession, response);
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

        for (PlayerSession s : sessions) {
            if (s.session().isOpen() && !Objects.equals(s.authToken(), origin.authToken())) {
                try {
                    s.session().getRemote().sendString(json);
                } catch (Exception e) {
                    System.err.println("Error sending message: " + e.getMessage());
                }
            }
        }
    }

    public static void connectUserToGame(PlayerSession session) {
        gameSessions.get(session.gameID()).add(session);
    }

    public static void removeUserFromGame(PlayerSession session) {
        gameSessions.get(session.gameID()).remove(session);
    }


}
