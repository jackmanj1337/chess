package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

@WebSocket
public class WebsocketServer {
    private static final Gson GSON = new Gson();

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


        ServerMessage response = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);


        try {
            session.getRemote().sendString(GSON.toJson(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
