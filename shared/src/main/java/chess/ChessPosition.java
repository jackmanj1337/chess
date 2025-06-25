package chess;

import java.util.Objects;


/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public String toString() {
        return "[" + getRow() + "," + getColumn() + "]";
    }

    @Override
    public boolean equals(Object comparisonTarget) {
        if (comparisonTarget == this) {
            return true;
        }
        if (!(comparisonTarget instanceof ChessMove)) {
            return false;
        }
        if (((ChessPosition) comparisonTarget).getRow() == this.getRow() &&
                ((ChessPosition) comparisonTarget).getColumn() == this.getColumn()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
