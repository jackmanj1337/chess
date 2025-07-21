package postlogin.boardprinter;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;


import java.util.Collection;

import static postlogin.boardprinter.BoardSquare.*;

public class BoardPrinter {


    public static void print(ChessGame game, ChessGame.TeamColor perspective, ChessPosition selectedPosition) {
        BoardSquare[][] board = getFullBoard(game);

        if (selectedPosition != null && game.getBoard().getPiece(selectedPosition) != null) {
            board[selectedPosition.getRow()][selectedPosition.getColumn()].highlightPiece();
            Collection<ChessMove> moves = game.validMoves(selectedPosition);
            for (ChessMove move : moves) {
                board[move.getEndPosition().getRow()][move.getEndPosition().getColumn()].highlightMove();
            }
        }


        if (perspective == ChessGame.TeamColor.WHITE) {
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
