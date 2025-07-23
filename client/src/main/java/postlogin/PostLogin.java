package postlogin;

import chess.ChessGame;
import model.GameData;
import postlogin.gameplay.Gameplay;
import serverfacade.ServerFacade;

import static utilities.EscapeSequences.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import serverfacade.results.*;
import serverfacade.requests.*;
import utilities.UserInputConstraints;

public class PostLogin {
    ServerFacade server;
    String activeAuthToken;
    String username;
    ArrayList<GameData> activeGames = new ArrayList<>();
    Scanner scanner;

    public PostLogin(ServerFacade server, Scanner scanner) {
        this.server = server;
        this.scanner = scanner;
    }


    public void ui(LoginResult userData) {
        activeAuthToken = userData.authToken();
        username = userData.username();
        updateGameList();
        while (true) {
            System.out.print("[" + username + "] >>> ");

            if (!scanner.hasNextLine()) {
                System.out.println("Input stream closed. Exiting...");
                server.logout(new LogoutRequest(activeAuthToken));
                break;
            }

            String input = scanner.nextLine().trim();

            String[] split = input.split("\\s+");

            switch (split[0].toLowerCase()) {
                case "create":
                    createGameMenu(split);
                    break;
                case "list":
                    listMenu();
                    break;
                case "join":
                    joinGameMenu(split);
                    break;
                case "observe":
                    if (split.length == 2) {
                        GameData game = getGameFromList(split[1]);
                        if (game != null) {
                            enterGame(Objects.requireNonNull(game), null);
                        } else {
                            System.out.print("Sorry, I cant find that game\n");
                        }
                    }
                    break;
                case "logout":
                    server.logout(new LogoutRequest(activeAuthToken));
                    activeAuthToken = null;
                    return;
                case "quit":
                    server.logout(new LogoutRequest(activeAuthToken));
                    activeAuthToken = null;
                    System.out.println("I hope you enjoyed my game!");
                    System.exit(0);
                case "help":
                    printHelpMenu();
                    break;
                default:
                    badInputResponse();
            }

            System.out.println(); // spacing
        }
    }

    private void joinGameMenu(String[] split) {
        if (split.length == 3) {
            ChessGame.TeamColor color;
            if (split[2].equalsIgnoreCase("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (split[2].equalsIgnoreCase("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                System.out.print("Sorry, I can't tell which team you are trying to join\n");
                System.out.print("You can only join as ");
                System.out.print(SET_TEXT_BRIGHT_BLUE + "white" + RESET_TEXT_COLOR + " or ");
                System.out.print(SET_TEXT_BRIGHT_BLUE + "black" + RESET_TEXT_COLOR + "\n");
                return;
            }

            GameData game = getGameFromList(split[1]);
            if (game == null) {
                System.out.print("Sorry, I can't find that game.\n");
                return;
            }

            JoinGameResult result = joinGameProcess(new JoinGameRequest
                    (activeAuthToken, color.name(), game.gameID()));
            switch (Objects.requireNonNull(result).httpCode()) {
                case 200:
                    enterGame(Objects.requireNonNull(getGameFromList(split[1])), color.name());
                    break;
                case 400:
                    System.out.print("There was a problem with your request.\n");
                    break;
                case 401:
                    System.out.print("Sorry, but you don't seem to be authorized.\n");
                    System.out.print("Try loging out and logging in again.\n");
                case 403:
                    System.out.print("Sorry, but that position is taken.\n");
                    break;
                case 500:
                    System.out.print("Sorry, but there appears to have been a problem with our database.\n");
                    break;
                default:
                    System.out.print("Something went wrong with your request\n");
                    System.out.print("Please try again shortly\n");
            }
        } else {
            badInputResponse();
        }
    }

    private void createGameMenu(String[] split) {
        if (split.length == 2 && split[1].length() <= UserInputConstraints.MAX_GAMENAME_LENGTH) {
            CreateGameResult result = createGameProcess(new CreateGameRequest(activeAuthToken, split[1]));

            if (result == null) {
                System.out.print("Something went wrong creating the game. No result was returned.\n");
                return;
            }

            switch (result.httpCode()) {
                case 200:
                    updateGameList();
                    boolean found = false;
                    for (int i = 0; i < activeGames.size(); i++) {
                        var game = activeGames.get(i);
                        if (game != null && game.gameID() == result.gameID()) {
                            found = true;
                            System.out.print("Your game has been created and currently assigned the ID ");
                            System.out.print(SET_TEXT_BRIGHT_BLUE + (i + 1) + RESET_TEXT_COLOR + "\n");
                            break;
                        }
                    }
                    if (!found) {
                        System.out.print("Something went wrong with our database connection, ");
                        System.out.print("but your game may still have been made.\n");
                        System.out.print("Try calling " + SET_TEXT_BRIGHT_BLUE + "list" + RESET_TEXT_COLOR + " to check.\n");
                    }
                    break;

                case 400:
                    System.out.print("There was a problem with your request.\n");
                    System.out.print("Note: Game name must only be alphanumeric or an \"_\" character.\n");
                    break;

                case 401:
                    System.out.print("Sorry, but you don't seem to be authorized.\n");
                    System.out.print("Try logging out and logging in again.\n");
                    break;

                case 500:
                    System.out.print("Sorry, but there appears to have been a problem with our database.\n");
                    break;

                default:
                    System.out.print("Something went wrong with your request.\n");
                    System.out.print("Please try again shortly.\n");
            }
        } else {
            badInputResponse();
        }
    }


    private void enterGame(GameData gameData, String status) {
        ChessGame.TeamColor pos;
        if (Objects.equals(status, "WHITE")) {
            pos = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(status, "BLACK")) {
            pos = ChessGame.TeamColor.BLACK;
        } else {
            pos = null;
        }
        Gameplay game = new Gameplay(activeAuthToken, username, gameData.gameID(), scanner, server, pos);
        game.ui();
    }


    private CreateGameResult createGameProcess(CreateGameRequest createGameRequest) {
        if (!createGameRequest.gameName().matches("[a-zA-Z0-9_]+") ||
                createGameRequest.gameName().length() > UserInputConstraints.MAX_GAMENAME_LENGTH) {
            return new CreateGameResult(400, "Error: illegal game name", null);
        }

        return server.createGame(createGameRequest);
    }

    private JoinGameResult joinGameProcess(JoinGameRequest joinGameRequest) {
        return server.joinGame(joinGameRequest);
    }

    private void listMenu() {
        updateGameList();

        if (activeGames.isEmpty()) {
            System.out.print("There are no active games\nYou can create one with ");
            System.out.print(SET_TEXT_BRIGHT_BLUE + "create <GAME_NAME>" + RESET_TEXT_COLOR + "\n");
            return;
        }


        String header = String.format("║  %-5s ║ %-30s ║ %-30s ║ %-30s ║", "ID", "Game Name", "White Player", "Black Player");
        String topBorder =
                "╔════════╦════════════════════════════════╦════════════════════════════════╦════════════════════════════════╗";
        String midBorder =
                "╠════════╬════════════════════════════════╬════════════════════════════════╬════════════════════════════════╣";
        String bottomBorder =
                "╚════════╩════════════════════════════════╩════════════════════════════════╩════════════════════════════════╝";

        System.out.println(topBorder);
        System.out.println(header);
        System.out.println(midBorder);

        for (int i = 0; i < activeGames.size(); i++) {
            String white = activeGames.get(i).whiteUsername() != null ? activeGames.get(i).whiteUsername() : "(open)";
            String black = activeGames.get(i).blackUsername() != null ? activeGames.get(i).blackUsername() : "(open)";
            String row = String.format("║  %-5d ║ %-30s ║ %-30s ║ %-30s ║",
                    i + 1,
                    activeGames.get(i).gameName(),
                    white,
                    black);
            System.out.println(row);
        }

        System.out.println(bottomBorder);
    }


    private static void badInputResponse() {
        System.out.println("I am sorry. I didn't quite understand that.");
        System.out.println
                ("Please try again, or type "
                        + SET_TEXT_BRIGHT_BLUE + "help" + RESET_TEXT_COLOR +
                        " for help");
    }


    private GameData getGameFromList(String listNum) {
        int num = Integer.parseInt(listNum);
        if (num < 1 || num > activeGames.size()) {
            return null;
        }
        return activeGames.get(num - 1);
    }

    private void updateGameList() {
        activeGames.clear();
        activeGames.addAll(server.listGames(new ListGamesRequest(activeAuthToken)).games());
    }


    private void printHelpMenu() {
        System.out.print(SET_TEXT_BRIGHT_BLUE + "create <GAME_NAME>" + RESET_TEXT_COLOR);
        System.out.print(" -> to create a new game\n");
        System.out.print("Note: Game name must only be alphanumeric or an \"_\" character\n");
        System.out.print("Game name must be " + UserInputConstraints.MAX_GAMENAME_LENGTH + " characters or less.\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "list" + RESET_TEXT_COLOR);
        System.out.print(" -> lists all active games\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "join <ID> <WHITE/BLACK>" + RESET_TEXT_COLOR);
        System.out.print(" -> join game as selected color\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "observe <ID>" + RESET_TEXT_COLOR);
        System.out.print(" -> to create a new game\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "logout" + RESET_TEXT_COLOR);
        System.out.print(" -> logout and return to login page\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "quit" + RESET_TEXT_COLOR);
        System.out.print(" -> logout and close the program\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "help" + RESET_TEXT_COLOR);
        System.out.print(" -> display currently available actions\n");
    }


}
