package handlers;

import dataaccess.DataAccessException;
import service.UserService;
import service.requests.*;
import service.results.*;
import spark.*;
import com.google.gson.Gson;


public class GameHandler {
    private static final Gson gson = new Gson();
    private static final UserService userService = new UserService();

    public static Object handleclear(Request req, Response res) throws DataAccessException {
        System.out.println("handleRegisterNewUser called");
        return null;
    }

}
