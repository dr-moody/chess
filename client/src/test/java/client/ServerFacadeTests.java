package client;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    UserData testUser = new UserData("user", "pass", "123@scam.edu");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void clear() throws ResponseException {
        String auth = facade.register(testUser);
        facade.createGame("testGame", auth);

    }

    @Test
    void register() {

    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void listGames() {
    }

    @Test
    void createGame() {
    }

    @Test
    void joinGame() {
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
