package chess.ChessMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public abstract class ChessMovesCalculator {
    public abstract Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition myPosition);

    private String[] IsValidMove(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
        String[] results = {"valid target", "no capture"};
        //check move ends on valid row
        if (endPosition.getRow() > board.CHESSBOARDROWS || endPosition.getRow() < 1) {
            results[0] = "invalid target";
            return results;
        }
        //check move ends on valid col
        if (endPosition.getColumn() > board.CHESSBOARDCOLS || endPosition.getColumn() < 1) {
            results[0] = "invalid target";
            return results;
        }


        // check if square is occupied by a friendly
        if (board.getPiece(endPosition) != null) {//square is occupied
            // check if occupying piece is friendly
            if (board.getPiece(myPosition).getTeamColor() == board.getPiece(endPosition).getTeamColor()) { // we share a color
                results[0] = "invalid target";
                return results;
            } else {
                results[1] = "capture";
                return results;
            }
        }

        return results;
    }

    // TODO some bug in here when going down and right
    protected Collection<ChessMove> validateDiagonals(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleDiagonals = new ArrayList<>();
        int[][] directions = {
                {1, 1},  // upper right
                {1, -1},  // upper right
                {-1, -1}, // lower left
                {-1, 1}  // lower left
        };

        for (int[] direction : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            while (true) {
                row += direction[0];
                col += direction[1];

                ChessPosition positionToValidate = new ChessPosition(row, col);
                String[] results = IsValidMove(board, myPosition, positionToValidate);

                if (Objects.equals(results[1], "capture")) {
                    possibleDiagonals.add(new ChessMove(myPosition, positionToValidate));
                    break;
                }
                if (Objects.equals(results[0], "invalid target")) {
                    break;
                }

                possibleDiagonals.add(new ChessMove(myPosition, positionToValidate));
            }
        }


        return possibleDiagonals;
    }


}

