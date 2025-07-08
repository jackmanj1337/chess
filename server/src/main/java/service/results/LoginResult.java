package service.results;

public record LoginResult(int httpCode, String message, String username, String authToken) {
}
