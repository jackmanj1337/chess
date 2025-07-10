package handlers;

import dataaccess.DataAccessException;
import service.GameService;
import service.UserService;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;
import service.results.JoinGameResult;
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

    public static Object handleCreateNewGame(Request req, Response res) throws DataAccessException {
        System.out.println("handleCreateNewGame called");
        CreateGameRequest requestp1 = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameRequest request = new CreateGameRequest(req.headers("Authorization"), requestp1.gameName());
        CreateGameResult results = gameService.createNewGame(request);

        res.status(results.httpCode());
        res.type("application/json");
        return gson.toJson(results);
    }

    public static Object handleJoinGame(Request req, Response res) throws DataAccessException {
        System.out.println("handleJoinGame called");
        JoinGameRequest requestp1 = gson.fromJson(req.body(), JoinGameRequest.class);
        JoinGameRequest request = new JoinGameRequest(req.headers("Authorization"), requestp1.playerColor(), requestp1.gameID());
        JoinGameResult results = gameService.joinGame(request);

        res.status(results.httpCode());
        res.type("application/json");
        return gson.toJson(results);
    }

}
