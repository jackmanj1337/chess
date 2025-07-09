package server;

import handlers.DBHandler;
import handlers.UserHandler;
import spark.*;

import static handlers.UserHandler.handleRegisterNewUser;

public class Server {


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

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
    }


}
