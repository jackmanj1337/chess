package serverfacade.results;

public record RegisterResult(int httpCode, String message, String username, String authToken) {
}
