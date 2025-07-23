package postlogin.gameplay;

import model.GameData;

import static utilities.EscapeSequences.RESET_TEXT_COLOR;
import static utilities.EscapeSequences.SET_TEXT_BRIGHT_RED;

public class NotificationManager {
    public static void handleLoadGame(GameData data) {
        Gameplay.updateGameData(data);
    }

    public static void handleNotification(String message) {
        System.out.println(message);
    }

    public static void handleError(String errorMessage) {
        System.out.print(SET_TEXT_BRIGHT_RED);
        System.out.println(errorMessage);
        System.out.print(RESET_TEXT_COLOR);
    }
}
