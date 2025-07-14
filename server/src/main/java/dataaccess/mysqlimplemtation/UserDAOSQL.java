package dataaccess.mysqlimplemtation;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dainterface.UserDAI;
import model.UserData;

import java.sql.SQLException;

public class UserDAOSQL extends SQLDAO implements UserDAI {
    public UserDAOSQL() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS user_data (
                  `username` VARCHAR(256) NOT NULL  UNIQUE,
                  `passwordHash` VARCHAR(256) NOT NULL,
                  `email` VARCHAR(256) NOT NULL,
                  PRIMARY KEY (username),
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        String sql = "INSERT INTO user_data (username, passwordHash, email) VALUES (?, ?, ?)";
        executeUpdate(sql, user.username(), user.password(), user.email());
        return user;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, passwordHash, email FROM user_data WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserData(
                            rs.getString("username"),
                            rs.getString("passwordHash"),
                            rs.getString("email")
                    );
                } else {
                    return null; // user not found
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
    }

    @Override
    public void deleteAllUsers() throws DataAccessException {
        String sql = "TRUNCATE TABLE user_data";
        executeUpdate(sql);
    }
}
