package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    private String message = null;
    private String errorMessage = null;
    private ChessGame game = null;


    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public static ServerMessage newNotification(String message) {
        ServerMessage notification = new ServerMessage(ServerMessageType.NOTIFICATION);
        notification.message = message;
        return notification;
    }


    public String getMessage() {
        return message;
    }

    public static ServerMessage newErrorMessage(String message) {
        ServerMessage error = new ServerMessage(ServerMessageType.ERROR);
        error.errorMessage = message;
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static ServerMessage newLoadGame(ChessGame game) {
        ServerMessage load = new ServerMessage(ServerMessageType.LOAD_GAME);
        load.game = game;
        return load;
    }

    public ChessGame getGame() {
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
