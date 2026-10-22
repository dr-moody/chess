package service;

import chess.ChessGame;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    @Test
    void clear() {
        Service myService = new Service("Memory");
        myService.register(new UserData("adam","1234","abc@gmail.com"));
        myService.clear();

        assertNull(myService.serviceUserDAO.getUser("adam"));
    }

    @Test
    void register() {
        Service myService = new Service("Memory");
        myService.register(new UserData("adam","1234","abc@gmail.com"));

        assertNotNull(myService.serviceUserDAO.getUser("adam"));

        ServiceResponse response = myService.register(new UserData("adam", "password", "123@gmail.com"));

        assert(Objects.equals(response.get("message"), "Error: already taken"));
    }

    @Test
    void login() {
        Service myService = new Service("Memory");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        myService.logout(authToken);
        ServiceResponse loginResponse = myService.login(new UserData("adam","1234",null));
        String loginAuthToken = loginResponse.get("authToken");

        assertNotNull(myService.serviceAuthDAO.verifyAuth(loginAuthToken));

        ServiceResponse phonyLoginResponse = myService.login(new UserData("phil", "hercules", "not@not.com"));

        assert(Objects.equals(phonyLoginResponse.get("message"), "Error: bad request"));

        ServiceResponse wrongLoginResponse = myService.login(new UserData("adam","123456",null));

        assert(Objects.equals(wrongLoginResponse.get("message"), "Error: unauthorized"));


    }

    @Test
    void logout() {
        Service myService = new Service("Memory");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        myService.logout(authToken);

        assertNull(myService.serviceAuthDAO.verifyAuth(authToken));

        ServiceResponse phonyLogoutResponse = myService.logout(authToken);

        assert(Objects.equals(phonyLogoutResponse.get("message"), "Error: unauthorized"));
    }

    @Test
    void createGame() {
        Service myService = new Service("Memory");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        ServiceResponse gameResponse = myService.createGame(authToken, "test game");
        int gameID = Integer.parseInt(gameResponse.get("gameID"));

        assertNotNull(myService.serviceGameDAO.getGame(gameID));
    }

    @Test
    void listGames() {
        Service myService = new Service("Memory");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        myService.createGame(authToken, "test game 1");
        myService.createGame(authToken, "test game 2");
        myService.createGame(authToken, "test game 3");

        assert(myService.serviceGameDAO.getAllGames().size() == 3);
    }

    @Test
    void joinGame() {
        Service myService = new Service("Memory");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        ServiceResponse gameResponse = myService.createGame(authToken, "test game");
        int gameID = Integer.parseInt(gameResponse.get("gameID"));
        myService.joinGame(authToken, ChessGame.TeamColor.WHITE, gameID);

        assert(Objects.equals(myService.serviceGameDAO.getGame(gameID).whiteUsername(), "adam"));
    }
}