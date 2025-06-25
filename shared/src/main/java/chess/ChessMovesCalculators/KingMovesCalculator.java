package chess.ChessMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

//TODO add support for castling
public class KingMovesCalculator extends ChessMovesCalculator {
    @Override
    public Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        int[][] directions = {
                {1, 0},  // up
                {1, 1},  // up right
                {0, 1}, // right[
                {-1, 1},  // down right
                {-1, 0},  // down
                {-1, -1}, //down left
                {0, -1}, // left
                {1, -1}, //left up
        };
        for (int[] direction : directions) {
            int targetRow = myPosition.getRow() + direction[0];
            int targetCol = myPosition.getColumn() + direction[1];
            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            if (Objects.equals(IsValidMove(board, myPosition, targetPosition)[0], TargetSquare.VALID_TARGET)) {
                possibleMoves.add(new ChessMove(myPosition, targetPosition));
            }

        }

        return possibleMoves;
    }
}
