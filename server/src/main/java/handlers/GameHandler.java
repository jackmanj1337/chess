package handlers;

import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
import service.GameService;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;
import service.results.ErrorResult;
import service.results.JoinGameResult;
import service.results.ListGamesResult;
import spark.*;
import com.google.gson.Gson;


public class GameHandler {
    private static final Gson GSON = new Gson();
    private static final GameService GAME_SERVICE = new GameService();

    public static Object handleListAllGames(Request req, Response res) {
        System.out.println("handleListAllGames called");
        res.type("application/json");
        try {
            ListGamesResult gamesList = GAME_SERVICE.listAllGames(req.headers("Authorization"));

            res.status(gamesList.httpCode());
            return GSON.toJson(gamesList);
        } catch (DataAccessException e) {
            res.status(500);
            return GSON.toJson(new ErrorResult("Error: " + e.getMessage()));
        }
    }

    public static Object handleCreateNewGame(Request req, Response res) throws DataAccessException {
        System.out.println("handleCreateNewGame called");
        res.type("application/json");

        try {
            CreateGameRequest requestp1 = GSON.fromJson(req.body(), CreateGameRequest.class);
            CreateGameRequest request = new CreateGameRequest(req.headers("Authorization"), requestp1.gameName());
            CreateGameResult results = GAME_SERVICE.createNewGame(request);
            res.status(results.httpCode());
            return GSON.toJson(results);
        } catch (DataAccessException e) {
            res.status(500);
            return GSON.toJson(new ErrorResult("Error: " + e.getMessage()));
        }
    }

    public static Object handleJoinGame(Request req, Response res) throws DataAccessException {
        System.out.println("handleJoinGame called");
        res.type("application/json");


        try {
            JoinGameRequest requestp1 = GSON.fromJson(req.body(), JoinGameRequest.class);
            JoinGameRequest request = new JoinGameRequest(req.headers("Authorization"), requestp1.playerColor(), requestp1.gameID());
            JoinGameResult results = GAME_SERVICE.joinGame(request);
            res.status(results.httpCode());
            return GSON.toJson(results);
        } catch (DataAccessException e) {
            res.status(500);
            return GSON.toJson(new ErrorResult("Error: " + e.getMessage()));
        }
    }

}
