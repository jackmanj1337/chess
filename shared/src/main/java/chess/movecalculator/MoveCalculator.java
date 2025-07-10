package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Objects;

public abstract class MoveCalculator {
    public abstract ArrayList<ChessMove> calculate(ChessBoard board, ChessPosition start);

    public static class ValidationResults {
        public final boolean moveOnBoard;
        public final boolean squareEmpty;
        public final boolean capture;
        public final boolean promotion;

        ValidationResults(boolean moveOnBoard, boolean squareEmpty, boolean capture, boolean promotion) {
            this.moveOnBoard = moveOnBoard;
            this.squareEmpty = squareEmpty;
            this.capture = capture;
            this.promotion = promotion;
        }
    }


    protected ValidationResults moveValidator(ChessBoard board, ChessPosition start, ChessPosition end) {
        boolean onBoard = false;
        boolean squareEmpty = false;
        boolean capture = false;
        boolean promotion = false;

        if (end.getRow() > 8 || end.getRow() < 1 || end.getColumn() > 8 || end.getColumn() < 1) {
            onBoard = false;
            return new ValidationResults(onBoard, squareEmpty, capture, promotion);
        } else {
            onBoard = true;
        }
        if (board.getPiece(end) == null) {
            squareEmpty = true;
        } else {
            squareEmpty = false;
            if (Objects.equals(board.getPiece(start).getTeamColor(), board.getPiece(end).getTeamColor())) {
                capture = false;
            } else {
                capture = true;
            }
        }

        if (board.getPiece(start).getPieceType() == ChessPiece.PieceType.PAWN) {
            if (end.getRow() == 8 || end.getRow() == 1) {
                promotion = true;
            }
        }


        return new ValidationResults(onBoard, squareEmpty, capture, promotion);
    }

    protected void addMove(ArrayList<ChessMove> list, ChessPosition start, ChessPosition end, ValidationResults results) {
        ChessPiece.PieceType[] possiblePromotions = {
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.QUEEN
        };

        if (results.promotion) {
            for (ChessPiece.PieceType promotion : possiblePromotions) {
                list.add(new ChessMove(start, end, promotion));
            }
        } else {
            list.add(new ChessMove(start, end, null));
        }
    }

    protected ArrayList<ChessMove> validateStraights(ChessBoard board, ChessPosition start) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
                {1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}
        };

        continuousDirectionalMoveValidator(board, start, directions, moves);


        return moves;
    }

    protected ArrayList<ChessMove> validateDiagonals(ChessBoard board, ChessPosition start) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
                {1, 1},
                {-1, 1},
                {1, -1},
                {-1, -1}
        };

        continuousDirectionalMoveValidator(board, start, directions, moves);


        return moves;
    }

    private void continuousDirectionalMoveValidator(ChessBoard board, ChessPosition start, int[][] directions, ArrayList<ChessMove> moves) {
        for (int[] direction : directions) {
            int tartgetRow = start.getRow();
            int targetCol = start.getColumn();
            while (true) {
                tartgetRow += direction[0];
                targetCol += direction[1];
                ChessPosition targetPosistion = new ChessPosition(tartgetRow, targetCol);
                ValidationResults results = moveValidator(board, start, targetPosistion);
                if (results.moveOnBoard == false) {
                    break;
                }
                if (results.squareEmpty) {
                    addMove(moves, start, targetPosistion, results);
                } else {
                    if (results.capture) {
                        addMove(moves, start, targetPosistion, results);
                    }
                    break;
                }

            }
        }
    }

    protected void singleStepRelativeMoveValidator(ChessBoard board, ChessPosition start, int[][] directions, ArrayList<ChessMove> moves) {
        for (int[] direction : directions) {
            int tartgetRow = start.getRow();
            int targetCol = start.getColumn();

            tartgetRow += direction[0];
            targetCol += direction[1];
            ChessPosition targetPosistion = new ChessPosition(tartgetRow, targetCol);
            ValidationResults results = moveValidator(board, start, targetPosistion);
            if (results.moveOnBoard == true) {
                if (results.squareEmpty) {
                    addMove(moves, start, targetPosistion, results);
                } else {
                    if (results.capture) {
                        addMove(moves, start, targetPosistion, results);
                    }
                }
            }
        }
    }


}
