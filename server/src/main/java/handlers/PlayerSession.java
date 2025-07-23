package handlers;

import org.eclipse.jetty.websocket.api.Session;


public record PlayerSession(Session session, String authToken, int gameID) {
}
