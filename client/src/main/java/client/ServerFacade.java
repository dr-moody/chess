package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Objects;

public class ServerFacade {
      private final HttpClient client = HttpClient.newHttpClient();
      private final String serverUrl;

      public ServerFacade(String url){
            serverUrl = url;
      }

//    clear
    public void clear () throws ResponseException {
          var request = buildRequest("DELETE", "/db", null, null);
          sendRequest(request);
    }

//    register
    public String register (UserData user) throws ResponseException {
          var request = buildRequest("POST", "/user", user, null);
          var response = sendRequest(request);
          return handleResponse(response, "authToken");
    }

//    login
    public String login (UserData user) throws ResponseException {
        var request = buildRequest("POST", "/session", user, null);
        var response = sendRequest(request);
        return handleResponse(response, "authToken");
    }

//    logout
    public void logout (String auth) throws ResponseException {
        var request = buildRequest("DELETE", "/session", null, new Header("authorization", auth));
        sendRequest(request);
    }

//    list games
    public ArrayList<GameData> listGames (String auth) throws ResponseException {
        var request = buildRequest("GET", "/game", null, new Header("authorization", auth));
        var response = sendRequest(request);
        return handleListResponse(response);
    }

//    create game
    public int createGame(String gameName, String auth) throws ResponseException {
        var request = buildRequest("POST", "/game", new GameName(gameName), new Header("authorization", auth));
        var response = sendRequest(request);
        return Integer.parseInt(Objects.requireNonNull(handleResponse(response, "gameID")));

    }

//    join game
    public boolean joinGame(int gameID, ChessGame.TeamColor color, String auth) throws ResponseException {
        var request = buildRequest("PUT", "/game", new GameInfo(gameID, color), new Header("authorization", auth));
        return isSuccessful(sendRequest(request).statusCode());
    }


    private HttpRequest buildRequest(String method, String path, Object body, Header head) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (head != null) {
            request.header(head.name(), head.value());
        }
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private String handleResponse(HttpResponse<String> response, String field) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        return json.get(field).getAsString();

    }

    private ArrayList<GameData> handleListResponse(HttpResponse<String> response) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray jsonGames = json.get("games").getAsJsonArray();

        Type listType = new TypeToken<ArrayList<GameData>>() {}.getType();

        return new Gson().fromJson(jsonGames, listType);
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
