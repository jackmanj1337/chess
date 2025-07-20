package client;

import dataaccess.DAOManager;
import dataaccess.DataAccessException;
import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.GameDAI;
import dataaccess.dainterface.UserDAI;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;
import serverfacade.requests.*;
import serverfacade.results.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    UserDAI users = DAOManager.users;
    AuthDAI auths = DAOManager.auths;
    GameDAI games = DAOManager.games;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void clearDB() throws DataAccessException {
        users.deleteAllUsers();
        auths.deleteAllAuths();
        games.deleteAllGames();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTest() {
        RegisterResult result = facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));
        assertEquals(200, result.httpCode());
    }

    @Test
    public void registerTestNullPassword() {
        RegisterResult result = facade.register(new RegisterRequest("doug", null, "doug@testing.com"));
        assertEquals(400, result.httpCode());
    }

    @Test
    public void loginTest() {
        facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));
        LoginResult result = facade.login(new LoginRequest("doug", "password"));
        assertEquals(200, result.httpCode());
    }

    @Test
    public void loginBadPasswordTest() {
        facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));
        LoginResult result = facade.login(new LoginRequest("doug", "passeord"));
        assertEquals(401, result.httpCode());
    }

    @Test
    public void createGameTest() {
        RegisterResult rResult = facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));
        CreateGameResult cGResult = facade.createGame(new CreateGameRequest(rResult.authToken(), "coolGame"));
        assertEquals(200, cGResult.httpCode());
    }

    @Test
    public void createGameBadAuthTest() {
        CreateGameResult cGResult = facade.createGame(new CreateGameRequest("spoofedToken", "coolGame"));
        assertEquals(401, cGResult.httpCode());
    }

    @Test
    public void listGamesTest() {
        RegisterResult rResult = facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));
        ListGamesResult lGResult = facade.listGames(new ListGamesRequest(rResult.authToken()));
        assertEquals(200, lGResult.httpCode());
    }

    @Test
    public void listGamesBadAuthTest() {
        ListGamesResult lGResult = facade.listGames(new ListGamesRequest("badToken"));
        assertEquals(401, lGResult.httpCode());
    }

    @Test
    public void joinGameTest() {
        RegisterResult rResult = facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));
        CreateGameResult cGResult = facade.createGame(new CreateGameRequest(rResult.authToken(), "coolGame"));
        JoinGameResult jGResult = facade.joinGame(new JoinGameRequest(rResult.authToken(), "WHITE", cGResult.gameID()));
        assertEquals(200, jGResult.httpCode());
    }

    @Test
    public void joinGameColorTakenTest() {
        RegisterResult rResult = facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));
        CreateGameResult cGResult = facade.createGame(new CreateGameRequest(rResult.authToken(), "coolGame"));
        facade.joinGame(new JoinGameRequest(rResult.authToken(), "WHITE", cGResult.gameID()));
        JoinGameResult jGResult = facade.joinGame(new JoinGameRequest(rResult.authToken(), "WHITE", cGResult.gameID()));
        assertEquals(403, jGResult.httpCode());
    }

    @Test
    public void clearDBTest() {
        assertEquals(200, facade.clearDB().httpCode());
    }


}
