package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.*;

public class SQLGameDAO implements GameDAO {

    @Override
    public void clear() throws DataAccessException {
        DatabaseManager.execute("DELETE FROM game");
    }

    @Override
    public boolean createGame(GameData game) throws DataAccessException {
        if (null == DatabaseManager.selectOne("SELECT * FROM game WHERE gameID = " + game.gameID())) {
            String gameJson = new Gson().toJson(game);
            ArrayList<String> values = new ArrayList<>();
            values.add(String.valueOf(game.gameID()));
            values.add(game.whiteUsername());
            values.add(game.blackUsername());
            values.add(game.gameName());
            values.add(gameJson);

            DatabaseManager.executeWithValues("INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)", values);
        }
        return false;
    }

    @Override
    public boolean updateGame(GameData game) throws DataAccessException {
        if (null == DatabaseManager.selectOne("SELECT * FROM game WHERE gameID = " + game.gameID())) {
            return false;
        }
        DatabaseManager.execute("DELETE FROM game WHERE gameID = " + game.gameID());
        return createGame(game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        HashMap<String, String> gameMap = DatabaseManager.selectOne("SELECT * FROM game WHERE gameID = " + gameID);
        if (gameMap != null) {
            ChessGame game = new Gson().fromJson(gameMap.get("game"), ChessGame.class);
            return new GameData(gameID, gameMap.get("whiteUsername"), gameMap.get("blackUsername"), gameMap.get("gameName"), game);
        }
        return null;
    }

    @Override
    public Collection<GameData> getAllGames() throws DataAccessException {
        HashSet<GameData> gamesList = new HashSet<>();
        ArrayList<HashMap<String, String>> mapList = DatabaseManager.selectAll("SELECT * FROM game");

        for (HashMap<String, String> gameMap : mapList) {
            if (gameMap == null) {
                continue;
            }
            ChessGame game = new Gson().fromJson(gameMap.get("game"), ChessGame.class);
            gamesList.add(new GameData(Integer.parseInt(gameMap.get("gameID")), gameMap.get("whiteUsername"), gameMap.get("blackUsername"), gameMap.get("gameName"), game));
        }

        return gamesList;
    }
}
