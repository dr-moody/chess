package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class Service {
    AuthDAO serviceAuthDAO;
    GameDAO serviceGameDAO;
    UserDAO serviceUserDAO;

    public Service (String DAOType) {
        if (Objects.equals(DAOType, "Memory")) {
            serviceAuthDAO = new MemoryAuthDAO();
            serviceGameDAO = new MemoryGameDAO();
            serviceUserDAO = new MemoryUserDAO();

        } else if (Objects.equals(DAOType, "SQL")) {
            serviceAuthDAO = null;
            serviceGameDAO = null;
            serviceUserDAO = null;
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public ServiceResponse clear() {
        serviceAuthDAO.clear();
        serviceUserDAO.clear();
        serviceGameDAO.clear();
        return new ServiceResponse("status code:200");
    }

    public ServiceResponse register(UserData RegisterRequest) {

        if (serviceUserDAO.getUser(RegisterRequest.username()) != null) {
            return new ServiceResponse("status code:403,message:Error: already taken");
        } else if (RegisterRequest.password() == null) {
            return new ServiceResponse("status code:400,message:Error: invalid password");
        }

        serviceUserDAO.createUser(RegisterRequest);
        String authToken = generateToken();
        serviceAuthDAO.createAuth(new AuthData(authToken, RegisterRequest.username()));
        return new ServiceResponse("status code:200,username:"+RegisterRequest.username()+",authToken:"+authToken);

    }

    public ServiceResponse login(UserData LoginRequest) {
        UserData user = serviceUserDAO.getUser(LoginRequest.username());
        if (LoginRequest.username() == null || LoginRequest.password() == null) {
            return new ServiceResponse("status code:400,message:Error: bad request");
        } else if (user == null || !Objects.equals(user.password(), LoginRequest.password())) {
            return new ServiceResponse("status code:401,message:Error: unauthorized");
        }
        String authToken = generateToken();
        serviceAuthDAO.createAuth(new AuthData(authToken, LoginRequest.username()));
        return new ServiceResponse("status code:200,username:"+LoginRequest.username()+",authToken:"+authToken);
    }

    public ServiceResponse logout(String authToken) {
        if (serviceAuthDAO.deleteAuth(authToken)) {
            return new ServiceResponse("status code:200");
        }
        else {
            return new ServiceResponse("status code:401,message:Error: unauthorized");
        }
    }

    public ServiceResponse listGames(String authToken) {
        if (serviceAuthDAO.verifyAuth(authToken) == null) {
            return new ServiceResponse("status code:401,message:Error: unauthorized");
        }
        String gamesValue = new Gson().toJson(serviceGameDAO.getAllGames());
        return new ServiceResponse("status code:200,games:"+gamesValue);
    }

    public ServiceResponse createGame(String authToken, String gameName) {
        if (serviceAuthDAO.verifyAuth(authToken) == null) {
            return new ServiceResponse("status code:401,message:Error: unauthorized");
        }

        int i = 0;
        while (serviceGameDAO.getGame(i) != null) {
            i++;
        }
        serviceGameDAO.createGame(new GameData(i, null, null, gameName, new ChessGame()));


        return new ServiceResponse("status code:200,gameID:"+Integer.toString(i));
    }

    public ServiceResponse joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) {
        if (serviceAuthDAO.verifyAuth(authToken) == null) {
            return new ServiceResponse("status code:401,message:Error: unauthorized");
        }

        GameData game = serviceGameDAO.getGame(gameID);

        if (game == null) {
            return new ServiceResponse("status code:400,message:Error: bad request");
        }

        String username = serviceAuthDAO.verifyAuth(authToken);

        if (playerColor == ChessGame.TeamColor.WHITE) {
            serviceGameDAO.updateGame(new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game()));
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            serviceGameDAO.updateGame(new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game()));
        }

        return new ServiceResponse("status code:200");
    }

}
