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
    public boolean equals(Object o) {
        if (!(o instanceof ChessPosition that)) {
            return false;
        }
        return getRow() == that.getRow() && col == that.col;
    }

    public String toDisplayString() {
        if (col < 1 || col > 8 || row < 1 || row > 8) {
            return "invalid";
        }
        char file = (char) ('a' + col - 1);
        return String.valueOf(file) + row;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getRow(), col);
    }

    @Override
    public String toString() {
        return "[" + row + ", " + col + "]";
    }
}
