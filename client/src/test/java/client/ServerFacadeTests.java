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

//    Clear test not needed since it is called in BeforeEach,
//    also we can't check it using listGames because our authToken gets deleted

    @Test
    void register() throws ResponseException {
        String auth = facade.register(testUser);
        assertNotNull(auth);
    }

    @Test
    void login() throws ResponseException {
        String auth = facade.register(testUser);
        facade.logout(auth);
        String loginAuth = facade.login(testUser);
        assertNotNull(loginAuth);
    }

//    Logout gets tested during login

    @Test
    void listGames() throws ResponseException {
        String auth = facade.register(testUser);
        facade.createGame("test1", auth);
        facade.createGame("test2", auth);
        facade.createGame("test3", auth);
        assert(facade.listGames(auth).size() == 3);
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
