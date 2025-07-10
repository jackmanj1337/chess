package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class KingCalculator extends MoveCalculator {

    @Override
    public ArrayList<ChessMove> calculate(ChessBoard board, ChessPosition start) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
                {1, 1},
                {-1, 1},
                {1, -1},
                {-1, -1},
                {1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}
        };

        singleStepRelativeMoveValidator(board, start, directions, moves);
        return moves;
    }


}
