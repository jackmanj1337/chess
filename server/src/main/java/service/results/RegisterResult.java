package service.results;

public record RegisterResult(String httpCode, String message, String username, String authToken) {
}
