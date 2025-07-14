package handlers;

import dataaccess.DataAccessException;
import service.UserService;
import service.requests.*;
import service.results.*;
import spark.*;
import com.google.gson.Gson;


public class UserHandler {
    private static final Gson GSON = new Gson();
    private static final UserService USER_SERVICE = new UserService();

    public static Object handleRegisterNewUser(Request req, Response res) throws DataAccessException {
        System.out.println("handleRegisterNewUser called");
        RegisterRequest newUserInfo = GSON.fromJson(req.body(), RegisterRequest.class);
        res.type("application/json");

        if (newUserInfo.username() == null || newUserInfo.email() == null || newUserInfo.password() == null) {
            res.status(400);
            RegisterResult result = new RegisterResult(400, "Error: bad request", null, null);
            return GSON.toJson(result);
        } else {
            RegisterResult result = USER_SERVICE.registerNewUser(newUserInfo);
            res.status(result.httpCode());
            return GSON.toJson(result);
        }
    }

    public static Object handleLogin(Request req, Response res) throws DataAccessException {
        System.out.println("handleLogin called");
        LoginRequest loginInfo = GSON.fromJson(req.body(), LoginRequest.class);
        res.type("application/json");

        if (loginInfo.username() == null || loginInfo.username().isEmpty() ||
                loginInfo.password() == null || loginInfo.password().isEmpty()) {
            res.status(400);
            LoginResult result = new LoginResult(400, "Error: bad request", null, null);
            return GSON.toJson(result);
        } else {
            LoginResult result = USER_SERVICE.login(loginInfo);
            res.status(result.httpCode());
            return GSON.toJson(result);
        }


    }

    public static Object handleLogout(Request req, Response res) throws DataAccessException {
        System.out.println("handleLogout called");
        LogoutRequest logoutInfo = new LogoutRequest(req.headers("Authorization"));
        res.type("application/json");

        if (logoutInfo.authToken().isEmpty()) {
            res.status(400);
        } else {
            LogoutResult result = USER_SERVICE.logout(logoutInfo);
            res.status(result.httpCode());
            return GSON.toJson(result);
        }


        return "called successfully";
    }
}
