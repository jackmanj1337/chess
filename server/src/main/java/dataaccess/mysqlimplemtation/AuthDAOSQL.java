package dataaccess.mysqlimplemtation;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dainterface.AuthDAI;
import model.AuthData;


import java.sql.SQLException;


public class AuthDAOSQL extends SQLDAO implements AuthDAI {
    public AuthDAOSQL() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS auth_data (
                  username VARCHAR(256) NOT NULL,
                  authToken VARCHAR(256) NOT NULL,
                  PRIMARY KEY (authToken),
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }

    @Override
    public AuthData addAuth(AuthData auth) throws DataAccessException {
        String sql = "INSERT INTO auth_data (username, authToken) VALUES (?, ?)";
        executeUpdate(sql, auth.username(), auth.authToken());
        return auth;
    }

    @Override
    public AuthData getAuthFromToken(String auth) throws DataAccessException {
        String sql = "SELECT username FROM auth_data WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, auth);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(auth, rs.getString("username"));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }


    @Override
    public void deleteAuth(String authkey) throws DataAccessException {
        var statement = "DELETE FROM auth_data WHERE authToken=?";
        executeUpdate(statement, authkey);
    }

    @Override
    public void deleteAllAuths() throws DataAccessException {
        var statement = "TRUNCATE auth_data";
        executeUpdate(statement);
    }


}
