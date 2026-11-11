package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void listGames() throws ResponseException {
        String auth = facade.register(testUser);
        facade.createGame("test1", auth);
        facade.createGame("test2", auth);
        facade.createGame("test3", auth);
        assert(facade.listGames(auth).size() == 3);
    }

    @Test
    void joinGame() throws ResponseException {
        String auth = facade.register(testUser);
        int gameID = facade.createGame("test1", auth);
        facade.joinGame(gameID, ChessGame.TeamColor.WHITE, auth);
        GameData game = facade.listGames(auth).getFirst();
        assertEquals(game.whiteUsername(), testUser.username());
    }

}
