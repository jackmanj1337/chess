package dataaccess.mysqlimplemtation;

import dataaccess.DataAccessException;
import dataaccess.dainterface.GameDAI;
import model.GameData;

import java.util.ArrayList;

public class GameDAOSQL extends SQLDAO implements GameDAI {
    @Override
    public GameData addNewGame(GameData data) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData updateGameData(GameData data) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllGames() throws DataAccessException {

    }

    @Override
    public ArrayList<GameData> listAllGames() throws DataAccessException {
        return null;
    }
}
