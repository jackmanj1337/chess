package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class KnightCalculator extends MoveCalculator {

    @Override
    public ArrayList<ChessMove> calculate(ChessBoard board, ChessPosition start) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
                {2, 1},
                {2, -1},
                {-2, 1},
                {-2, -1},
                {1, 2},
                {-1, 2},
                {1, -2},
                {-1, -2}
        };

        singleStepRelativeMoveValidator(board, start, directions, moves);
        return moves;
    }
}
