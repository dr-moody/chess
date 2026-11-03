package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.*;

public class SQLGameDAO implements GameDAO {

    @Override
    public void clear() {
        DatabaseManager.execute("DELETE FROM game");
    }

    @Override
    public boolean createGame(GameData game) {
        if (null != DatabaseManager.selectOne("SELECT * FROM game WHERE id = " + game.gameID())) {
            String gameJson = new Gson().toJson(game);
            ArrayList<Object> values = new ArrayList<>(List.of(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), gameJson));
            ArrayList<String> stringValues = new ArrayList<>();

            for (Object value : values) {stringValues.add(value.toString());}

            DatabaseManager.executeWithValues("INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)", stringValues);
        }
        return false;
    }

    @Override
    public boolean updateGame(GameData game) {
        if (null == DatabaseManager.selectOne("SELECT * FROM game WHERE gameID = " + game.gameID())) {
            return false;
        }
        DatabaseManager.execute("DELETE FROM game WHERE id = " + game.gameID());
        return createGame(game);
    }

    @Override
    public GameData getGame(int gameID) {
        HashMap<String, String> gameMap = DatabaseManager.selectOne("SELECT * FROM game WHERE gameID = " + gameID);
        if (gameMap != null) {
            ChessGame game = new Gson().fromJson(gameMap.get("game"), ChessGame.class);
            return new GameData(gameID, gameMap.get("whiteUsername"), gameMap.get("blackUsername"), gameMap.get("gameName"), game);
        }
        return null;
    }

    @Override
    public Collection<GameData> getAllGames() {
        HashSet<GameData> gamesList = new HashSet<>();
        ArrayList<HashMap<String, String>> mapList = DatabaseManager.selectAll("SELECT * FROM game");

        for (HashMap<String, String> gameMap : mapList) {
            ChessGame game = new Gson().fromJson(gameMap.get("game"), ChessGame.class);
            gamesList.add(new GameData(Integer.parseInt(gameMap.get("gameID")), gameMap.get("whiteUsername"), gameMap.get("blackUsername"), gameMap.get("gameName"), game));
        }

        return gamesList;
    }
}
