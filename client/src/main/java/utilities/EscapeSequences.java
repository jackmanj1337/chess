package utilities;

public class EscapeSequences {

    private static final String UNICODE_ESCAPE = "\u001b";
    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    private static final String SET_BG_COLOR = UNICODE_ESCAPE + "[48;5;";

    public static final String ERASE_SCREEN = UNICODE_ESCAPE + "[H" + UNICODE_ESCAPE + "[2J";
    public static final String ERASE_LINE = UNICODE_ESCAPE + "[2K";

    public static final String SET_TEXT_BOLD = UNICODE_ESCAPE + "[1m";
    public static final String SET_TEXT_FAINT = UNICODE_ESCAPE + "[2m";
    public static final String RESET_TEXT_BOLD_FAINT = UNICODE_ESCAPE + "[22m";
    public static final String SET_TEXT_ITALIC = UNICODE_ESCAPE + "[3m";
    public static final String RESET_TEXT_ITALIC = UNICODE_ESCAPE + "[23m";
    public static final String SET_TEXT_UNDERLINE = UNICODE_ESCAPE + "[4m";
    public static final String RESET_TEXT_UNDERLINE = UNICODE_ESCAPE + "[24m";
    public static final String SET_TEXT_BLINKING = UNICODE_ESCAPE + "[5m";
    public static final String RESET_TEXT_BLINKING = UNICODE_ESCAPE + "[25m";

    public static final String RESET_TEXT_COLOR = UNICODE_ESCAPE + "[39m";
    public static final String RESET_BG_COLOR = UNICODE_ESCAPE + "[49m";


    public static final String SET_TEXT_BLACK = SET_TEXT_COLOR + "0m";
    public static final String SET_TEXT_RED = SET_TEXT_COLOR + "1m";
    public static final String SET_TEXT_GREEN = SET_TEXT_COLOR + "2m";
    public static final String SET_TEXT_YELLOW = SET_TEXT_COLOR + "3m";
    public static final String SET_TEXT_BLUE = SET_TEXT_COLOR + "4m";
    public static final String SET_TEXT_MAGENTA = SET_TEXT_COLOR + "5m";
    public static final String SET_TEXT_CYAN = SET_TEXT_COLOR + "6m";
    public static final String SET_TEXT_WHITE = SET_TEXT_COLOR + "7m";

    public static final String SET_TEXT_BRIGHT_BLACK = SET_TEXT_COLOR + "8m";   // Gray
    public static final String SET_TEXT_BRIGHT_RED = SET_TEXT_COLOR + "9m";
    public static final String SET_TEXT_BRIGHT_GREEN = SET_TEXT_COLOR + "10m";
    public static final String SET_TEXT_BRIGHT_YELLOW = SET_TEXT_COLOR + "11m";
    public static final String SET_TEXT_BRIGHT_BLUE = SET_TEXT_COLOR + "12m";
    public static final String SET_TEXT_BRIGHT_MAGENTA = SET_TEXT_COLOR + "13m";
    public static final String SET_TEXT_BRIGHT_CYAN = SET_TEXT_COLOR + "14m";
    public static final String SET_TEXT_BRIGHT_WHITE = SET_TEXT_COLOR + "15m";

    public static final String SET_BG_BLACK = SET_BG_COLOR + "0m";
    public static final String SET_BG_RED = SET_BG_COLOR + "1m";
    public static final String SET_BG_GREEN = SET_BG_COLOR + "2m";
    public static final String SET_BG_YELLOW = SET_BG_COLOR + "3m";
    public static final String SET_BG_BLUE = SET_BG_COLOR + "4m";
    public static final String SET_BG_MAGENTA = SET_BG_COLOR + "5m";
    public static final String SET_BG_CYAN = SET_BG_COLOR + "6m";
    public static final String SET_BG_WHITE = SET_BG_COLOR + "7m";

    public static final String SET_BG_BRIGHT_BLACK = SET_BG_COLOR + "8m";   // Gray
    public static final String SET_BG_BRIGHT_RED = SET_BG_COLOR + "9m";
    public static final String SET_BG_BRIGHT_GREEN = SET_BG_COLOR + "10m";
    public static final String SET_BG_BRIGHT_YELLOW = SET_BG_COLOR + "11m";
    public static final String SET_BG_BRIGHT_BLUE = SET_BG_COLOR + "12m";
    public static final String SET_BG_BRIGHT_MAGENTA = SET_BG_COLOR + "13m";
    public static final String SET_BG_BRIGHT_CYAN = SET_BG_COLOR + "14m";
    public static final String SET_BG_BRIGHT_WHITE = SET_BG_COLOR + "15m";

    // === 256-color dynamic arrays ===
    public static final String[] TEXT_COLORS = new String[256];
    public static final String[] BG_COLORS = new String[256];

    static {
        for (int i = 0; i < 256; i++) {
            TEXT_COLORS[i] = SET_TEXT_COLOR + i + "m";
            BG_COLORS[i] = SET_BG_COLOR + i + "m";
        }
    }

    // === Unicode Chess Symbols ===
    public static final String WHITE_KING = " ♔ ";
    public static final String WHITE_QUEEN = " ♕ ";
    public static final String WHITE_BISHOP = " ♗ ";
    public static final String WHITE_KNIGHT = " ♘ ";
    public static final String WHITE_ROOK = " ♖ ";
    public static final String WHITE_PAWN = " ♙ ";
    public static final String BLACK_KING = " ♚ ";
    public static final String BLACK_QUEEN = " ♛ ";
    public static final String BLACK_BISHOP = " ♝ ";
    public static final String BLACK_KNIGHT = " ♞ ";
    public static final String BLACK_ROOK = " ♜ ";
    public static final String BLACK_PAWN = " ♟ ";
    public static final String EMPTY = " \u2003 ";

    // === Cursor Positioning ===
    public static String moveCursorToLocation(int x, int y) {
        return UNICODE_ESCAPE + "[" + y + ";" + x + "H";
    }
}
