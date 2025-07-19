package postlogin;

import chess.ChessGame;
import model.GameData;
import postlogin.boardprinter.BoardPrinter;
import serverfacade.ServerFacade;

import static utilities.EscapeSequences.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import serverfacade.results.*;
import serverfacade.requests.*;

public class PostLogin {
    ServerFacade server;
    String activeAuthToken;
    String username;
    ArrayList<GameData> activeGames = new ArrayList<>();

    public PostLogin(ServerFacade server, LoginResult userData) {
        this.server = server;
        activeAuthToken = userData.authToken();
        username = userData.username();
    }


    public void ui() {
        try (Scanner scanner = new Scanner(System.in);) {
            while (true) {


                System.out.print("[" + username + "] >>> ");
                String input = scanner.nextLine().trim();

                String[] split = input.split("\\s+");

                switch (split[0].toLowerCase()) {
                    case "create":
                        if (split.length == 2) {
                            CreateGameResult result = createGameMenu(new CreateGameRequest(activeAuthToken, split[1]));
                            switch (Objects.requireNonNull(result).httpCode()) {
                                case 200:
                                    updateGameList();
                                    int index = -1;
                                    for (int i = 1; i >= activeGames.size(); i++) {
                                        if (Objects.requireNonNull(getGameFromList(String.valueOf(i))).gameID() == result.gameID()) {
                                            index = i;
                                        }
                                    }
                                    if (index == -1) {
                                        System.out.print("something went wrong with our database connection,");
                                        System.out.print("but your game may still have been made.\n");
                                        System.out.print("try calling " + SET_TEXT_BRIGHT_BLUE + "list" + RESET_TEXT_COLOR + "to check\n");
                                    } else {
                                        System.out.print("Your game has been created and currently assigned the ID ");
                                        System.out.print(SET_TEXT_BRIGHT_BLUE + index + RESET_TEXT_COLOR + "\n");
                                    }
                                    break;
                                case 400:
                                    System.out.print("There was a problem with your request.\n");
                                    System.out.print("Note: Game name must only be alphanumeric or an \"_\" character\n");
                                    break;
                                case 401:
                                    System.out.print("Sorry, but you don't seem to be authorized.\n");
                                    System.out.print("Try loging out and logging in again.\n");
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
                        break;
                    case "list":
                        listMenu();
                        break;
                    case "join":
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
                                break;
                            }

                            JoinGameResult result = joinGameMenu(new JoinGameRequest(activeAuthToken, color.name(), Objects.requireNonNull(getGameFromList(split[1])).gameID()));
                            switch (Objects.requireNonNull(result).httpCode()) {
                                case 200:
                                    enterGame(Objects.requireNonNull(getGameFromList(split[1])));
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
                        break;
                    case "observe":
                        if (split.length == 2) {
                            GameData game = getGameFromList(split[1]);
                            enterGame(game);
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

    }

    private void enterGame(GameData gameData) {
        if (username.equals(gameData.blackUsername())) {
            BoardPrinter.print(gameData.game(), ChessGame.TeamColor.BLACK, null);
        } else {
            BoardPrinter.print(gameData.game(), ChessGame.TeamColor.WHITE, null);
        }
    }


    private CreateGameResult createGameMenu(CreateGameRequest createGameRequest) {
        return null;
    }

    private JoinGameResult joinGameMenu(JoinGameRequest joinGameRequest) {
        return null;
    }

    private void listMenu() {
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
        if (num < 0 || num >= activeGames.size()) {
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
