package client;

import chess.ChessGame;
import chess.ChessPosition;
import org.junit.jupiter.api.*;
import postlogin.boardprinter.BoardPrinter;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
