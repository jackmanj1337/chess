package chess.ChessMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends ChessMovesCalculator {
    @Override
    public Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition myPosition) {
        return validateDiagonals(board, myPosition);
    }


}
