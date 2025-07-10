package service;

import dataaccess.localstorage.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.localstorage.GameDAO;
import dataaccess.localstorage.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.*;
import service.results.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    @BeforeEach
    public void setUp() throws DataAccessException {
        // Reset before each test
        UserDAO users = new UserDAO();
        users.deleteAllUsers();

        AuthDAO auths = new AuthDAO();
        auths.deleteAllAuths();

        GameDAO games = new GameDAO();
        games.deleteAllGames();
    }

    @Test
    public void registerNewUserTest() throws DataAccessException {
        UserService userservice = new UserService();
        RegisterResult result = userservice.registerNewUser(new RegisterRequest("doug", "pass", "doug@testing.com"));
        assertEquals(200, result.httpCode(), "could not register doug");
    }

    @Test
    public void cannotRegisterDuplicateUserTest() throws DataAccessException {
        UserService userservice = new UserService();
        userservice.registerNewUser(new RegisterRequest("doug", "securepass", "doug2@testing.com"));
        RegisterResult result = userservice.registerNewUser(new RegisterRequest("doug", "pass", "doug@testing.com"));
        assertEquals(403, result.httpCode(), "registered 2 \"doug\"s");
    }

    @Test
    public void logoutExistingUser() throws DataAccessException {
        UserService userservice = new UserService();
        RegisterResult regResult = userservice.registerNewUser(new RegisterRequest("doug", "pass", "doug@testing.com"));
        LogoutResult loutResult = userservice.logout(new LogoutRequest(regResult.authToken()));
        assertEquals(200, loutResult.httpCode(), "should have gotten a 200 response");
    }


    @Test
    public void logoutImaginaryUser() throws DataAccessException {
        UserService userservice = new UserService();
        LogoutResult loutResult = userservice.logout(new LogoutRequest("Spoofed token"));
        assertEquals(401, loutResult.httpCode(), "should have gotten a 401 response");
    }

    @Test
    public void loginExistingUser() throws DataAccessException {
        UserService userservice = new UserService();
        RegisterResult regResult = userservice.registerNewUser(new RegisterRequest("doug", "pass", "doug@testing.com"));
        userservice.logout(new LogoutRequest(regResult.authToken()));
        LoginResult linResult = userservice.login(new LoginRequest("doug", "pass"));

        assertEquals(200, linResult.httpCode(), "should have gotten a 200 response");
    }


    @Test
    public void loginImaginaryUser() throws DataAccessException {
        UserService userservice = new UserService();
        LoginResult linResult = userservice.login(new LoginRequest("BadHacker", "definitely a real password"));
        assertEquals(401, linResult.httpCode(), "should have gotten a 401 response");
    }

    @Test
    public void clearDBTest() throws DataAccessException {
        UserService userservice = new UserService();
        GameService gameservice = new GameService();
        userservice.registerNewUser(new RegisterRequest("doug", "pass", "doug@testing.com"));
        // add createGame call

        userservice.clearAllUsersAndAuths();
        gameservice.clearAllGames();

        LoginResult linResult = userservice.login(new LoginRequest("doug", "pass"));
        //add getGame call
        assertEquals(401, linResult.httpCode());

    }

    @Test
    public void listAllGamesTest() throws DataAccessException {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        RegisterResult regResult = userService.registerNewUser(new RegisterRequest("doug", "dougspassword", "doug@arealwebsite.com"));
        ListGamesResult listresult = gameService.listAllGames(regResult.authToken());
        assertEquals(200, listresult.httpCode());
    }

    @Test
    public void cannotListAllGamesWithoutAuthTest() throws DataAccessException {
        GameService gameService = new GameService();
        ListGamesResult listResult = gameService.listAllGames("very legit auth token");
        assertEquals(401, listResult.httpCode());
    }


    @Test
    public void createGameTest() throws DataAccessException {
        UserService userservice = new UserService();
        RegisterResult regResult = userservice.registerNewUser(new RegisterRequest("doug", "pass", "doug@testing.com"));
        GameService gameservice = new GameService();
        CreateGameResult cGResult = gameservice.createNewGame(new CreateGameRequest(regResult.authToken(), "really fun game"));
        assertEquals(200, cGResult.httpCode());
    }

    @Test
    public void cannotCreateGameWithoutAuthTest() throws DataAccessException {
        GameService gameservice = new GameService();
        CreateGameResult cGResult = gameservice.createNewGame(new CreateGameRequest(null, "really fun game"));
        assertEquals(401, cGResult.httpCode());
    }

    @Test
    public void joinGameAsWhite() throws DataAccessException {
        UserService userservice = new UserService();
        RegisterResult regResult = userservice.registerNewUser(new RegisterRequest("doug", "pass", "doug@testing.com"));
        GameService gameservice = new GameService();
        CreateGameResult cGResult = gameservice.createNewGame(new CreateGameRequest(regResult.authToken(), "really fun game"));
        JoinGameResult jGResult = gameservice.joinGame(new JoinGameRequest(regResult.authToken(), "WHITE", cGResult.gameID()));
        assertEquals(200, jGResult.httpCode());

    }

    @Test
    public void cannotJoinGameAsGreen() throws DataAccessException {
        UserService userservice = new UserService();
        RegisterResult regResult = userservice.registerNewUser(new RegisterRequest("doug", "pass", "doug@testing.com"));
        GameService gameservice = new GameService();
        CreateGameResult cGResult = gameservice.createNewGame(new CreateGameRequest(regResult.authToken(), "really fun game"));
        JoinGameResult jGResult = gameservice.joinGame(new JoinGameRequest(regResult.authToken(), "GREEN", cGResult.gameID()));
        assertEquals(400, jGResult.httpCode());

    }


}
