package postlogin.boardprinter;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import postlogin.gameplay.Gameplay;


import java.util.Collection;

import static postlogin.boardprinter.BoardSquare.*;
import static utilities.EscapeSequences.ERASE_SCREEN;

public class BoardPrinter {


    public static void print(ChessGame.TeamColor perspective, ChessPosition selectedPosition, GameData data) {
        BoardSquare[][] board = getFullBoard(data.game());

        if (selectedPosition != null && data.game().getBoard().getPiece(selectedPosition) != null) {
            board[selectedPosition.getRow()][selectedPosition.getColumn()].highlightPiece();
            Collection<ChessMove> moves = data.game().validMoves(selectedPosition);
            for (ChessMove move : moves) {
                board[move.getEndPosition().getRow()][move.getEndPosition().getColumn()].highlightMove();
            }
        }


        System.out.print("\n");


        if (perspective == ChessGame.TeamColor.WHITE || perspective == null) {
            for (int i = 9; i >= 0; i--) {
                for (int j = 0; j < 10; j++) {
                    board[i][j].print();
                }
                System.out.print("\n");
            }
        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 9; j >= 0; j--) {
                    board[i][j].print();
                }
                System.out.print("\n");
            }
        }

        if (data.game().getWinner() == null) {
            if (data.game().getTeamTurn() == ChessGame.TeamColor.WHITE) {
                System.out.print(data.whiteUsername() + "<WHITE> is the active player\n");
            }
            if (data.game().getTeamTurn() == ChessGame.TeamColor.BLACK) {
                System.out.print(data.blackUsername() + "<BLACK> is the active player\n");
            }
        } else {
            System.out.println(data.game().getWinner() + " has won the game.");
        }

    }

    private static BoardSquare[][] getFullBoard(ChessGame game) {
        BoardSquare[][] board = generateEmptyLabeledBoard();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                board[i][j].fillSquare(game.getBoard().getPiece(new ChessPosition(i, j)));
            }
        }
        return board;
    }

}
