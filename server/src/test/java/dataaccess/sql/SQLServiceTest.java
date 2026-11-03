package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Service;
import service.ServiceResponse;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SQLServiceTest {

    @BeforeEach
    void reset() {
        Service myService = new Service("SQL");
        myService.clear();
    }

    @Test
    void alternateService() {
        Service myService = new Service("Memory");
    }

    @Test
    void clear() throws DataAccessException {
        Service myService = new Service("SQL");
        myService.register(new UserData("adam","1234","abc@gmail.com"));
        myService.clear();

        assertNull(myService.serviceUserDAO.getUser("adam"));
    }

    @Test
    void register() throws DataAccessException {
        Service myService = new Service("SQL");
        myService.register(new UserData("adam","1234","abc@gmail.com"));

        assertNotNull(myService.serviceUserDAO.getUser("adam"));

        ServiceResponse response = myService.register(new UserData("adam", "password", "123@gmail.com"));

        assert(Objects.equals(response.get("message"), "Error: already taken"));
    }

    @Test
    void login() throws DataAccessException {
        Service myService = new Service("SQL");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        myService.logout(authToken);
        ServiceResponse loginResponse = myService.login(new UserData("adam","1234",null));
        String loginAuthToken = loginResponse.get("authToken");

        assertNotNull(myService.serviceAuthDAO.verifyAuth(loginAuthToken));

        ServiceResponse phonyLoginResponse = myService.login(new UserData("phil", "hercules", "not@not.com"));

        assert(Objects.equals(phonyLoginResponse.get("message"), "Error: unauthorized"));

        ServiceResponse wrongLoginResponse = myService.login(new UserData(null,"123456",null));

        assert(Objects.equals(wrongLoginResponse.get("message"), "Error: bad request"));


    }

    @Test
    void logout() throws DataAccessException {
        Service myService = new Service("SQL");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        myService.logout(authToken);

        assertNull(myService.serviceAuthDAO.verifyAuth(authToken));

        ServiceResponse phonyLogoutResponse = myService.logout(authToken);

        assert(Objects.equals(phonyLogoutResponse.get("message"), "Error: unauthorized"));
    }

    @Test
    void createGame() throws DataAccessException {
        Service myService = new Service("SQL");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        ServiceResponse gameResponse = myService.createGame(authToken, "test game");
        int gameID = Integer.parseInt(gameResponse.get("gameID"));

        assertNotNull(myService.serviceGameDAO.getGame(gameID));

        ServiceResponse phonyCreateResponse = myService.createGame(Service.generateToken(), "dead");

        assert(Objects.equals(phonyCreateResponse.get("message"), "Error: unauthorized"));

        ServiceResponse nullCreateResponse = myService.createGame(Service.generateToken(), null);

        assertEquals(nullCreateResponse.get("message"), "Error: unauthorized");
    }

    @Test
    void listGames() throws DataAccessException {
        Service myService = new Service("SQL");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        myService.createGame(authToken, "test game 1");
        myService.createGame(authToken, "test game 2");
        myService.createGame(authToken, "test game 3");

        assert(myService.serviceGameDAO.getAllGames().size() == 3);
        assert(Objects.equals(myService.listGames(authToken).get("status code"), "200"));
        assert(Objects.equals(myService.listGames(null).get("status code"), "401"));
    }

    @Test
    void joinGame() throws DataAccessException {
        Service myService = new Service("SQL");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        ServiceResponse gameResponse = myService.createGame(authToken, "test game");
        int gameID = Integer.parseInt(gameResponse.get("gameID"));
        myService.joinGame(authToken, ChessGame.TeamColor.WHITE, gameID);

        assert(Objects.equals(myService.serviceGameDAO.getGame(gameID).whiteUsername(), "adam"));

        ServiceResponse phonyJoinResponse = myService.joinGame(Service.generateToken(), ChessGame.TeamColor.WHITE, 0);

        assert(Objects.equals(phonyJoinResponse.get("message"), "Error: unauthorized"));

        ServiceResponse takenJoinResponse = myService.joinGame(authToken, ChessGame.TeamColor.WHITE, gameID);

        assertEquals("Error: already taken", takenJoinResponse.get("message"));

    }
}