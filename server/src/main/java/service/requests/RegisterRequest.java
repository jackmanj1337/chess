package service.requests;

public record RegisterRequest(String username, String plainPassword, String email) {
}
