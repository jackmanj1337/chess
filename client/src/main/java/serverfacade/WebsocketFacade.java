package serverfacade;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import postlogin.gameplay.NotificationManager;
import utilities.ServerConnectionSettings;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.net.URI;

@WebSocket
public class WebsocketFacade {

    private String authToken;
    private int gameID;
    private Session session;
    public static final Gson GSON = new Gson();

    public WebsocketFacade(String authToken, int gameID) {
        this.gameID = gameID;
        this.authToken = authToken;
        establishWebsocketConnection();
    }

    public void establishWebsocketConnection() {

        WebSocketClient client = new WebSocketClient();
        WebsocketFacade socket = this;

        try {
            client.start();
            URI uri = new URI("ws://" + ServerConnectionSettings.SERVER_LOCATION + "/ws");
            client.connect(socket, uri).get();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        System.out.println("Connected to server");

        UserGameCommand connect = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);

        try {
            session.getRemote().sendString(GSON.toJson(connect));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        System.out.println("Received message from server: " + msg);
        ServerMessage message = GSON.fromJson(msg, ServerMessage.class);
        switch (message.getServerMessageType()) {
            case NOTIFICATION:
                NotificationManager.handleNotification(message.getMessage());
                break;
            case ERROR:
                NotificationManager.handleError(message.getErrorMessage());
                break;
            case LOAD_GAME:
                NotificationManager.handleLoadGame(message.getGame());
                break;
            default:
                System.out.println("Unhandled message type: " + message.getServerMessageType());
                break;
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Connection closed: " + reason);
    }

    @OnWebSocketError
    public void onError(Throwable cause) {
        cause.printStackTrace();
    }

    public void sendMessage(UserGameCommand command) {
        String message = GSON.toJson(command);
        if (session != null && session.isOpen()) {
            try {
                session.getRemote().sendString(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
