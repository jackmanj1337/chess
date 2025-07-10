package chess;

import java.util.Objects;

public class ThreatChecker {
    public static boolean isPositionThreatened(ChessBoard board, ChessPosition position) {
        boolean positionIsThreatened = false;
        ChessGame.TeamColor threateningColor;
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            threateningColor = ChessGame.TeamColor.BLACK;
        } else {
            threateningColor = ChessGame.TeamColor.WHITE;
        }

        //check for knights
        int[][] knightDirections = {
                {2, 1},
                {2, -1},
                {-2, 1},
                {-2, -1},
                {1, 2},
                {-1, 2},
                {1, -2},
                {-1, -2}
        };

        positionIsThreatened = isPositionDirectlyThreatened(board, position, knightDirections, threateningColor, positionIsThreatened, ChessPiece.PieceType.KNIGHT);

        if (positionIsThreatened) {
            return positionIsThreatened;
        }

        // Check for pawns
        int directionModifier = (threateningColor == ChessGame.TeamColor.BLACK) ? 1 : -1;
        int[][] pawnDirections = {
                {1, 1},
                {1, -1}
        };

        for (int[] direction : pawnDirections) {
            int targetRow = position.getRow();
            int targetCol = position.getColumn();

            targetRow += direction[0] * directionModifier;
            targetCol += direction[1];
            if (targetRow >= 1 && targetRow <= 8 && targetCol >= 1 && targetCol <= 8) {
                ChessPosition threateningPosition = new ChessPosition(targetRow, targetCol);
                ChessPiece threateningPiece = board.getPiece(threateningPosition);
                if (threateningPiece != null) {
                    if (Objects.equals(threateningPiece.getTeamColor(), threateningColor) &&
                            Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.PAWN)) {
                        positionIsThreatened = true;
                        return positionIsThreatened;
                    }
                }
            }


        }

        // Check for opposing king
        int[][] kingDirections = {
                {1, 1},
                {-1, 1},
                {1, -1},
                {-1, -1},
                {1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}
        };

        positionIsThreatened = isPositionDirectlyThreatened(board, position, kingDirections, threateningColor, positionIsThreatened, ChessPiece.PieceType.KING);

        if (positionIsThreatened) {
            return positionIsThreatened;
        }

        // check straights for rooks and queens
        int[][] straights = {
                {1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}
        };

        positionIsThreatened = isPositionRemotelyThreatened(board, position, straights, threateningColor, positionIsThreatened, ChessPiece.PieceType.ROOK);
        if (positionIsThreatened) {
            return positionIsThreatened;
        }


        // check diagonals for bishops and queens
        int[][] diagonals = {
                {1, 1},
                {-1, 1},
                {1, -1},
                {-1, -1}
        };

        positionIsThreatened = isPositionRemotelyThreatened(board, position, diagonals, threateningColor, positionIsThreatened, ChessPiece.PieceType.BISHOP);

        return positionIsThreatened;
    }

    private static boolean isPositionRemotelyThreatened(ChessBoard board, ChessPosition position, int[][] diagonals, ChessGame.TeamColor threateningColor, boolean positionIsthreatened, ChessPiece.PieceType threateningPieceType) {
        for (int[] direction : diagonals) {
            int targetRow = position.getRow();
            int targetCol = position.getColumn();
            while (targetRow >= 1 && targetRow <= 8 && targetCol >= 1 && targetCol <= 8) {
                targetRow += direction[0];
                targetCol += direction[1];
                if (targetRow >= 1 && targetRow <= 8 && targetCol >= 1 && targetCol <= 8) {
                    ChessPosition threateningPosition = new ChessPosition(targetRow, targetCol);
                    ChessPiece threateningPiece = board.getPiece(threateningPosition);
                    if (threateningPiece != null) {
                        if (Objects.equals(threateningPiece.getTeamColor(), threateningColor)) {
                            if (Objects.equals(threateningPiece.getPieceType(), threateningPieceType) ||
                                    Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.QUEEN)) {
                                positionIsthreatened = true;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }

                    }
                }
            }
        }
        return positionIsthreatened;
    }

    private static boolean isPositionDirectlyThreatened(ChessBoard board, ChessPosition position, int[][] knightDirections, ChessGame.TeamColor threateningColor, boolean positionIsthreatened, ChessPiece.PieceType threateningPieceType) {
        for (int[] direction : knightDirections) {
            int targetRow = position.getRow();
            int targetCol = position.getColumn();

            targetRow += direction[0];
            targetCol += direction[1];
            if (targetRow >= 1 && targetRow <= 8 && targetCol >= 1 && targetCol <= 8) {
                ChessPosition threateningPosition = new ChessPosition(targetRow, targetCol);
                ChessPiece threateningPiece = board.getPiece(threateningPosition);
                if (threateningPiece != null) {
                    if (Objects.equals(threateningPiece.getTeamColor(), threateningColor) &&
                            Objects.equals(threateningPiece.getPieceType(), threateningPieceType)) {
                        positionIsthreatened = true;
                    }
                }
            }
        }
        return positionIsthreatened;
    }
}
