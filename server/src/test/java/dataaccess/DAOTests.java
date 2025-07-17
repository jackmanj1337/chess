package dataaccess;

import chess.ChessGame;
import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.GameDAI;
import dataaccess.dainterface.UserDAI;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DAOTests {
    UserDAI users = DAOManager.users;
    AuthDAI auths = DAOManager.auths;
    GameDAI games = DAOManager.games;


    @BeforeEach
    public void setUp() throws DataAccessException {
        // Reset before each test
        users.deleteAllUsers();
        auths.deleteAllAuths();
        games.deleteAllGames();
    }


    // UserDAO Tests
    @Test
    void createNewUserTest() throws DataAccessException {
        var user = new UserData("doug", "password", "doug@testing.com");
        assertDoesNotThrow(() -> users.createUser(user));
    }

    @Test
    void cannotHaveDuplicateUsersTest() throws DataAccessException {
        var user = new UserData("doug", "password", "doug@testing.com");
        users.createUser(user);

        assertThrows(DataAccessException.class, () -> users.createUser(user));
    }

    @Test
    void getUserTest() throws DataAccessException {
        var user = new UserData("doug", "password", "doug@testing.com");
        users.createUser(user);
        var returned = users.getUser("doug");

        assertNotNull(returned);
        assertEquals(user.username(), returned.username());
        assertEquals(user.password(), returned.password());
        assertEquals(user.email(), returned.email());
    }

    @Test
    void getImaginaryUserReturnsNullTest() throws DataAccessException {
        var returned = users.getUser("doug");
        assertNull(returned);
    }


    @Test
    void deleteAllUsersTest() throws DataAccessException {
        var user = new UserData("doug", "password", "doug@testing.com");

        users.createUser(user);
        users.deleteAllUsers();
        user = users.getUser("doug");

        assertNull(user);
    }

    // AuthDAO Tests
    @Test
    void addAuthTest() throws DataAccessException {
        assertDoesNotThrow(() -> auths.addAuth(new AuthData("dummyToken", "doug")));
    }

    @Test
    void authDataMustBeCompleteTest() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> auths.addAuth(new AuthData(null, null)));
    }

    @Test
    void getAuthFromTokenTest() throws DataAccessException {
        var newAuth = auths.addAuth(new AuthData("dummyToken", "doug"));
        assertNotNull(auths.getAuthFromToken(newAuth.authToken()));
    }

    @Test
    void getAuthFromBadTokenReturnsNullTest() throws DataAccessException {
        assertNull(auths.getAuthFromToken("badToken"));
    }

    @Test
    void deleteAuthTest() throws DataAccessException {
        var newAuth = auths.addAuth(new AuthData("dummyToken", "doug"));
        auths.deleteAuth(newAuth.authToken());
        assertNull(auths.getAuthFromToken(newAuth.authToken()));
    }

    @Test
    void deleteAllAuthsTest() throws DataAccessException {
        var auth1 = auths.addAuth(new AuthData("dummyToken", "doug"));
        var auth2 = auths.addAuth(new AuthData("testToken", "john"));
        auths.deleteAllAuths();
        assertNull(auths.getAuthFromToken(auth1.authToken()));
        assertNull(auths.getAuthFromToken(auth2.authToken()));
    }

    // GameDao Tests
    @Test
    void addNewGameTest() throws DataAccessException {
        GameData game = new GameData(0, null, null, "dougsGame", new ChessGame());
        GameData added = games.addNewGame(game);
        assertTrue(added.gameID() >= 1);
    }

    @Test
    void addBadGameTest() throws DataAccessException {
        GameData game = new GameData(0, null, null, null, new ChessGame());
        assertThrows(DataAccessException.class, () -> games.addNewGame(game));
    }

    @Test
    void getGameTest() throws DataAccessException {
        GameData game = new GameData(0, null, null, "dougsGame", new ChessGame());
        GameData added = games.addNewGame(game);
        GameData retrieved = games.getGame(added.gameID());
        assertNotNull(retrieved);
    }

    @Test
    void getImaginaryGameTest() throws DataAccessException {
        assertNull(games.getGame(-1));
    }

    @Test
    void updateGameDataTest() throws DataAccessException {
        GameData game = new GameData(0, null, null, "dougsGame", new ChessGame());
        GameData added = games.addNewGame(game);
        GameData update = new GameData(added.gameID(), "doug", added.blackUsername(), added.gameName(), added.game());
        assertDoesNotThrow(() -> games.updateGameData(update));
    }

    @Test
    void updateImaginaryGameDataTest() throws DataAccessException {
        GameData game = new GameData(0, null, null, "dougsGame", new ChessGame());
        games.updateGameData(game);
        assertNull(games.getGame(game.gameID()));
    }

    @Test
    void deleteAllGamesTest() throws DataAccessException {
        GameData game1 = new GameData(-1, "doug", "greg", "dougsGame", new ChessGame());
        games.addNewGame(game1);
        GameData game2 = new GameData(-1, "greg", "john", "johnsGame", new ChessGame());
        games.addNewGame(game2);
        games.deleteAllGames();
        ArrayList<GameData> gameList = games.listAllGames();
        assertEquals(0, gameList.size());
    }

    @Test
    void listAllGamesTest() throws DataAccessException {
        GameData game1 = new GameData(-1, "doug", "greg", "dougsGame", new ChessGame());
        games.addNewGame(game1);
        GameData game2 = new GameData(-1, "greg", "john", "johnsGame", new ChessGame());
        games.addNewGame(game2);
        ArrayList<GameData> gameList = games.listAllGames();
        assertEquals(2, gameList.size());
    }

    @Test
    void listAllGamesEmptyTest() throws DataAccessException {
        ArrayList<GameData> gameList = games.listAllGames();
        assertEquals(0, gameList.size());
    }
}
