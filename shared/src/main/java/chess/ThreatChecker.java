package chess;

import java.util.Objects;

public class ThreatChecker {

    public static final int[][] KNIGHT_DIRECTIONS = new int[][]{
            {2, 1},
            {2, -1},
            {-2, 1},
            {-2, -1},
            {1, 2},
            {-1, 2},
            {1, -2},
            {-1, -2}
    };
    public static final int[][] PAWN_DIRECTIONS = new int[][]{
            {1, 1},
            {1, -1}
    };
    public static final int[][] KING_DIRECTIONS = new int[][]{
            {1, 1},
            {-1, 1},
            {1, -1},
            {-1, -1},
            {1, 0},
            {0, 1},
            {-1, 0},
            {0, -1}
    };
    public static final int[][] STRAIGHTS = new int[][]{
            {1, 0},
            {0, 1},
            {-1, 0},
            {0, -1}
    };
    public static final int[][] DIAGONALS = new int[][]{
            {1, 1},
            {-1, 1},
            {1, -1},
            {-1, -1}
    };

    public static boolean isPositionThreatened(ChessBoard board, ChessPosition position) {
        boolean positionIsThreatened = false;
        ChessGame.TeamColor threateningColor;
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            threateningColor = ChessGame.TeamColor.BLACK;
        } else {
            threateningColor = ChessGame.TeamColor.WHITE;
        }
        //check for knights
        positionIsThreatened =
                isPositionDirectlyThreatened(
                        board,
                        position,
                        KNIGHT_DIRECTIONS,
                        threateningColor,
                        positionIsThreatened,
                        ChessPiece.PieceType.KNIGHT
                );
        if (positionIsThreatened) {
            return positionIsThreatened;
        }
        // Check for pawns
        int directionModifier = (threateningColor == ChessGame.TeamColor.BLACK) ? 1 : -1;

        for (int[] direction : PAWN_DIRECTIONS) {
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
        positionIsThreatened =
                isPositionDirectlyThreatened(
                        board,
                        position,
                        KING_DIRECTIONS,
                        threateningColor,
                        positionIsThreatened,
                        ChessPiece.PieceType.KING
                );
        if (positionIsThreatened) {
            return positionIsThreatened;
        }

        // check straights for rooks and queens
        positionIsThreatened =
                isPositionRemotelyThreatened(
                        board,
                        position,
                        STRAIGHTS,
                        threateningColor,
                        positionIsThreatened,
                        ChessPiece.PieceType.ROOK
                );
        if (positionIsThreatened) {
            return positionIsThreatened;
        }

        // check diagonals for bishops and queens
        positionIsThreatened =
                isPositionRemotelyThreatened(
                        board,
                        position,
                        DIAGONALS,
                        threateningColor,
                        positionIsThreatened,
                        ChessPiece.PieceType.BISHOP
                );
        return positionIsThreatened;
    }

    private static boolean isPositionRemotelyThreatened(
            ChessBoard board,
            ChessPosition position,
            int[][] directions,
            ChessGame.TeamColor threateningColor,
            boolean positionIsthreatened,
            ChessPiece.PieceType threateningPieceType
    ) {
        for (int[] direction : directions) {
            int row = position.getRow();
            int col = position.getColumn();

            while (true) {
                row += direction[0];
                col += direction[1];
                if (!isOnBoard(row, col)) {
                    break;
                }

                ChessPosition checkPos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(checkPos);

                if (piece == null) {
                    continue;
                }

                if (!Objects.equals(piece.getTeamColor(), threateningColor)) {
                    break;
                }

                ChessPiece.PieceType type = piece.getPieceType();
                if (type == threateningPieceType || type == ChessPiece.PieceType.QUEEN) {
                    return true;
                } else {
                    break;
                }
            }
        }

        return false;
    }

    private static boolean isPositionDirectlyThreatened(
            ChessBoard board,
            ChessPosition position,
            int[][] knightDirections,
            ChessGame.TeamColor threateningColor,
            boolean positionIsthreatened,
            ChessPiece.PieceType threateningPieceType
    ) {
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

    private static boolean isOnBoard(int row, int col) {
        return (row >= 1 && row <= 8 && col >= 1 && col <= 8);
    }
}

