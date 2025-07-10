package service;

import chess.ChessGame;
import dataaccess.localstorage.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.localstorage.GameDAO;
import model.GameData;
import service.requests.CreateGameRequest;
import service.results.CreateGameResults;
import service.results.ListGamesResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;


public class GameService {
    public void clearAllGames() throws DataAccessException {
        GameDAO gameAccess = new GameDAO();
        gameAccess.deleteAllGames();
    }

    public ListGamesResult listAllGames(String token) throws DataAccessException {
        AuthDAO authAccess = new AuthDAO();
        if (authAccess.getAuthFromToken(token) != null) {
            GameDAO gamesAccess = new GameDAO();
            return new ListGamesResult(200, "all good", gamesAccess.listAllGames());
        } else {
            return new ListGamesResult(401, "Error: unauthorized", new ArrayList<>());
        }


    }

    public CreateGameResults createNewGame(CreateGameRequest request) throws DataAccessException {
        AuthDAO authAccess = new AuthDAO();
        if (authAccess.getAuthFromToken(request.authToken()) != null) {
            GameDAO gamesAccess = new GameDAO();
            int id;
            do {
                id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
            } while (gamesAccess.getGame(id) != null);
            gamesAccess.addNewGame(new GameData(id, null, null, request.gameName(), new ChessGame()));
            return new CreateGameResults(200, "all good", id);
        } else {
            return new CreateGameResults(401, "Error: unauthorized", -1);
        }
    }

}
