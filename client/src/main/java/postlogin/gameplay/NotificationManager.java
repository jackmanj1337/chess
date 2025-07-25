package postlogin.gameplay;

import model.GameData;

import static utilities.EscapeSequences.RESET_TEXT_COLOR;
import static utilities.EscapeSequences.SET_TEXT_BRIGHT_RED;


public class NotificationManager {

    private String username;

    public NotificationManager(String username) {
        this.username = username;
    }

    public void handleLoadGame(GameData data) {
        Gameplay.updateGameData(data);
        System.out.print("[" + username + "] >>> ");
    }

    public void handleNotification(String message) {
        System.out.print("\n");
        System.out.println(message);
        System.out.print("[" + username + "] >>> ");

    }

    public void handleError(String errorMessage) {
        System.out.print("\n");
        System.out.print(SET_TEXT_BRIGHT_RED);
        System.out.println(errorMessage);
        System.out.print(RESET_TEXT_COLOR);
        System.out.print("[" + username + "] >>> ");

    }
}
