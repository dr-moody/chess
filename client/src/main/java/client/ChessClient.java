package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static client.State.*;
import static ui.EscapeSequences.*;

public class ChessClient {
    private State state = SIGNEDOUT;
    private String auth;
    private ServerFacade facade;

    private Scanner scanner;

    public ChessClient (String serverUrl) {
        facade = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println(" Welcome to the chess experience you've always dreamed of. Sign in to start.");
        System.out.print(help());

        scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPropmt();
            String line = scanner.nextLine();

            try {
                System.out.println(eval(line));
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPropmt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

    private String eval(String line) {
        try {
//            String[] tokens = line.toLowerCase().split(" ");
//            String cmd = (tokens.length > 0) ? tokens[0] : "help";
//            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            String cmd = line;
            if (state == SIGNEDOUT) {
                return switch (cmd) {
                    case "login" -> login();
                    case "register" -> register();
                    case "quit" -> "quit";
                    default -> help();
                };
            } else {
                return switch (cmd) {
                    case "logout" -> logout();
                    case "create game" -> createGame();
                    case "list games" -> listGame();
                    case "play game" -> playGame();
                    case "observe game" -> observeGame();
                    default -> help();
                };
            }
        } catch (ResponseException ex) {
            return "error";
        }
    }

    private String login() throws ResponseException {
        System.out.println("Please enter your username");
        String username = scanner.nextLine();
        System.out.println("Please enter your password");
        String password = scanner.nextLine();

        auth = facade.login(new UserData(username, password, null));

        state = SIGNEDIN;
        return "Succesfully logged in";

    }

    private String register() throws ResponseException {
        System.out.println("Please enter your username");
        String username = scanner.nextLine();
        System.out.println("Please enter your password");
        String password = scanner.nextLine();
        System.out.println("Please enter your email");
        String email = scanner.nextLine();

        auth = facade.register(new UserData(username, password, email));

        state = SIGNEDIN;
        return "Successfully registered and signed in";
    }

    private String createGame() throws ResponseException {
        System.out.println("Please enter a game name");
        String gameName = scanner.nextLine();

        facade.createGame(gameName, auth);
        return "Successfully created game";
    }

    private String logout() throws ResponseException {
        facade.logout(auth);
        auth = "";
        state = SIGNEDOUT;

        return "Logged out";
    }

    private String listGame() throws ResponseException {
        ArrayList<GameData> gamesList = facade.listGames(auth);
        for (int i = 1; i <= gamesList.size(); i++) {
            GameData game = gamesList.get(i - 1);
            System.out.println(
                        "id: " + i
                    + ", game name: " + game.gameName()
                    +  ", white: " + game.whiteUsername()
                    +  ", black: " + game.blackUsername()
            );
        }
    }

    private String playGame() throws ResponseException {
        System.out.println("Please enter a game number");
        int gameNum = Integer.parseInt(scanner.nextLine());
        ArrayList<GameData> gamesList = facade.listGames(auth);
        int gameID = gamesList.get(gameNum).gameID();

        String colorText = "";
        boolean triedYet = false;
        while (colorText.toLowerCase(Locale.ROOT).charAt(0) != 'w' &&
                colorText.toLowerCase(Locale.ROOT).charAt(0) != 'b') {
            if (triedYet) {System.out.println("Invalid, please try again.");}
            System.out.println("Would you like to play white or black? (w/b)");
            colorText = scanner.nextLine();
            triedYet = true;
        }
        ChessGame.TeamColor color = colorText.equals("w") ? WHITE : BLACK;
        facade.joinGame(gameID, color, auth);

        drawBoard(gameID, color);
    }

    private String observeGame() {
    }

    private String help() {
        if (state == SIGNEDOUT) {
            return """
                    - help
                    - quit
                    - login
                    - register
                    """;
        }
        return """
                - help
                - logout
                - create game
                - list games
                - play game
                - observe game
                """;
    }

    private void drawBoard(int gameID, ChessGame.TeamColor color) {
    }
}
