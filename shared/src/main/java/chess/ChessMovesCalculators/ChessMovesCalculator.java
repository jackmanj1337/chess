package chess.ChessMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ChessMovesCalculator {
    public abstract Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition myPosition);

    private boolean IsValidMove(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
        boolean isValid = true;
        //check move ends on the board

        //check move ends on valid row
        if (isValid) {
            if (endPosition.getRow() > 7 || endPosition.getRow() < 0) {
                isValid = false;
            }
        }
        //check move ends on valid col
        if (isValid) {
            if (endPosition.getColumn() > 7 || endPosition.getColumn() < 0) {
                isValid = false;
            }
        }


        // check if square is occupied by a friendly
        if (isValid) {
            if (board.getPiece(endPosition) != null) {//square is occupied
                // check if occupying piece is friendly
                if (board.getPiece(myPosition).getTeamColor() == board.getPiece(endPosition).getTeamColor()) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    // TODO some bug in here when going down and right
    protected Collection<ChessMove> validateDiagonals(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleDiagonals = new ArrayList<>();
        int[][] directions = {
                {1, 1},  // upper right
                {-1, 1},  // lower right
                {-1, -1}, // lower left
                {1, -1}  // upper left
        };

        for (int[] direction : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            while (true) {
                row += direction[0];
                col += direction[1];

                ChessPosition positionToValidate = new ChessPosition(row, col);

                if (!IsValidMove(board, myPosition, positionToValidate)) {
                    break;
                }

                possibleDiagonals.add(new ChessMove(myPosition, positionToValidate));
            }
        }


        return possibleDiagonals;
    }


}

