package serverfacade.results;

public record CreateGameResult(int httpCode, String message, Integer gameID) {
}
