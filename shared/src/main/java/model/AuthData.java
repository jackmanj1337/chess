package model;

import java.util.UUID;

public record AuthData(String authToken, String username) {
    AuthData generateNewAuth(String username) {
        return new AuthData(UUID.randomUUID().toString(), username);
    }
}
