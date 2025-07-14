package handlers;

import dataaccess.DataAccessException;
import service.GameService;
import service.UserService;
import service.results.ErrorResult;
import spark.*;
import com.google.gson.Gson;

public class DBHandler {
    private static final Gson GSON = new Gson();


    public static Object handleClearDB(Request req, Response res) {
        res.type("application/json");
        System.out.println("handleClearDB called");
        try {
            GameService games = new GameService();
            games.clearAllGames();
            UserService users = new UserService();
            users.clearAllUsersAndAuths();
            res.status(200);
            return GSON.toJson(null);
        } catch (DataAccessException e) {
            res.status(500);
            return GSON.toJson(new ErrorResult("Error: " + e.getMessage()));
        }

    }
}
