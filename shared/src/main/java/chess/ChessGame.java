package chess;

import chess.MoveCalculator.MoveCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor activeTeam = TeamColor.WHITE;
    private ChessBoard board;


    @SuppressWarnings("unchecked")
    private ArrayList<ChessMove>[][] allValidMoves = new ArrayList[8][8];

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return activeTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        activeTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public ChessPosition findKing(TeamColor color) {
        ChessPiece king = new ChessPiece(color, ChessPiece.PieceType.KING);
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition positionToCheck = new ChessPosition(i, j);
                if (king.equals(board.getPiece(positionToCheck))) {
                    return positionToCheck;
                }
            }
        }
        return new ChessPosition(0, 0); // no king of the appropriate color found;
    }

    public boolean IsPositionThreatened(ChessPosition position) {
        boolean positionIsthreatened = false;
        TeamColor threateningColor;
        if (board.getPiece(position).getTeamColor() == TeamColor.WHITE) {
            threateningColor = TeamColor.BLACK;
        } else {
            threateningColor = TeamColor.WHITE;
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

        for (int[] direction : knightDirections) {
            int tartgetRow = position.getRow();
            int targetCol = position.getColumn();

            tartgetRow += direction[0];
            targetCol += direction[1];
            ChessPosition threateningPosition = new ChessPosition(tartgetRow, targetCol);
            ChessPiece threateningPiece = board.getPiece(threateningPosition);
            if (Objects.equals(threateningPiece.getTeamColor(), threateningColor) &&
                    Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.KNIGHT)) {
                positionIsthreatened = true;
                return positionIsthreatened;
            }


        }

        // Check for pawns
        int directionModifier = (threateningColor == TeamColor.BLACK) ? 1 : -1;
        int[][] pawnDirections = {
                {1, 1},
                {1, -1}
        };

        for (int[] direction : pawnDirections) {
            int tartgetRow = position.getRow();
            int targetCol = position.getColumn();

            tartgetRow += direction[0] * directionModifier;
            targetCol += direction[1];
            ChessPosition threateningPosition = new ChessPosition(tartgetRow, targetCol);
            ChessPiece threateningPiece = board.getPiece(threateningPosition);
            if (Objects.equals(threateningPiece.getTeamColor(), threateningColor) &&
                    Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.PAWN)) {
                positionIsthreatened = true;
                return positionIsthreatened;
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

        for (int[] direction : kingDirections) {
            int tartgetRow = position.getRow();
            int targetCol = position.getColumn();

            tartgetRow += direction[0];
            targetCol += direction[1];
            ChessPosition threateningPosition = new ChessPosition(tartgetRow, targetCol);
            ChessPiece threateningPiece = board.getPiece(threateningPosition);
            if (Objects.equals(threateningPiece.getTeamColor(), threateningColor) &&
                    Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.KING)) {
                positionIsthreatened = true;
                return positionIsthreatened;
            }
        }

        // check straights for rooks and queens
        int[][] straights = {
                {1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}
        };

        for (int[] direction : straights) {
            int tartgetRow = position.getRow();
            int targetCol = position.getColumn();
            while (true) {
                tartgetRow += direction[0];
                targetCol += direction[1];
                ChessPosition threateningPosition = new ChessPosition(tartgetRow, targetCol);
                ChessPiece threateningPiece = board.getPiece(threateningPosition);
                if (Objects.equals(threateningPiece.getTeamColor(), null)) {
                    if (Objects.equals(threateningPiece.getTeamColor(), threateningColor)) {
                        if (Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.ROOK) ||
                                Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.QUEEN)) {
                            positionIsthreatened = true;
                            return positionIsthreatened;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }

            }
        }

        // check diagonals for bishops and queens
        int[][] diagonals = {
                {1, 1},
                {-1, 1},
                {1, -1},
                {-1, -1}
        };

        for (int[] direction : diagonals) {
            int tartgetRow = position.getRow();
            int targetCol = position.getColumn();
            while (true) {
                tartgetRow += direction[0];
                targetCol += direction[1];
                ChessPosition threateningPosition = new ChessPosition(tartgetRow, targetCol);
                ChessPiece threateningPiece = board.getPiece(threateningPosition);
                if (Objects.equals(threateningPiece.getTeamColor(), null)) {
                    if (Objects.equals(threateningPiece.getTeamColor(), threateningColor)) {
                        if (Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.BISHOP) ||
                                Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.QUEEN)) {
                            positionIsthreatened = true;
                            return positionIsthreatened;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }

            }
        }


        return positionIsthreatened;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> moves;
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("It is not that team's turn");
        }
        boolean moveIsValid = false;
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        for (ChessMove validMove : validMoves) {
            if (move.equals(validMove)) {
                moveIsValid = true;
                break;
            }
        }
        if (!moveIsValid) {
            throw new InvalidMoveException("No legal moves exist for that piece");
        }

        int startRow = move.getStartPosition().getRow() - 1;
        int startCol = move.getStartPosition().getColumn() - 1;
        TeamColor activeColor = board.getPiece(move.getStartPosition()).getTeamColor();
        ChessPiece.PieceType pieceToAdd;
        if (move.getPromotionPiece() == null) {
            pieceToAdd = board.getPiece(move.getStartPosition()).getPieceType();
        } else {
            pieceToAdd = move.getPromotionPiece();
        }
        board.addPiece(move.getEndPosition(), new ChessPiece(activeColor, pieceToAdd));

        board.removePiece(move.getStartPosition());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    /*
    two options on how to do this
    1. if in check, run through every pseudo-legal move and search for one where the king is not in check
    2. something much more complicated where you check all vectors of attack and
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                this.board.addPiece(new ChessPosition(i, j), board.getPiece(new ChessPosition(i, j)));
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
