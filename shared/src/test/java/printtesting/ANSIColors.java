package printtesting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ANSIColors {
    // Chess Unicode characters: white and black pieces
    private static final String[] CHESS_PIECES = {
            "♔", "♕", "♖", "♗", "♘", "♙", // White
            "♚", "♛", "♜", "♝", "♞", "♟"  // Black
    };

    // ANSI color codes for foreground and background
    private static final String[] COLORS = {
            "30", "31", "32", "33", "34", "35", "36", "37", // Regular
            "90", "91", "92", "93", "94", "95", "96", "97"  // Bright
    };

    private static final String[] COLOR_NAMES = {
            "Black", "Red", "Green", "Yellow", "Blue", "Magenta", "Cyan", "White",
            "Bright Black", "Bright Red", "Bright Green", "Bright Yellow",
            "Bright Blue", "Bright Magenta", "Bright Cyan", "Bright White"
    };

    @Test
    public void main() {
        System.out.println("Displaying Unicode Chess Pieces with ANSI Colors\n");

        for (int fg = 0; fg < COLORS.length; fg++) {
            for (int bg = 0; bg < COLORS.length; bg++) {
                String fgColor = COLORS[fg];
                String bgColor = String.valueOf(Integer.parseInt(COLORS[bg]) + 10); // Background codes are FG + 10
                String label = String.format("%-14s on %-14s", COLOR_NAMES[fg], COLOR_NAMES[bg]);
                String ansiPrefix = "\u001B[" + fgColor + ";" + bgColor + "m";
                String ansiReset = "\u001B[0m";

                System.out.print(label + ": ");
                for (String piece : CHESS_PIECES) {
                    System.out.print(ansiPrefix + piece + ansiReset + " ");
                }
                System.out.println();
            }
        }
        assertTrue(true);
    }

}
