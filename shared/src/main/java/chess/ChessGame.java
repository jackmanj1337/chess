package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor activeTeam;
    private ChessBoard board;


    @SuppressWarnings("unchecked")
    private ArrayList<ChessMove>[][] allValidMoves = new ArrayList[8][8];
    private int validMoveCount;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                allValidMoves[i][j] = new ArrayList<>();
            }
        }
        setTeamTurn(TeamColor.WHITE);
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
        //validMoveCount = calculateAllValidMoves(team);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public ChessPosition findKing(TeamColor color, ChessBoard board) {
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

    public boolean isPositionThreatened(ChessBoard board, ChessPosition position) {
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
            int targetRow = position.getRow();
            int targetCol = position.getColumn();

            targetRow += direction[0];
            targetCol += direction[1];
            if (targetRow >= 1 && targetRow <= 8 && targetCol >= 1 && targetCol <= 8) {
                ChessPosition threateningPosition = new ChessPosition(targetRow, targetCol);
                ChessPiece threateningPiece = board.getPiece(threateningPosition);
                if (threateningPiece != null) {
                    if (Objects.equals(threateningPiece.getTeamColor(), threateningColor) &&
                            Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.KNIGHT)) {
                        positionIsthreatened = true;
                        return positionIsthreatened;
                    }
                }
            }


        }

        // Check for pawns
        int directionModifier = (threateningColor == TeamColor.BLACK) ? 1 : -1;
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
                        positionIsthreatened = true;
                        return positionIsthreatened;
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

        for (int[] direction : kingDirections) {
            int targetRow = position.getRow();
            int targetCol = position.getColumn();

            targetRow += direction[0];
            targetCol += direction[1];
            if (targetRow >= 1 && targetRow <= 8 && targetCol >= 1 && targetCol <= 8) {
                ChessPosition threateningPosition = new ChessPosition(targetRow, targetCol);
                ChessPiece threateningPiece = board.getPiece(threateningPosition);
                if (threateningPiece != null) {
                    if (Objects.equals(threateningPiece.getTeamColor(), threateningColor) &&
                            Objects.equals(threateningPiece.getPieceType(), ChessPiece.PieceType.KING)) {
                        positionIsthreatened = true;
                        return positionIsthreatened;
                    }
                }
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
        }

        // check diagonals for bishops and queens
        int[][] diagonals = {
                {1, 1},
                {-1, 1},
                {1, -1},
                {-1, -1}
        };

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
        }


        return positionIsthreatened;
    }


    private void makeTestMove(ChessBoard testingBoard, ChessMove move) {
        int startRow = move.getStartPosition().getRow() - 1;
        int startCol = move.getStartPosition().getColumn() - 1;
        TeamColor activeColor = testingBoard.getPiece(move.getStartPosition()).getTeamColor();
        ChessPiece.PieceType pieceToAdd;
        if (move.getPromotionPiece() == null) {
            pieceToAdd = testingBoard.getPiece(move.getStartPosition()).getPieceType();
        } else {
            pieceToAdd = move.getPromotionPiece();
        }
        testingBoard.addPiece(move.getEndPosition(), new ChessPiece(activeColor, pieceToAdd));

        testingBoard.removePiece(move.getStartPosition());
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        TeamColor defending = board.getPiece(startPosition).getTeamColor();
        Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Iterator<ChessMove> iter = moves.iterator();
        while (iter.hasNext()) {
            ChessMove move = iter.next();
            ChessBoard testingGrounds = new ChessBoard(board);
            makeTestMove(testingGrounds, move);
            ChessPosition activeKing = findKing(defending, testingGrounds);
            if (isPositionThreatened(testingGrounds, activeKing)) {
                iter.remove();
            }
        }


        return moves;
    }

    private int calculateAllValidMoves(TeamColor team) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                allValidMoves[i][j].clear();
            }
        }
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == team) {
                    allValidMoves[i][j].addAll(validMoves(position));
                    count += allValidMoves[i][j].size();
                }
            }
        }
        return count;
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

        TeamColor nextPlayer;
        if (getTeamTurn() == TeamColor.WHITE) {
            nextPlayer = TeamColor.BLACK;
        } else {
            nextPlayer = TeamColor.WHITE;
        }
        setTeamTurn(nextPlayer);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isPositionThreatened(board, findKing(teamColor, board));
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
        boolean check = isInCheck(teamColor);
        int moves = calculateAllValidMoves(teamColor);
        return (check) && (moves == 0);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return (!isInCheck(teamColor) && (calculateAllValidMoves(teamColor) == 0));
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = new ChessBoard();
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return validMoveCount == chessGame.validMoveCount && activeTeam == chessGame.activeTeam && Objects.equals(getBoard(), chessGame.getBoard()) && Objects.deepEquals(allValidMoves, chessGame.allValidMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeTeam, getBoard(), Arrays.deepHashCode(allValidMoves), validMoveCount);
    }
}
