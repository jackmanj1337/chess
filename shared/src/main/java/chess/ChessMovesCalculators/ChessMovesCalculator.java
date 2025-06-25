package chess.ChessMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public abstract class ChessMovesCalculator {
    protected enum TargetSquare {VALID_TARGET, INVALID_TARGET}

    protected enum Capture {CAPTURE, NO_CAPTURE}

    protected enum Promotion {PROMOTION, NO_PROMOTION}

    public abstract Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition myPosition);

    protected Object[] IsValidMove(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
        Object[] results = {TargetSquare.VALID_TARGET, Capture.NO_CAPTURE, Promotion.NO_PROMOTION};
        //check move ends on valid row
        if (endPosition.getRow() > board.CHESSBOARDROWS || endPosition.getRow() < 1) {
            results[0] = TargetSquare.INVALID_TARGET;
            return results;
        }
        //check move ends on valid col
        if (endPosition.getColumn() > board.CHESSBOARDCOLS || endPosition.getColumn() < 1) {
            results[0] = TargetSquare.INVALID_TARGET;
            return results;
        }


        // check if square is occupied by a friendly
        if (board.getPiece(endPosition) != null) {//square is occupied
            // check if occupying piece is friendly
            if (board.getPiece(myPosition).getTeamColor() == board.getPiece(endPosition).getTeamColor()) { // we share a color
                results[0] = TargetSquare.INVALID_TARGET;
            } else {
                results[1] = Capture.CAPTURE;
            }
        }

        if (endPosition.getRow() == 1 || endPosition.getRow() == 8) {
            results[2] = Promotion.PROMOTION;
        }

        return results;
    }


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
                Object[] results = IsValidMove(board, myPosition, positionToValidate);

                if (Objects.equals(results[1], Capture.CAPTURE)) {
                    possibleDiagonals.add(new ChessMove(myPosition, positionToValidate));
                    break;
                }
                if (Objects.equals(results[0], TargetSquare.INVALID_TARGET)) {
                    break;
                }

                possibleDiagonals.add(new ChessMove(myPosition, positionToValidate));
            }
        }


        return possibleDiagonals;
    }

    protected Collection<ChessMove> validateStraights(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleStraights = new ArrayList<>();
        int[][] directions = {
                {1, 0},  // up
                {-1, 0},  // down
                {0, -1}, // left
                {0, 1}  // right
        };

        for (int[] direction : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            while (true) {
                row += direction[0];
                col += direction[1];

                ChessPosition positionToValidate = new ChessPosition(row, col);
                Object[] results = IsValidMove(board, myPosition, positionToValidate);

                if (Objects.equals(results[1], Capture.CAPTURE)) {
                    possibleStraights.add(new ChessMove(myPosition, positionToValidate));
                    break;
                }
                if (Objects.equals(results[0], TargetSquare.INVALID_TARGET)) {
                    break;
                }

                possibleStraights.add(new ChessMove(myPosition, positionToValidate));
            }
        }


        return possibleStraights;
    }


}

