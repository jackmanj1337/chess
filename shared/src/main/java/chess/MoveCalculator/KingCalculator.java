package chess.MoveCalculator;

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

        for (int[] direction : directions){
            int tartgetRow = start.getRow();
            int targetCol = start.getColumn();

                tartgetRow += direction[0];
                targetCol += direction[1];
                ChessPosition targetPosistion = new ChessPosition(tartgetRow, targetCol);
                ValidationResults results = MoveValidator(board, start, targetPosistion);
                if (results.moveOnBoard == true) {
                    if (results.squareEmpty){
                        AddMove(moves, start, targetPosistion, results);
                    } else{
                        if (results.capture){
                            AddMove(moves, start, targetPosistion, results);
                        }
                    }
                }



        }


        return moves;
    }
}
