package handlers;

import dataaccess.DataAccessException;
import service.UserService;
import service.requests.*;
import service.results.*;
import spark.*;
import com.google.gson.Gson;


public class UserHandler {
    private static final Gson gson = new Gson();
    private static final UserService userService = new UserService();

    public static Object handleRegisterNewUser(Request req, Response res) throws DataAccessException {
        System.out.println("handleRegisterNewUser called");
        RegisterRequest newUserInfo = gson.fromJson(req.body(), RegisterRequest.class);
        res.type("application/json");

        if (newUserInfo.username().isEmpty() || newUserInfo.email().isEmpty() || newUserInfo.password().isEmpty()) {
            res.status(400);
        } else {
            RegisterResult result = userService.registerNewUser(newUserInfo);
            res.status(result.httpCode());
            return gson.toJson(result);
        }


        return "called successfully";
    }

    public static Object handleLogin(Request req, Response res) throws DataAccessException {
        System.out.println("handleLogin called");
        LoginRequest loginInfo = gson.fromJson(req.body(), LoginRequest.class);
        res.type("application/json");

        if (loginInfo.username().isEmpty() || loginInfo.password().isEmpty()) {
            res.status(400);
        } else {
            LoginResult result = userService.login(loginInfo);
            res.status(result.httpCode());
            return gson.toJson(result);
        }


        return "called successfully";
    }

    public static Object handleLogout(Request req, Response res) throws DataAccessException {
        System.out.println("handleLogout called");
        LogoutRequest logoutInfo = new LogoutRequest(req.headers("Authorization"));
        res.type("application/json");

        if (logoutInfo.authToken().isEmpty()) {
            res.status(400);
        } else {
            LogoutResult result = userService.logout(logoutInfo);
            res.status(result.httpCode());
            return gson.toJson(result);
        }


        return "called successfully";
    }
}
