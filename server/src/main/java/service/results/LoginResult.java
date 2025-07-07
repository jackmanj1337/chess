package service.results;

public record LoginResult(String httpCode, String message, String username, String authToken) {
}
