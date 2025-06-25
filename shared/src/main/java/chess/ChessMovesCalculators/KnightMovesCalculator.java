package chess.ChessMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class KnightMovesCalculator extends ChessMovesCalculator {
    @Override
    public Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();


        int[][] directions = {
                {2, -1},  // up left
                {2, 1},  // up right
                {1, 2}, // right up
                {-1, 2},  // right down
                {-2, 1},  // down right
                {-2, -1}, //down left
                {-1, -2}, // left down
                {1, -2}, //left up
        };
        for (int[] direction : directions) {
            int targetRow = myPosition.getRow() + direction[0];
            int targetCol = myPosition.getColumn() + direction[1];
            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            if (Objects.equals(IsValidMove(board, myPosition, targetPosition)[0], "valid target")) {
                possibleMoves.add(new ChessMove(myPosition, targetPosition));
            }

        }


        return possibleMoves;
    }
}
