package postlogin.boardprinter;

import chess.ChessGame;
import chess.ChessPiece;

import static postlogin.boardprinter.ColorSettings.*;
import static utilities.EscapeSequences.*;

public class BoardSquare {
    private String color;
    private String content;

    public BoardSquare(String color, String content) {
        this.color = color;
        this.content = content;
    }

    public BoardSquare(String color) {
        this.color = color;
        content = EMPTY;
    }

    public void highlight() {
        color = HIGHLIGHT_COLOR;
    }

    public void fillSquare(ChessPiece piece) {
        if (piece != null) {
            String symbol = switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case ROOK -> BLACK_ROOK;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case PAWN -> BLACK_PAWN;
            };

            String textColor = (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? PIECE_LIGHT_COLOR
                    : PIECE_DARK_COLOR;

            this.content = textColor + symbol;
        } else {
            this.content = EMPTY;
        }
    }

    public void print() {
        System.out.print(color);
        System.out.print(content);
        System.out.print(RESET_TEXT_COLOR);
        System.out.print(RESET_BG_COLOR);
    }

    public static BoardSquare[][] generateEmptyLabeledBoard() {
        BoardSquare[][] board = new BoardSquare[10][10];

        // Define labels
        String[] colLabels = {" ａ ", " ｂ ", " ｃ ", " ｄ ", " ｅ ", " ｆ ", " ｇ ", " ｈ "};
        String[] rowLabels = {" １ ", " ２ ", " ３ ", " ４ ", " ５ ", " ６ ", " ７ ", " ８ "};

        // Top row (labels)
        board[0][0] = new BoardSquare(BOARDER_COLOR, EMPTY);
        for (int col = 0; col < 8; col++) {
            board[0][col + 1] = new BoardSquare
                    (BOARDER_COLOR, LABEL_COLOR + colLabels[col]);
        }
        board[0][9] = new BoardSquare(BOARDER_COLOR, EMPTY);

        // Middle rows (board squares with row labels)
        for (int row = 0; row < 8; row++) {
            // Left row label
            board[row + 1][0] = new BoardSquare
                    (BOARDER_COLOR, LABEL_COLOR + rowLabels[row]);

            for (int col = 0; col < 8; col++) {
                String squareColor = (row + col) % 2 == 0 ? BOARD_DARK_COLOR : BOARD_LIGHT_COLOR;
                board[row + 1][col + 1] = new BoardSquare(squareColor);
            }

            // Right row label
            board[row + 1][9] = new BoardSquare
                    (BOARDER_COLOR, LABEL_COLOR + rowLabels[row]);
        }

        // Bottom row (labels)
        board[9][0] = new BoardSquare(BOARDER_COLOR, EMPTY);
        for (int col = 0; col < 8; col++) {
            board[9][col + 1] = new BoardSquare
                    (BOARDER_COLOR, LABEL_COLOR + colLabels[col]);
        }
        board[9][9] = new BoardSquare(BOARDER_COLOR, EMPTY);

        return board;
    }

}
