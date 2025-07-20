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
        int portNum = 8080;
        var port = server.run(portNum);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + portNum);
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
    public void sampleTest() {


        System.out.println("calling facade register");
        RegisterResult result = facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));

        System.out.println("returned results");
        System.out.println(result.toString());

        System.out.println("\n\ncalling facade login");
        LoginResult linresult = facade.login(new LoginRequest("doug", "password"));
        System.out.println("returned results");
        System.out.println(linresult.toString());

        System.out.println("\n\ncalling facade logout");
        LogoutResult loutresult = facade.logout(new LogoutRequest(linresult.authToken()));
        System.out.println("returned results");
        System.out.println(loutresult.toString());

        Assertions.assertTrue(true);
    }

    @Test
    public void RegisterTest() {
        RegisterResult result = facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));
        assertEquals(200, result.httpCode());
    }

    @Test
    public void RegisterTestNullPassword() {
        RegisterResult result = facade.register(new RegisterRequest("doug", null, "doug@testing.com"));
        assertEquals(400, result.httpCode());
    }

    @Test
    public void LoginTest() {
        facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));
        LoginResult result = facade.login(new LoginRequest("doug", "password"));
        assertEquals(200, result.httpCode());
    }

    @Test
    public void LoginBadPasswordTest() {
        facade.register(new RegisterRequest("doug", "password", "doug@testing.com"));
        LoginResult result = facade.login(new LoginRequest("doug", "passeord"));
        assertEquals(401, result.httpCode());
    }


}
