package serverfacade.requests;

public record CreateGameRequest(String authToken, String gameName) {
}
