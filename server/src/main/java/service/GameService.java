package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;

public class GameService {
    public void clearAllGames() throws DataAccessException {
        GameDAO gameAccess = new GameDAO();
        gameAccess.deleteAllGames();
    }

}
