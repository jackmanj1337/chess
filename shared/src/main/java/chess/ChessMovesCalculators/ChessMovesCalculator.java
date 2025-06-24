package chess.ChessMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ChessMovesCalculator {
    public abstract Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition myPosition);

    private boolean IsValid(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
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

    protected Collection<ChessMove> validateDiagonals(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleDiagonals = new ArrayList<>();
        int rowModifier;
        int colModifier;

        //check upper left diagonals
        rowModifier = 0;
        colModifier = 0;
        while (true) {

            rowModifier += 1;
            colModifier -= 1;

            ChessPosition positionToValidate = new ChessPosition(myPosition.getRow() + rowModifier, myPosition.getColumn() + colModifier);

            if (!IsValid(board, myPosition, positionToValidate)) {
                break;
            }

            possibleDiagonals.add(new ChessMove(myPosition, positionToValidate));
        }

        //check upper right diagonals
        rowModifier = 0;
        colModifier = 0;
        while (true) {

            rowModifier += 1;
            colModifier += 1;

            ChessPosition positionToValidate = new ChessPosition(myPosition.getRow() + rowModifier, myPosition.getColumn() + colModifier);

            if (!IsValid(board, myPosition, positionToValidate)) {
                break;
            }

            possibleDiagonals.add(new ChessMove(myPosition, positionToValidate));
        }

        //check lower left diagonals
        rowModifier = 0;
        colModifier = 0;
        while (true) {

            rowModifier -= 1;
            colModifier -= 1;

            ChessPosition positionToValidate = new ChessPosition(myPosition.getRow() + rowModifier, myPosition.getColumn() + colModifier);

            if (!IsValid(board, myPosition, positionToValidate)) {
                break;
            }

            possibleDiagonals.add(new ChessMove(myPosition, positionToValidate));
        }

        //check lower right diagonals
        rowModifier = 0;
        colModifier = 0;
        while (true) {

            rowModifier -= 1;
            colModifier += 1;

            ChessPosition positionToValidate = new ChessPosition(myPosition.getRow() + rowModifier, myPosition.getColumn() + colModifier);

            if (!IsValid(board, myPosition, positionToValidate)) {
                break;
            }

            possibleDiagonals.add(new ChessMove(myPosition, positionToValidate));
        }


        return possibleDiagonals;
    }
}

