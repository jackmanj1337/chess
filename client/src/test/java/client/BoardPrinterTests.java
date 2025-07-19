package client;

import chess.ChessGame;
import chess.ChessPosition;
import dataaccess.DAOManager;
import dataaccess.DataAccessException;
import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.GameDAI;
import dataaccess.dainterface.UserDAI;
import model.GameData;
import org.junit.jupiter.api.*;
import postlogin.PostLogin;
import postlogin.boardprinter.BoardPrinter;
import serverfacade.ServerFacade;
import serverfacade.results.LoginResult;
import service.GameService;
import service.UserService;
import service.requests.CreateGameRequest;
import service.requests.RegisterRequest;
import service.results.CreateGameResult;
import service.results.RegisterResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utilities.EscapeSequences.RESET_TEXT_COLOR;
import static utilities.EscapeSequences.SET_TEXT_BRIGHT_BLUE;

public class BoardPrinterTests {


    @Test
    public void BoardPrinterTest() {
        ChessGame game = new ChessGame();
        ChessPosition pos = null;
        ChessGame.TeamColor team = ChessGame.TeamColor.BLACK;
        BoardPrinter boardPrinter = new BoardPrinter();

        boardPrinter.print(game, team, pos);


        assertTrue(true);
    }

    @Test
    public void ListGameTest() throws DataAccessException {
        ArrayList<GameData> activeGames = new ArrayList<>();
        //activeGames.add(new GameData(532424323, "doug", "john", "coolGame", null));
        //activeGames.add(new GameData(32803, "grug", "greg", "coolGame", null));
        //activeGames.add(new GameData(8032341, null, "lonely", "otherGame", null));


        if (activeGames.isEmpty()) {
            System.out.print("There are no active games\nYou can create one with ");
            System.out.print(SET_TEXT_BRIGHT_BLUE + "create <GAME_NAME>" + RESET_TEXT_COLOR + "\n");
            return;
        }


        String header = String.format("║  %-5s ║ %-30s ║ %-30s ║ %-30s ║", "ID", "Game Name", "White Player", "Black Player");
        String topBorder = "╔════════╦════════════════════════════════╦════════════════════════════════╦════════════════════════════════╗";
        String midBorder = "╠════════╬════════════════════════════════╬════════════════════════════════╬════════════════════════════════╣";
        String bottomBorder = "╚════════╩════════════════════════════════╩════════════════════════════════╩════════════════════════════════╝";

        System.out.println(topBorder);
        System.out.println(header);
        System.out.println(midBorder);

        for (int i = 0; i < activeGames.size(); i++) {
            String white = activeGames.get(i).whiteUsername() != null ? activeGames.get(i).whiteUsername() : "(open)";
            String black = activeGames.get(i).blackUsername() != null ? activeGames.get(i).blackUsername() : "(open)";
            String row = String.format("║  %-5d ║ %-30s ║ %-30s ║ %-30s ║",
                    i + 1,
                    activeGames.get(i).gameName(),
                    white,
                    black);
            System.out.println(row);
        }

        System.out.println(bottomBorder);

        assertTrue(true);
    }
}
