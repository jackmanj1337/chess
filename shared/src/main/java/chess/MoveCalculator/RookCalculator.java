package chess.MoveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.MoveCalculator.MoveCalculator;

import java.util.ArrayList;

public class RookCalculator extends MoveCalculator {

    @Override
    public ArrayList<ChessMove> calculate(ChessBoard board, ChessPosition start) {
        return validateStraights(board, start);
    }
}
