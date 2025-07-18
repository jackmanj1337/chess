package serverfacade.results;

import model.GameData;

import java.util.Collection;

public record ListGamesResult(int httpCode, String message, Collection<GameData> games) {

}
