package chess.MoveCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class PawnCalculator extends MoveCalculator {

    @Override
    public ArrayList<ChessMove> calculate(ChessBoard board, ChessPosition start) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int directionModifier = (board.getPiece(start).getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;

        int targetRow;
        int targetCol;
        ChessPosition target;
        ValidationResults results;

        //Check imediately ahead
        targetRow = start.getRow() + directionModifier;
        targetCol = start.getColumn();
        target = new ChessPosition(targetRow, targetCol);
        results = MoveValidator(board, start, target);
        if (results.moveOnBoard == true){
            if (results.squareEmpty == true){
                AddMove(moves, start, target, results);
                if (start.getRow() == ((directionModifier == 1) ? 2 : 7)){
                    targetRow += directionModifier;
                    target = new ChessPosition(targetRow, targetCol);
                    results = MoveValidator(board, start, target);
                    if (results.moveOnBoard == true && results.squareEmpty == true){
                        AddMove(moves, start, target, results);
                    }
                }

            }
        }

        //check capturing
        for (int leftorright = -1; leftorright < 2; leftorright += 2){
            targetRow = start.getRow() + directionModifier;
            targetCol = start.getColumn() + leftorright;
            target = new ChessPosition(targetRow, targetCol);
            results = MoveValidator(board, start, target);
            if (results.capture){
                AddMove(moves, start, target, results);
            }
        }



        return moves;
    }
}
