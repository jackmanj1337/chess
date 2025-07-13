package service;

import dataaccess.DAOManager;
import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.UserDAI;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.results.LoginResult;
import service.results.LogoutResult;
import service.results.RegisterResult;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;

import java.util.UUID;

public class UserService {
    public RegisterResult registerNewUser(RegisterRequest registerRequest) throws DataAccessException {
        UserDAI userAccess = DAOManager.users;
        if (userAccess.getUser(registerRequest.username()) == null) {
            userAccess.createUser(new UserData(registerRequest.username(), registerRequest.plainPassword(), registerRequest.email()));
            LoginResult authdata = login(new LoginRequest(registerRequest.username(), registerRequest.plainPassword()));
            return new RegisterResult(authdata.httpCode(), authdata.message(), authdata.username(), authdata.authToken());
        } else {
            return new RegisterResult(403, "Error: already taken", null, null);
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserDAI userAccess = DAOManager.users;
        AuthDAI authAccess = DAOManager.auths;
        UserData loginTarget = userAccess.getUser(loginRequest.username());
        if (loginTarget == null) {
            return new LoginResult(401, "Error: unauthorized", null, null);
        }
        if (loginRequest.password().equals(loginTarget.password())) {
            String token;
            token = UUID.randomUUID().toString();
            authAccess.addAuth(new AuthData(token, loginRequest.username()));
            return new LoginResult(200, "all good", loginRequest.username(), token);
        } else {
            return new LoginResult(401, "Error: unauthorized", null, null);
        }
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException {
        AuthDAI authAccess = DAOManager.auths;
        if (authAccess.getAuthFromToken(logoutRequest.authToken()) != null) {
            authAccess.deleteAuth(logoutRequest.authToken());
            return new LogoutResult(200, "all good");
        }
        return new LogoutResult(401, "Error: unauthorized");
    }

    public void clearAllUsersAndAuths() throws DataAccessException {
        UserDAI userAccess = DAOManager.users;
        AuthDAI authAccess = DAOManager.auths;
        authAccess.deleteAllAuths();
        userAccess.deleteAllUsers();
    }
}
