package serverfacade;

import com.google.gson.Gson;
import postlogin.gameplay.NotificationManager;
import utilities.ServerConnectionSettings;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;


@ClientEndpoint
public class WebsocketFacade {

    private String authToken;
    private int gameID;
    private Session session;
    private String username;
    public static final Gson GSON = new Gson();
    private static NotificationManager notify;

    public WebsocketFacade(String authToken, int gameID, String username) {
        this.gameID = gameID;
        this.authToken = authToken;
        this.username = username;
        establishWebsocketConnection();
        notify = new NotificationManager(username);
    }

    private void establishWebsocketConnection() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            URI uri = new URI("ws://" + ServerConnectionSettings.SERVER_LOCATION + "/ws");
            container.connectToServer(this, uri);
        } catch (DeploymentException | IOException | java.net.URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server");

        UserGameCommand connect = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);

        try {
            session.getBasicRemote().sendText(GSON.toJson(connect));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String msg) {
        //System.out.println("Received message from server: " + msg);
        ServerMessage message = GSON.fromJson(msg, ServerMessage.class);
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> notify.handleNotification(message.getMessage());
            case ERROR -> notify.handleError(message.getErrorMessage());
            case LOAD_GAME -> notify.handleLoadGame(message.getGame());
            default -> System.out.println("Unhandled message type: " + message.getServerMessageType());
        }
    }


    public void sendMessage(UserGameCommand command) {
        String message = GSON.toJson(command);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

