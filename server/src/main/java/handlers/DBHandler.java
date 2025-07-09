package handlers;

import dataaccess.DataAccessException;
import service.GameService;
import service.UserService;
import service.requests.*;
import service.results.*;
import spark.*;
import com.google.gson.Gson;

public class DBHandler {
    private static final Gson gson = new Gson();
    private static final UserService userService = new UserService();


    public static Object handleClearDB(Request req, Response res) throws DataAccessException {
        System.out.println("handleClearDB called");
        GameService games = new GameService();
        games.clearAllGames();
        UserService users = new UserService();
        users.clearAllUsersAndAuths();
        res.status(200);
        res.type("application/json");
        return gson.toJson(null);
        
    }
}
