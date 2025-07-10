package handlers;

import dataaccess.DataAccessException;
import service.GameService;
import service.UserService;
import spark.*;
import com.google.gson.Gson;

public class DBHandler {
    private static final Gson GSON = new Gson();


    public static Object handleClearDB(Request req, Response res) throws DataAccessException {
        System.out.println("handleClearDB called");
        GameService games = new GameService();
        games.clearAllGames();
        UserService users = new UserService();
        users.clearAllUsersAndAuths();
        res.status(200);
        res.type("application/json");
        return GSON.toJson(null);

    }
}
