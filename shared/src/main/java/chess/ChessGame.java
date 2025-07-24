package chess;

import java.util.*;

import chess.ThreatChecker;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor activeTeam;
    private ChessBoard board;
    private boolean whiteCanQueenSideCastle = true;
    private boolean whiteCanKingSideCastle = true;
    private boolean blackCanQueenSideCastle = true;
    private boolean blackCanKingSideCastle = true;
    private ChessPosition enPassantablePawn = null;
    private ChessPosition enPassantDestination = null;
    private String winner = null;

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

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
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

    private Collection<ChessMove> checkCastle(ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        if (Objects.equals(position, new ChessPosition(1, 5)) && !isInCheck(TeamColor.WHITE)) {
            if (whiteCanKingSideCastle &&
                    board.getPiece(new ChessPosition(1, 6)) == null &&
                    board.getPiece(new ChessPosition(1, 7)) == null) {
                ChessBoard testingGrounds = new ChessBoard(board);
                ChessMove move = new ChessMove(position, new ChessPosition(1, 6), null);
                makeTestMove(testingGrounds, move);
                if (!ThreatChecker.isPositionThreatened(testingGrounds, new ChessPosition(1, 6))) {
                    move = new ChessMove(new ChessPosition(1, 6), new ChessPosition(1, 7), null);
                    makeTestMove(testingGrounds, move);
                    if (!ThreatChecker.isPositionThreatened(testingGrounds, new ChessPosition(1, 7))) {
                        moves.add(new ChessMove(position, new ChessPosition(1, 7), null));
                    }
                }
            }
            if (whiteCanQueenSideCastle &&
                    board.getPiece(new ChessPosition(1, 2)) == null &&
                    board.getPiece(new ChessPosition(1, 3)) == null &&
                    board.getPiece(new ChessPosition(1, 4)) == null) {
                ChessBoard testingGrounds = new ChessBoard(board);
                ChessMove move = new ChessMove(position, new ChessPosition(1, 4), null);
                makeTestMove(testingGrounds, move);
                if (!ThreatChecker.isPositionThreatened(testingGrounds, new ChessPosition(1, 4))) {
                    move = new ChessMove(new ChessPosition(1, 4), new ChessPosition(1, 3), null);
                    makeTestMove(testingGrounds, move);
                    if (!ThreatChecker.isPositionThreatened(testingGrounds, new ChessPosition(1, 3))) {
                        moves.add(new ChessMove(position, new ChessPosition(1, 3), null));
                    }
                }
            }
        }
        if (Objects.equals(position, new ChessPosition(8, 5)) && !isInCheck(TeamColor.BLACK)) {
            if (blackCanKingSideCastle &&
                    board.getPiece(new ChessPosition(8, 6)) == null &&
                    board.getPiece(new ChessPosition(8, 7)) == null) {
                ChessBoard testingGrounds = new ChessBoard(board);
                ChessMove move = new ChessMove(position, new ChessPosition(8, 6), null);
                makeTestMove(testingGrounds, move);
                if (!ThreatChecker.isPositionThreatened(testingGrounds, new ChessPosition(8, 6))) {
                    move = new ChessMove(new ChessPosition(8, 6), new ChessPosition(8, 7), null);
                    makeTestMove(testingGrounds, move);
                    if (!ThreatChecker.isPositionThreatened(testingGrounds, new ChessPosition(8, 7))) {
                        moves.add(new ChessMove(position, new ChessPosition(8, 7), null));
                    }
                }
            }
            if (blackCanQueenSideCastle &&
                    board.getPiece(new ChessPosition(8, 2)) == null &&
                    board.getPiece(new ChessPosition(8, 3)) == null &&
                    board.getPiece(new ChessPosition(8, 4)) == null) {
                ChessBoard testingGrounds = new ChessBoard(board);
                ChessMove move = new ChessMove(position, new ChessPosition(8, 4), null);
                makeTestMove(testingGrounds, move);
                if (!ThreatChecker.isPositionThreatened(testingGrounds, new ChessPosition(8, 4))) {
                    move = new ChessMove(new ChessPosition(8, 4), new ChessPosition(8, 3), null);
                    makeTestMove(testingGrounds, move);
                    if (!ThreatChecker.isPositionThreatened(testingGrounds, new ChessPosition(8, 3))) {
                        moves.add(new ChessMove(position, new ChessPosition(8, 3), null));
                    }
                }
            }
        }


        return moves;
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
        moves.addAll(checkCastle(startPosition));

        if (enPassantablePawn != null) {
            if (Objects.equals(board.getPiece(startPosition).getPieceType(), ChessPiece.PieceType.PAWN) &&
                    (startPosition.getRow() == enPassantablePawn.getRow()) &&
                    (board.getPiece(startPosition).getTeamColor() != board.getPiece(enPassantablePawn).getTeamColor())
                    && (startPosition.getColumn() + 1 == enPassantablePawn.getColumn()
                    || startPosition.getColumn() - 1 == enPassantablePawn.getColumn())) {
                if (board.getPiece(startPosition).getTeamColor() == TeamColor.WHITE) {
                    moves.add(
                            new ChessMove(startPosition,
                                    new ChessPosition(6, enPassantablePawn.getColumn()), null));
                } else {
                    moves.add(
                            new ChessMove(startPosition,
                                    new ChessPosition(3, enPassantablePawn.getColumn()), null));
                }
            }
        }


        Iterator<ChessMove> iter = moves.iterator();
        while (iter.hasNext()) {
            ChessMove move = iter.next();
            ChessBoard testingGrounds = new ChessBoard(board);
            makeTestMove(testingGrounds, move);
            ChessPosition activeKing = findKing(defending, testingGrounds);
            if (ThreatChecker.isPositionThreatened(testingGrounds, activeKing)) {
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
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("You cant move a piece that doesn't exist");
        }
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

        governCastle(move);

        //Check if we did EnPassant
        governEnPassant(move);


        TeamColor nextPlayer;
        if (getTeamTurn() == TeamColor.WHITE) {
            nextPlayer = TeamColor.BLACK;
        } else {
            nextPlayer = TeamColor.WHITE;
        }
        setTeamTurn(nextPlayer);
    }

    private void governEnPassant(ChessMove move) {
        if (Objects.equals(enPassantDestination, move.getEndPosition()) &&
                board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
            board.removePiece(enPassantablePawn);
        }

        //check if next player can EnPassant
        if ((move.getStartPosition().getRow() == 2) &&
                (move.getEndPosition().getRow() == 4) &&
                (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN)) {
            enPassantablePawn = move.getEndPosition();
            enPassantDestination = new ChessPosition(3, enPassantablePawn.getColumn());
        } else if ((move.getStartPosition().getRow() == 7) &&
                (move.getEndPosition().getRow() == 5) &&
                (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN)) {
            enPassantablePawn = move.getEndPosition();
            enPassantDestination = new ChessPosition(6, enPassantablePawn.getColumn());
        } else {
            enPassantablePawn = null;
            enPassantDestination = null;
        }
    }

    private void governCastle(ChessMove move) {
        ChessMove whiteQueenSideCastle =
                new ChessMove(new ChessPosition(1, 5), new ChessPosition(1, 3), null);
        ChessMove whiteKingSideCastle =
                new ChessMove(new ChessPosition(1, 5), new ChessPosition(1, 7), null);
        ChessMove blackQueenSideCastle =
                new ChessMove(new ChessPosition(8, 5), new ChessPosition(8, 3), null);
        ChessMove blackKingSideCastle =
                new ChessMove(new ChessPosition(8, 5), new ChessPosition(8, 7), null);

        if (Objects.equals(move, whiteQueenSideCastle)) {
            board.removePiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.KING));
            board.removePiece(new ChessPosition(1, 1));
            board.addPiece(new ChessPosition(1, 4), new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        } else if (Objects.equals(move, whiteKingSideCastle)) {
            board.removePiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.KING));
            board.removePiece(new ChessPosition(1, 8));
            board.addPiece(new ChessPosition(1, 6), new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        } else if (Objects.equals(move, blackQueenSideCastle)) {
            board.removePiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.KING));
            board.removePiece(new ChessPosition(8, 1));
            board.addPiece(new ChessPosition(8, 4), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        } else if (Objects.equals(move, blackKingSideCastle)) {
            board.removePiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.KING));
            board.removePiece(new ChessPosition(8, 8));
            board.addPiece(new ChessPosition(8, 6), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        } else {
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

        if (Objects.equals(move.getStartPosition(), new ChessPosition(1, 5))) {
            whiteCanQueenSideCastle = false;
            whiteCanKingSideCastle = false;
        }
        if (Objects.equals(move.getStartPosition(), new ChessPosition(8, 5))) {
            blackCanKingSideCastle = false;
            blackCanQueenSideCastle = false;
        }
        if (Objects.equals(move.getStartPosition(), new ChessPosition(1, 1))) {
            whiteCanQueenSideCastle = false;
        }
        if (Objects.equals(move.getStartPosition(), new ChessPosition(1, 8))) {
            whiteCanKingSideCastle = false;
        }
        if (Objects.equals(move.getStartPosition(), new ChessPosition(8, 1))) {
            blackCanQueenSideCastle = false;
        }
        if (Objects.equals(move.getStartPosition(), new ChessPosition(8, 8))) {
            blackCanKingSideCastle = false;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return ThreatChecker.isPositionThreatened(board, findKing(teamColor, board));
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
        return validMoveCount ==
                chessGame.validMoveCount &&
                activeTeam == chessGame.activeTeam &&
                Objects.equals(getBoard(), chessGame.getBoard()) &&
                Objects.deepEquals(allValidMoves, chessGame.allValidMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeTeam, getBoard(), Arrays.deepHashCode(allValidMoves), validMoveCount);
    }
}
