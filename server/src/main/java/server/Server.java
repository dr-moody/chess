package server;

import chess.ChessGame;
import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.GameData;
import model.UserData;
import org.jetbrains.annotations.NotNull;
import service.Service;
import service.ServiceResponse;

import java.util.ArrayList;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    
    private Service service;

    public Server() {

        this.service = new Service("Memory");

        javalin = Javalin.create(config -> config.staticFiles.add("web"))

                .delete("/db", this::clear)
                .post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)

                ;
    }

    private void joinGame(Context ctx) {
        String authToken = ctx.header("authorization");
        Map<String, String> bodyString = new Gson().fromJson(ctx.body(), Map.class);
        try {
            ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(bodyString.get("playerColor"));
            Map<String, Double> bodyDouble = new Gson().fromJson(ctx.body(), Map.class);
            int gameID = bodyDouble.get("gameID").intValue();

            ServiceResponse response = service.joinGame(authToken, color, gameID);
            ctx.status(Integer.parseInt(response.get("status code"))).result(new Gson().toJson(response.body));
        }
        catch (Exception e) {
            ctx.status(400).result(new Gson().toJson(new ServiceResponse("message:Error bad request").body));
        }

    }

    private void createGame(Context ctx) {
        String authToken = ctx.header("authorization");
        Map<String, String> body = new Gson().fromJson(ctx.body(), Map.class);
        String gameName = body.get("gameName");

        ServiceResponse response = service.createGame(authToken, gameName);
        ctx.status(Integer.parseInt(response.get("status code"))).result(new Gson().toJson(response.body));
    }

    private void listGames(Context ctx) {
        String authToken = ctx.header("authorization");
        ServiceResponse response = service.listGames(authToken);

        ArrayList<GameData> gamesList = new Gson().fromJson(response.get("games"), ArrayList.class);
        Map<String, ArrayList<GameData>> gamesMap = Map.of("games", gamesList);

        ctx.status(Integer.parseInt(response.get("status code"))).result(new Gson().toJson(gamesMap));
    }

    private void logout(Context ctx) {
        String authToken = ctx.header("authorization");
        ServiceResponse response = service.logout(authToken);
        ctx.status(Integer.parseInt(response.get("status code"))).result(new Gson().toJson(response.body));
    }

    private void login(Context ctx) {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        ServiceResponse response = service.login(user);
        ctx.status(Integer.parseInt(response.get("status code"))).result(new Gson().toJson(response.body));
    }

    private void register(Context ctx) {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        ServiceResponse response = service.register(user);
        ctx.status(Integer.parseInt(response.get("status code"))).result(new Gson().toJson(response.body));
    }

    private void clear(Context ctx) {
        ctx.status(Integer.parseInt(service.clear().get("status code")));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
    
    
}
