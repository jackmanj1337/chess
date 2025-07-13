package service;

import chess.ChessGame;
import dataaccess.DAOManager;
import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.GameDAI;
import dataaccess.DataAccessException;
import model.GameData;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;
import service.results.JoinGameResult;
import service.results.ListGamesResult;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


public class GameService {
    public void clearAllGames() throws DataAccessException {
        GameDAI gameAccess = DAOManager.games;
        gameAccess.deleteAllGames();
    }

    public ListGamesResult listAllGames(String token) throws DataAccessException {
        AuthDAI authAccess = DAOManager.auths;
        if (authAccess.getAuthFromToken(token) != null) {
            GameDAI gamesAccess = DAOManager.games;
            return new ListGamesResult(200, "all good", gamesAccess.listAllGames());
        } else {
            return new ListGamesResult(401, "Error: unauthorized", new ArrayList<>());
        }


    }

    public CreateGameResult createNewGame(CreateGameRequest request) throws DataAccessException {
        AuthDAI authAccess = DAOManager.auths;
        if (request.authToken() == null || request.gameName() == null) {
            return new CreateGameResult(400, "Error: bad request", null);
        }
        if (authAccess.getAuthFromToken(request.authToken()) != null) {
            GameDAI gamesAccess = DAOManager.games;
            int id;
            do {
                id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
            } while (gamesAccess.getGame(id) != null);
            gamesAccess.addNewGame(new GameData(id, null, null, request.gameName(), new ChessGame()));
            return new CreateGameResult(200, "all good", id);
        } else {
            return new CreateGameResult(401, "Error: unauthorized", null);
        }
    }

    public JoinGameResult joinGame(JoinGameRequest request) throws DataAccessException {
        AuthDAI authAccess = DAOManager.auths;
        if (authAccess.getAuthFromToken(request.authToken()) != null) {
            GameDAI gamesAccess = DAOManager.games;
            GameData oldGame = gamesAccess.getGame(request.gameID());
            if (oldGame == null) {
                return new JoinGameResult(400, "Error: bad request");
            } else {
                if (Objects.equals(request.playerColor(), "WHITE")) {
                    if (oldGame.whiteUsername() == null) {
                        gamesAccess.updateGameData(new GameData(
                                oldGame.gameID(),
                                authAccess.getAuthFromToken(request.authToken()).username(),
                                oldGame.blackUsername(),
                                oldGame.gameName(),
                                oldGame.game()
                        ));
                        return new JoinGameResult(200, "all good");
                    } else {
                        return new JoinGameResult(403, "Error: already taken");
                    }
                } else if ((Objects.equals(request.playerColor(), "BLACK"))) {
                    if (oldGame.blackUsername() == null) {
                        gamesAccess.updateGameData(new GameData(
                                oldGame.gameID(),
                                oldGame.whiteUsername(),
                                authAccess.getAuthFromToken(request.authToken()).username(),
                                oldGame.gameName(),
                                oldGame.game()
                        ));
                        return new JoinGameResult(200, "all good");
                    } else {
                        return new JoinGameResult(403, "Error: already taken");
                    }
                } else {
                    return new JoinGameResult(400, "Error: bad request");
                }
            }

        } else {
            return new JoinGameResult(401, "Error: unauthorized");
        }
    }

}
