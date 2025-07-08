package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.RegisterRequest;
import service.UserService;
import service.results.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    @BeforeEach
    public void setUp() throws DataAccessException {
        // Reset before each test
        UserDAO users = new UserDAO();
        users.deleteAllUsers();

        AuthDAO auths = new AuthDAO();
        auths.deleteAllAuths();
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


}
