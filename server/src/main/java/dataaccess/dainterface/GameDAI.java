package dataaccess.dainterface;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public interface GameDAI {

    public GameData addNewGame(GameData data) throws DataAccessException;

    public GameData getGame(int gameID) throws DataAccessException;

    public GameData updateGameData(GameData data) throws DataAccessException;

    //public void deleteGame(int gameID) throws DataAccessException;

    public void deleteAllGames() throws DataAccessException;

    public ArrayList<GameData> listAllGames() throws DataAccessException;
}
