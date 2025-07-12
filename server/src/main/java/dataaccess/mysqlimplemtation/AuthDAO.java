package dataaccess.mysqlimplemtation;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dainterface.AuthDAI;
import model.AuthData;
import dataaccess.mysqlimplemtation.SQLDAO;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AuthDAO extends SQLDAO implements AuthDAI {
    public AuthDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS kv_pair (
                  id INT NOT NULL AUTO_INCREMENT,
                  username VARCHAR(256) NOT NULL,
                  authToken VARCHAR(256) NOT NULL,
                  PRIMARY KEY (id),
                  UNIQUE INDEX (key_string)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }

    @Override
    public AuthData addAuth(AuthData auth) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuthFromToken(String auth) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuthFromUsername(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authkey) throws DataAccessException {

    }

    @Override
    public void deleteAllAuths() throws DataAccessException {

    }


}
