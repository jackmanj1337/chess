package chess;

import chess.movecalculator.*;

import java.util.Collection;
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

    public ChessPiece(ChessPiece other) {
        this.pieceColor = other.getTeamColor();
        this.type = other.getPieceType();
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
        MoveCalculator calc = switch (board.getPiece(myPosition).getPieceType()) {
            case KING -> new KingCalculator();
            case QUEEN -> new QueenCalculator();
            case BISHOP -> new BishopCalculator();
            case KNIGHT -> new KnightCalculator();
            case ROOK -> new RookCalculator();
            case PAWN -> new PawnCalculator();
        };

        return calc.calculate(board, myPosition);
    }

    @Override
    public String toString() {
        return switch (getPieceType()) {
            case PieceType.KING -> (pieceColor == ChessGame.TeamColor.WHITE) ? "K" : "k";
            case PieceType.QUEEN -> (pieceColor == ChessGame.TeamColor.WHITE) ? "Q" : "q";
            case PieceType.BISHOP -> (pieceColor == ChessGame.TeamColor.WHITE) ? "B" : "b";
            case PieceType.KNIGHT -> (pieceColor == ChessGame.TeamColor.WHITE) ? "N" : "n";
            case PieceType.ROOK -> (pieceColor == ChessGame.TeamColor.WHITE) ? "R" : "r";
            case PieceType.PAWN -> (pieceColor == ChessGame.TeamColor.WHITE) ? "P" : "p";
        };
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}

