package chess.ChessMovesCalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

// TODO add en passant capture
public class PawnMovesCalculator extends ChessMovesCalculator {
    private void addMove(ArrayList<ChessMove> moves, ChessPosition from, ChessPosition to, Object[] results) {

        ChessPiece.PieceType[] possiblePromotions = {
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.ROOK
        };


        if (Objects.equals(results[2], Promotion.PROMOTION)) {
            for (ChessPiece.PieceType promotion : possiblePromotions) {
                moves.add(new ChessMove(from, to, promotion));
            }
        } else {
            moves.add(new ChessMove(from, to));
        }
    }

    @Override
    public Collection<ChessMove> getPossibleMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int directionModifier = 1;
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            directionModifier = -1;
        }

        // variable declarations
        int targetRow;
        int targetCol;
        ChessPosition targetPosition;
        Object[] results;

        //check directly in front of pawn
        targetRow = myPosition.getRow() + (directionModifier * 1);
        targetCol = myPosition.getColumn();
        targetPosition = new ChessPosition(targetRow, targetCol);
        results = IsValidMove(board, myPosition, targetPosition);
        if (Objects.equals(results[0], TargetSquare.VALID_TARGET) &&
                Objects.equals(results[1], Capture.NO_CAPTURE)) {
            addMove(possibleMoves, myPosition, targetPosition, results);
        }

        //check double move
        if (board.getPiece(myPosition).getDistanceMoved() == 0 && board.getPiece(targetPosition) == null && myPosition.getRow() == ((directionModifier == 1) ? 2 : 7)) {
            targetRow = myPosition.getRow() + (directionModifier * 2);
            targetCol = myPosition.getColumn();
            targetPosition = new ChessPosition(targetRow, targetCol);
            results = IsValidMove(board, myPosition, targetPosition);
            if (Objects.equals(results[0], TargetSquare.VALID_TARGET) &&
                    Objects.equals(results[1], Capture.NO_CAPTURE)) {
                addMove(possibleMoves, myPosition, targetPosition, results);
            }
        }


        //check diagonals
        int[][] directions = {
                {1, -1},  // up left
                {1, 1}  // up right
        };
        for (int[] direction : directions) {
            targetRow = myPosition.getRow() + (directionModifier * direction[0]);
            targetCol = myPosition.getColumn() + direction[1];
            targetPosition = new ChessPosition(targetRow, targetCol);
            results = IsValidMove(board, myPosition, targetPosition);
            if (Objects.equals(results[0], TargetSquare.VALID_TARGET) &&
                    Objects.equals(results[1], Capture.CAPTURE)) {
                addMove(possibleMoves, myPosition, targetPosition, results);
            }
        }


        return possibleMoves;
    }
}
