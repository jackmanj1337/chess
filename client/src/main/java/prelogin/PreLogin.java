package prelogin;

import model.GameData;
import serverfacade.ServerFacade;

import static utilities.EscapeSequences.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import serverfacade.results.*;
import serverfacade.requests.*;
import utilities.UserInputConstraints;

public class PreLogin {
    ServerFacade server;


    public PreLogin(ServerFacade server) {
        this.server = server;
    }


    public LoginResult ui() {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;


        while (!loggedIn) {


            System.out.print("[LOGGED_OUT] >>> ");
            String input = scanner.nextLine().trim();

            String[] split = input.split("\\s+");

            switch (split[0].toLowerCase()) {
                case "help":
                    printHelpMenu();
                    break;
                case "quit":
                    System.out.println("I hope you enjoyed my game!");
                    System.exit(0);
                case "login":
                    if (split.length == 3) {
                        LoginResult result = loginMenu(new LoginRequest(split[1], split[2]));
                        switch (Objects.requireNonNull(result).httpCode()) {
                            case 200:
                                System.out.print("Welcome " + split[1] + "\n");
                                return result;
                            case 400:
                                System.out.print("There was a problem with your request.\n");
                                System.out.print("Note: Username and password must only be alphanumeric or an \"_\" character.\n");
                                break;
                            case 401:
                                System.out.print("Sorry, but there is problem in your username or password.\n");
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
                case "register":
                    if (split.length == 4 && (split[1].length() <= UserInputConstraints.MAX_USERNAME_LENGTH)) {
                        RegisterResult result = registerMenu(new RegisterRequest(split[1], split[2], split[3]));
                        switch (Objects.requireNonNull(result).httpCode()) {
                            case 200:
                                System.out.print("Welcome " + split[1] + "\n");
                                return new LoginResult(result.httpCode(), result.message(), result.username(), result.authToken());
                            case 400:
                                System.out.print("There was a problem with your request.\n");
                                System.out.print("Note: Username and password must only be alphanumeric or an \"_\" character.\n");
                                break;
                            case 403:
                                System.out.print("Sorry, but that username is already in use.\n");
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
                default:
                    badInputResponse();
            }

            System.out.println(); // spacing
        }

        scanner.close();

        return null;
    }


    private static void badInputResponse() {
        System.out.println("I am sorry. I didn't quite understand that.");
        System.out.println
                ("Please try again, or type "
                        + SET_TEXT_BRIGHT_BLUE + "help" + RESET_TEXT_COLOR +
                        " for help");
    }

    private RegisterResult registerMenu(RegisterRequest request) {
        if (!request.username().matches("[a-zA-Z0-9_]+") || !request.password().matches("[a-zA-Z0-9_]+")) {
            return new RegisterResult(400, "Error: illegal username or password", null, null);
        }

        return server.register(request);
    }

    private LoginResult loginMenu(LoginRequest linreq) {
        if (!linreq.username().matches("[a-zA-Z0-9_]+") || !linreq.password().matches("[a-zA-Z0-9_]+")) {
            return new LoginResult(400, "Error: illegal username or password", null, null);
        }

        return server.login(linreq);
    }

    private void printHelpMenu() {
        System.out.print(SET_TEXT_BRIGHT_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR);
        System.out.print(" -> to create a new account\n");
        System.out.print("Note: Username and password must only be alphanumeric or an \"_\" character.\n");
        System.out.print("Username must be " + UserInputConstraints.MAX_USERNAME_LENGTH + " characters or less.\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR);
        System.out.print(" -> login to an existing account\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "quit" + RESET_TEXT_COLOR);
        System.out.print(" -> close the program\n");

        System.out.print(SET_TEXT_BRIGHT_BLUE + "help" + RESET_TEXT_COLOR);
        System.out.print(" -> display currently available actions\n");
    }


}
