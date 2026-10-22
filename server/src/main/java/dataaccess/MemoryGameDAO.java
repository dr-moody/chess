package dataaccess;

import model.GameData;

import java.time.DateTimeException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    HashSet<GameData> gameDataList = new HashSet<>();

    @Override
    public void clear() {
        gameDataList.clear();
    }

    @Override
    public boolean createGame(GameData game) {
        for (GameData g : gameDataList) {
            if (g.gameID() == game.gameID()) {
                return false;
            }
        }
        gameDataList.add(game);
        return true;
    }

    @Override
    public boolean updateGame(GameData game) {
        for (GameData g : gameDataList) {
            if (g.gameID() == game.gameID()) {
                g = game;
                return true;
            }
        }
        return false;
    }

    @Override
    public GameData getGame(int gameID) {
        for (GameData g : gameDataList) {
            if (g.gameID() == gameID) {
                return g;
            }
        }
        return null;
    }

    @Override
    public Collection<GameData> getAllGames() {
        return gameDataList;
    }
}
