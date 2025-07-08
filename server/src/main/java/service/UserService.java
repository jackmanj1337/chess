package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.results.LoginResult;
import service.results.RegisterResult;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;

import java.util.UUID;

public class UserService {
    public RegisterResult registerNewUser(RegisterRequest registerRequest) throws DataAccessException {
        UserDAO userAccess = new UserDAO();
        if (userAccess.getUser(registerRequest.username()) == null) {
            userAccess.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            LoginResult authdata = login(new LoginRequest(registerRequest.username(), registerRequest.password()));
            return new RegisterResult(authdata.httpCode(), authdata.message(), authdata.username(), authdata.authToken());
        } else {
            return new RegisterResult(403, "Error: already taken", null, null);
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserDAO userAccess = new UserDAO();
        AuthDAO authAccess = new AuthDAO();
        UserData loginTarget = userAccess.getUser(loginRequest.username());
        if (loginTarget == null) {
            return new LoginResult(500, "Specified user does not exist", loginRequest.username(), null);
        }
        if (loginRequest.password().equals(loginTarget.password())) {
            String token = UUID.randomUUID().toString();
            authAccess.addAuth(new AuthData(token, loginRequest.username()));
            return new LoginResult(200, "all good", loginRequest.username(), token);
        } else {
            return new LoginResult(401, "unauthorized", loginRequest.username(), null);
        }
    }

    public void logout(LogoutRequest logoutRequest) {
    }
}
