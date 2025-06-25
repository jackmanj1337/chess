package chess;

import chess.ChessMovesCalculators.*;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessMovesCalculator calculator = switch (getPieceType()) {
            case KING -> new KingMovesCalculator();
            case QUEEN -> new QueenMovesCalculator();
            case BISHOP -> new BishopMovesCalculator();
            case ROOK -> new RookMovesCalculator();
            case KNIGHT -> new KnightMovesCalculator();
            case PAWN -> new PawnMovesCalculator();
        };

        return calculator.getPossibleMoves(board, myPosition);
    }

    @Override
    public boolean equals(Object comparisonTarget) {
        if (comparisonTarget == this) {
            return true;
        }
        if (!(comparisonTarget instanceof ChessMove)) {
            return false;
        }
        if (((ChessPiece) comparisonTarget).getTeamColor() == this.getTeamColor() &&
                ((ChessPiece) comparisonTarget).getPieceType() == this.getPieceType()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
