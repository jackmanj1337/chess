package handlers;

import dataaccess.DataAccessException;
import service.GameService;
import service.UserService;
import service.results.ListGamesResult;
import spark.*;
import com.google.gson.Gson;


public class GameHandler {
    private static final Gson gson = new Gson();
    private static final UserService userService = new UserService();
    private static final GameService gameService = new GameService();

    public static Object handleListAllGames(Request req, Response res) throws DataAccessException {
        System.out.println("handleListAllGames called");

        ListGamesResult gamesList = gameService.listAllGames(req.headers("Authorization"));

        res.status(gamesList.httpCode());
        res.type("application/json");
        return gson.toJson(gamesList);
    }

}
