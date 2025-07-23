package postlogin.gameplay;

import model.GameData;
import serverfacade.ServerFacade;
import serverfacade.WebsocketFacade;
import serverfacade.requests.LogoutRequest;
import websocket.messages.ServerMessage;

import java.util.Objects;
import java.util.Scanner;

import static prelogin.PreLogin.badInputResponse;

public class Gameplay {
    private String authToken;
    private String username;
    private int gameID;
    private WebsocketFacade websocketFacade;
    private ServerFacade server;
    private Scanner scanner;

    public Gameplay(String authToken, String username, int gameID, Scanner scanner, ServerFacade server) {
        this.authToken = authToken;
        this.username = username
        this.gameID = gameID;
        this.scanner = scanner;
        this.server = server;
        websocketFacade = new WebsocketFacade(authToken, gameID);
    }

    public void ui(){

        while(true){
            System.out.print("[" + username + "] >>> ");

            if (!scanner.hasNextLine()) {
                System.out.println("Input stream closed. Exiting...");
                websocketFacade.sendMessage();
                server.logout(new LogoutRequest(authToken));
                break;
            }

            String input = scanner.nextLine().trim();

            String[] split = input.split("\\s+");

            switch (split[0].toLowerCase()) {
                case "create":
                    break;
                default:
                    badInputResponse();

            }
        }
    }


}
