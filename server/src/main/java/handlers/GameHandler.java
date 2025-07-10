package handlers;

import dataaccess.DataAccessException;
import service.GameService;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;
import service.results.JoinGameResult;
import service.results.ListGamesResult;
import spark.*;
import com.google.gson.Gson;


public class GameHandler {
    private static final Gson GSON = new Gson();
    private static final GameService GAME_SERVICE = new GameService();

    public static Object handleListAllGames(Request req, Response res) throws DataAccessException {
        System.out.println("handleListAllGames called");

        ListGamesResult gamesList = GAME_SERVICE.listAllGames(req.headers("Authorization"));

        res.status(gamesList.httpCode());
        res.type("application/json");
        return GSON.toJson(gamesList);
    }

    public static Object handleCreateNewGame(Request req, Response res) throws DataAccessException {
        System.out.println("handleCreateNewGame called");
        CreateGameRequest requestp1 = GSON.fromJson(req.body(), CreateGameRequest.class);
        CreateGameRequest request = new CreateGameRequest(req.headers("Authorization"), requestp1.gameName());
        CreateGameResult results = GAME_SERVICE.createNewGame(request);

        res.status(results.httpCode());
        res.type("application/json");
        return GSON.toJson(results);
    }

    public static Object handleJoinGame(Request req, Response res) throws DataAccessException {
        System.out.println("handleJoinGame called");
        JoinGameRequest requestp1 = GSON.fromJson(req.body(), JoinGameRequest.class);
        JoinGameRequest request = new JoinGameRequest(req.headers("Authorization"), requestp1.playerColor(), requestp1.gameID());
        JoinGameResult results = GAME_SERVICE.joinGame(request);

        res.status(results.httpCode());
        res.type("application/json");
        return GSON.toJson(results);
    }

}
