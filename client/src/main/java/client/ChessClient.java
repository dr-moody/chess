package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
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
        StringBuilder gameString = new StringBuilder();
        ArrayList<GameData> gamesList = facade.listGames(auth);
        for (int i = 1; i <= gamesList.size(); i++) {
            GameData game = gamesList.get(i - 1);
            gameString
                    .append("id: ")
                    .append(i)
                    .append(", game name: ")
                    .append(game.gameName())
                    .append(", white: ")
                    .append(game.whiteUsername())
                    .append(", black: ")
                    .append(game.blackUsername())
                    .append("\n");
        }
        return gameString.toString();
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

        return drawBoard(gameID, color);
    }

    private String observeGame() throws ResponseException {
        System.out.println("Please enter a game number");
        int gameNum = Integer.parseInt(scanner.nextLine());
        ArrayList<GameData> gamesList = facade.listGames(auth);
        int gameID = gamesList.get(gameNum).gameID();

        facade.joinGame(gameID, null, auth);

        return drawBoard(gameID, WHITE);
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

    private String drawBoard(int gameID, ChessGame.TeamColor color) throws ResponseException {
        ChessBoard gameBoard = facade.listGames(auth).get(gameID).game().getBoard();
        ChessPiece[][] board = gameBoard.board();


        StringBuilder boardString = new StringBuilder();

        if (color == WHITE) {
            // Need to iter through the rows backwards so it doesn't print upsidedown
            for (int i = board.length - 1; i >= 0; i--) {
                boardString.append(SET_BG_COLOR_YELLOW + i + " " + RESET);
                for (int j = 0 ; j < Arrays.copyOfRange(board[i], 0, 8).length; j++) {
                    ChessPiece square =  Arrays.copyOfRange(board[i], 0, 8)[j];
                    String squareColor = (i + j % 2 == 0) ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREEN;
                    boardString.append(squareColor);
                    if (square != null) {
                        boardString.append(square.toString()).append(" ");
                    } else {
                        boardString.append(". ");
                    }
                    boardString.append(RESET);
                }
                boardString.append("\n");
            }
            boardString.append(" ");
            for (int k = 1; k <= 8; k++) {
                boardString.append(SET_BG_COLOR_YELLOW + k + " " + RESET);
            }
        } else {
            for (int i = board.length; i < 8; i++) {
                boardString.append(SET_BG_COLOR_YELLOW + i + " " + RESET);
                for (int j = 0 ; j < Arrays.copyOfRange(board[i], 0, 8).length; j++) {
                    ChessPiece square =  Arrays.copyOfRange(board[i], 0, 8)[j];
                    String squareColor = (i + j % 2 == 0) ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREEN;
                    boardString.append(squareColor);
                    if (square != null) {
                        boardString.append(square.toString()).append(" ");
                    } else {
                        boardString.append(". ");
                    }
                    boardString.append(RESET);
                }
                boardString.append("\n");
            }
            boardString.append(" ");
            for (int k = 1; k <= 8; k++) {
                boardString.append(SET_BG_COLOR_YELLOW + k + " " + RESET);
            }
        }

        return boardString.toString();
    }
}
