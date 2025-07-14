package dataaccess.mysqlimplemtation;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dainterface.GameDAI;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class GameDAOSQL extends SQLDAO implements GameDAI {


    public GameDAOSQL() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS game_data (
            `gameID` int NOT NULL UNIQUE,
            `whiteName` varchar(256) DEFAULT NULL,
            `blackName` varchar(256) DEFAULT NULL,
            `gameName` varchar(256) NOT NULL,
            `game` TEXT NOT NULL,
            PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }

    @Override
    public GameData addNewGame(GameData data) throws DataAccessException {
        String sql = "INSERT INTO game_data (gameID, whiteName, blackName, gameName, game) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(sql,
                data.gameID(),
                data.whiteUsername(),
                data.blackUsername(),
                data.gameName(),
                GSON.toJson(data.game()));
        return data;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT whiteName, blackName, gameName, game FROM game_data WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(gameID));
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GameData(gameID,
                            rs.getString("whiteName"),
                            rs.getString("blackName"),
                            rs.getString("gameName"),
                            GSON.fromJson(rs.getString("game"), ChessGame.class));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public GameData updateGameData(GameData data) throws DataAccessException {
        String sql = "UPDATE game_data SET whiteName = ?, blackName = ?, gameName = ?, game = ? WHERE gameID = ?";
        executeUpdate(sql,
                data.whiteUsername(),
                data.blackUsername(),
                data.gameName(),
                GSON.toJson(data.game()),
                data.gameID());
        return data;
    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        String sql = "TRUNCATE TABLE game_data";
        executeUpdate(sql);
    }

    @Override
    public ArrayList<GameData> listAllGames() throws DataAccessException {
        String sql = "SELECT gameID, whiteName, blackName, gameName, game FROM game_data";
        ArrayList<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                var game = new GameData(
                        rs.getInt("gameID"),
                        rs.getString("whiteName"),
                        rs.getString("blackName"),
                        rs.getString("gameName"),
                        GSON.fromJson(rs.getString("game"), ChessGame.class)
                );
                games.add(game);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to list games: " + e.getMessage());
        }
        return games;
    }
}