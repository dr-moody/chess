package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.internal.Streams;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import java.util.*;

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
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                if (!result.equals("quit")) {System.out.println(result);}

            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt() {
        System.out.print(RESET + "\n" + GREEN + ">>> " + RESET);
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
                    case "quit" -> "quit";
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
        ArrayList<Integer> gameNums = getGameNum();
        int gameID = gameNums.get(0);
        int gameNum = gameNums.get(1);

        String colorText = "";
        boolean triedYet = false;
        while (colorText.isEmpty() || (colorText.toLowerCase(Locale.ROOT).charAt(0) != 'w' &&
                colorText.toLowerCase(Locale.ROOT).charAt(0) != 'b')) {
            if (triedYet) {System.out.println("Invalid, please try again.");}
            System.out.println("Would you like to play white or black? (w/b)");
            colorText = scanner.nextLine();
            triedYet = true;
        }
        ChessGame.TeamColor color = colorText.equals("w") ? WHITE : BLACK;
        facade.joinGame(gameID, color, auth);

        return drawBoard(gameNum - 1, color);
    }

    private String observeGame() throws ResponseException {
        ArrayList<Integer> gameNums = getGameNum();
        int gameID = gameNums.get(0);
        int gameNum = gameNums.get(1);

        facade.joinGame(gameID, null, auth);

        return drawBoard(gameNum - 1, WHITE);
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
                - quit
                """;
    }

    private ArrayList<Integer> getGameNum () throws ResponseException {
        int gameID;
        int gameNum;
        while (true) {
            try {
                System.out.println("Please enter a game number");
                gameNum = Integer.parseInt(scanner.nextLine());
                ArrayList<GameData> gamesList = facade.listGames(auth);
                gameID = gamesList.get(gameNum - 1).gameID();
                break;
            } catch (Throwable _) {
                System.out.println("Invalid game number, please choose one from below");
                System.out.println(listGame());
            }
        }
        return new ArrayList<>(List.of(gameID, gameNum));
    }

    private String drawBoard(int gameNum, ChessGame.TeamColor color) throws ResponseException {
        ChessBoard gameBoard = facade.listGames(auth).get(gameNum).game().getBoard();
        ChessPiece[][] board = gameBoard.board();


        StringBuilder boardString = new StringBuilder();

        if (color == WHITE) {
            // Need to iter through the rows backwards so it doesn't print upsidedown
            for (int i = board.length - 1; i >= 0; i--) {
                boardString.append(SET_BG_COLOR_BLACK + " " + (i + 1) + " " + RESET);
                for (int j = 0 ; j < Arrays.copyOfRange(board[i], 0, 8).length; j++) {
                    boardString.append(drawSquare(board, i, j));
                }
                boardString.append("\n");
            }
            boardString.append("   ");
            for (int k = 1; k <= 8; k++) {
                boardString.append(SET_BG_COLOR_BLACK + " " + numToAlph(k) + " " + RESET);
            }
        } else {
            for (int i = 0; i < board.length; i++) {
                boardString.append(SET_BG_COLOR_BLACK + " " + (i + 1) + " " + RESET);
                for (int j = Arrays.copyOfRange(board[i], 0, 8).length - 1; j >= 0 ; j--) {
                    boardString.append(drawSquare(board, i, j));
                }
                boardString.append("\n");
            }
            boardString.append("   ");
            for (int k = 8; k >= 1; k--) {
                boardString.append(SET_BG_COLOR_BLACK + " " + numToAlph(k) + " " + RESET);
            }
        }

        return boardString.toString();
    }

    private String numToAlph(int k) {
        return switch (k) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> "null";
        };
    }

    private String drawSquare(ChessPiece[][] board, int i, int j) {
        StringBuilder squareString = new StringBuilder();
        ChessPiece square =  Arrays.copyOfRange(board[i], 0, 8)[j];
        String squareColor = ((i + j) % 2 == 0) ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_LIGHT_GREY;
        squareString.append(squareColor);
        if (square != null) {
            String pieceColor = (square.getTeamColor() == WHITE) ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK;
            squareString.append(pieceColor);
            squareString.append(" ").append(square.toString()).append(" ");
        } else {
            squareString.append("   ");
        }
        squareString.append(RESET);

        return squareString.toString();
    }


}
