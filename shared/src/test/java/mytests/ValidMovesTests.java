package mytests;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.chess.TestUtilities;

import java.util.ArrayList;
import java.util.List;

public class ValidMovesTests {
    private static final String TRAPPED_PIECE_MOVES = "ChessGame validMoves returned valid moves for a trapped piece";

    @Test
    @DisplayName("Piece Completely Trapped")
    public void rookPinnedToKing() {

        var game = new ChessGame();
        game.setBoard(TestUtilities.loadBoard("""
                |K| | | | | | |Q|
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | |r| | | | |
                | | | | | | | | |
                | |k| | | | | | |
                | | | | | | | | |
                """));

        ChessPosition position = new ChessPosition(4, 4);
        Assertions.assertTrue(game.validMoves(position).isEmpty(), TRAPPED_PIECE_MOVES);
    }


}
