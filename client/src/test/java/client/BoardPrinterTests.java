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
    public void boardPrinterTest() {
        ChessGame game = new ChessGame();
        ChessPosition pos = null;
        ChessGame.TeamColor team = ChessGame.TeamColor.BLACK;
        BoardPrinter boardPrinter = new BoardPrinter();

        boardPrinter.print(game, team, pos);


        assertTrue(true);
    }

}
