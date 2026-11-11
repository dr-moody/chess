package client;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @BeforeEach
    public void reset() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void clear() throws ResponseException {
        String auth = facade.register(testUser);
        facade.createGame("testGame", auth);
        facade.clear();
        assert(facade.listGames(auth).isEmpty());
    }

    @Test
    void register() throws ResponseException {
        String auth = facade.register(testUser);
        assertNotNull(auth);
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
