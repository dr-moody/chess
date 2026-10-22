package service;

import model.UserData;
import org.junit.jupiter.api.Test;

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


    }

    @Test
    void logout() {
        Service myService = new Service("Memory");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        myService.logout(authToken);

        assertNull(myService.serviceAuthDAO.verifyAuth(authToken));
    }

    @Test
    void createGame() {
        Service myService = new Service("Memory");
        ServiceResponse response = myService.register(new UserData("adam","1234","abc@gmail.com"));
        String authToken = response.get("authToken");
        myService.createGame(authToken, "test game");
    }

    @Test
    void listGames() {
    }

    @Test
    void joinGame() {
    }
}