package dataaccess.localstorage;

import dataaccess.DataAccessException;
import dataaccess.dainterface.GameDAI;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameDAO implements GameDAI {

    static ArrayList<GameData> games = new ArrayList<>();

    @Override
    public GameData addNewGame(GameData data) throws DataAccessException {
        games.add(data);
        return data;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData data : games) {
            if (gameID == data.gameID()) {
                return data;
            }
        }
        return null;
    }

    @Override
    public GameData updateGameData(GameData data) throws DataAccessException {
        int position = games.indexOf(getGame(data.gameID()));
        games.set(position, data);
        return data;
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        games.removeIf(game -> gameID == (game.gameID()));
    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        games.clear();
    }

    @Override
    public ArrayList<GameData> listAllGames() throws DataAccessException {
        return new ArrayList<>(games);
    }


}
