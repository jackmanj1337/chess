package postlogin.gameplay;

import chess.*;
import model.GameData;
import postlogin.boardprinter.BoardPrinter;
import serverfacade.ServerFacade;
import serverfacade.WebsocketFacade;
import serverfacade.requests.LogoutRequest;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Objects;
import java.util.Scanner;

import static prelogin.PreLogin.badInputResponse;
import static utilities.EscapeSequences.*;

public class Gameplay {
    private final String authToken;
    private final String username;
    private final int gameID;
    private final WebsocketFacade websocketFacade;
    private final ServerFacade server;
    private final Scanner scanner;
    private static GameData gameData;
    private static ChessGame.TeamColor teamColor;

    public Gameplay(
            String authToken,
            String username,
            int gameID,
            Scanner scanner,
            ServerFacade server,
            ChessGame.TeamColor teamColor) {
        this.authToken = authToken;
        this.username = username;
        this.gameID = gameID;
        this.scanner = scanner;
        this.server = server;
        Gameplay.teamColor = teamColor;
        websocketFacade = new WebsocketFacade(authToken, gameID, username);
    }

    // is it working now?

    public void ui() {

        while (true) {
            System.out.print("[" + username + "] >>> ");

            if (!scanner.hasNextLine()) {
                System.out.println("Input stream closed. Exiting...");
                websocketFacade.sendMessage(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
                server.logout(new LogoutRequest(authToken));
                break;
            }

            String input = scanner.nextLine().trim();

            String[] split = input.split("\\s+");

            switch (split[0].toLowerCase()) {
                case "help":
                    printHelpMenu();
                    break;
                case "redraw":
                    BoardPrinter.print(teamColor, null, gameData);
                    break;
                case "leave":
                    websocketFacade.sendMessage(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
                    return;
                case "move":
                    if (split.length >= 3) {
                        ChessPosition start = notationToPosition(split[1]);
                        ChessPosition end = notationToPosition(split[2]);
                        if (start == null || end == null) {
                            badInputResponse();
                        }
                        ChessPiece.PieceType promotion;
                        if (split.length == 3) {
                            promotion = null;
                        } else {
                            promotion = chessPieceNameToType(split[3]);
                        }
                        ChessMove move = new ChessMove(start, end, promotion);
                        boolean goodMove = true;
                        try {
                            gameData.game().makeMove(move);
                        } catch (InvalidMoveException e) {
                            System.out.print(SET_TEXT_BRIGHT_RED);
                            System.out.println(e.getMessage());
                            System.out.print(RESET_TEXT_COLOR);
                            goodMove = false;
                        }
                        if (goodMove) {
                            websocketFacade.sendMessage(new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move));
                        }
                    } else {
                        badInputResponse();
                    }
                    break;
                case "resign":
                    websocketFacade.sendMessage(new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID));
                    break;
                case "inspect":
                    if (split.length == 2) {
                        BoardPrinter.print(teamColor, notationToPosition(split[1]), gameData);
                    } else {
                        badInputResponse();
                    }
                    break;
                default:
                    badInputResponse();

            }
        }
    }

    private static ChessPiece.PieceType chessPieceNameToType(String in) {
        return switch (in.toUpperCase()) {
            case "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "ROOK" -> ChessPiece.PieceType.ROOK;
            case "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            default -> null;
        };
    }

    private ChessPosition notationToPosition(String notation) {
        if (notation == null || notation.length() != 2) {
            return null;
        }

        char fileChar = Character.toLowerCase(notation.charAt(0));
        char rankChar = notation.charAt(1);

        if (fileChar < 'a' || fileChar > 'h' || rankChar < '1' || rankChar > '8') {
            return null;
        }

        int col = fileChar - 'a' + 1;
        int row = rankChar - '1' + 1;

        return new ChessPosition(row, col);
    }

    private void printHelpMenu() {
        System.out.print("NOTE: All positions should be inputted as chess notation\n");
        System.out.print("Example: " + SET_TEXT_BRIGHT_GREEN + "move a1 h8" + RESET_TEXT_COLOR + "\n\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "redraw" + RESET_TEXT_COLOR);
        System.out.print(" -> Redraws the current game state at the bottom of the screen\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "inspect <position>" + RESET_TEXT_COLOR);
        System.out.print(" -> Shows all the legal moves that can be made by a piece at the specified position\n");
        System.out.print("If the position is invalid or empty simply redraws the board with no highlighting.\n");

        if (teamColor != null) {
            System.out.print(SET_TEXT_BRIGHT_BLUE + "move <StartPosition> <EndPosition>" + RESET_TEXT_COLOR);
            System.out.print(" -> Attempts to move the piece at the Start position to the End position\n");

            System.out.print(SET_TEXT_BRIGHT_BLUE + "move <StartPosition> <EndPosition> <PromotionPiece>"
                    + RESET_TEXT_COLOR);
            System.out.print(" -> Attempts to move the piece at the Start position to " +
                    "the End position and promote to the specified piece\n");
            System.out.print("Note: Specifying an illegal promotion or failing to " +
                    "specify a required one will cause the whole move to fail\n");

            System.out.print(SET_TEXT_BRIGHT_BLUE + "resign" + RESET_TEXT_COLOR);
            System.out.print(" -> Concedes the game to the other player and ends the game.\n");
            System.out.print("Requires confirmation after initial execution\n");
        }

        System.out.print(SET_TEXT_BRIGHT_BLUE + "leave" + RESET_TEXT_COLOR);
        System.out.print(" -> Leaves the current game and return to the game selection menu.\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "help" + RESET_TEXT_COLOR);
        System.out.print(" -> display currently available actions\n");
    }

    public static void updateGameData(GameData data) {
        gameData = data;
        BoardPrinter.print(teamColor, null, gameData);
    }


}
