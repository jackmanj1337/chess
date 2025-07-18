import chess.*;
import model.GameData;
import prelogin.PreLogin;
import serverfacade.ServerFacade;

import java.util.ArrayList;

public class Main {
    private String activeAuthToken;
    ArrayList<GameData> gamesList = new ArrayList<>();


    public static void main(String[] args) {
        System.out.println(
                """
                           _____ _    _ ______  _____ _____\s
                          / ____| |  | |  ____|/ ____/ ____|
                         | |    | |__| | |__  | (___| (___ \s
                         | |    |  __  |  __|  \\___ \\\\___ \\\s
                         | |____| |  | | |____ ____) |___) |
                          \\_____|_|  |_|______|_____/_____/\s
                        """
        );

        ServerFacade server = new ServerFacade("http://localhost:8080");
        PreLogin preLogin = new PreLogin(server);
        while (true) {
            System.out.println(preLogin.ui());

        }
    }
}