package serverfacade.requests;

public record JoinGameRequest(String authToken, String playerColor, int gameID) {
}
