package chess.ChessMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

// TODO add en passant capture
public class PawnMovesCalculator extends ChessMovesCalculator {
    @Override
    public Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        //check directly in front of pawn
        int targetRow = myPosition.getRow() + 1;
        int targetCol = myPosition.getColumn();
        ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
        String[] results = IsValidMove(board, myPosition, targetPosition);
        if (Objects.equals(results[0], "valid target") &&
                Objects.equals(results[1], "no capture") {
            possibleMoves.add(new ChessMove(myPosition, targetPosition));
        }




        return possibleMoves;
    }
}
