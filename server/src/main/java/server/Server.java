package server;

import handlers.DBHandler;
import handlers.GameHandler;
import handlers.UserHandler;
import spark.*;
import server.WebsocketServer;


public class Server {


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", WebsocketServer.class);
        // Register your endpoints and handle exceptions here.
        registerRoutes();


        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void registerRoutes() {
        Spark.post("/user", UserHandler::handleRegisterNewUser);
        System.out.println("Registered POST /user");

        Spark.post("/session", UserHandler::handleLogin);
        System.out.println("Registered POST /session");

        Spark.delete("/session", UserHandler::handleLogout);
        System.out.println("Registered DELETE /session");

        Spark.delete("/db", DBHandler::handleClearDB);
        System.out.println("Registered DELETE /db");

        Spark.get("/game", GameHandler::handleListAllGames);
        System.out.println("Registered GET /game");

        Spark.post("/game", GameHandler::handleCreateNewGame);

        Spark.put("/game", GameHandler::handleJoinGame);
    }


}
