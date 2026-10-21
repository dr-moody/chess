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
    public void createGame(GameData game) throws DataAccessException {
        for (GameData g : gameDataList) {
            if (g.gameID() == game.gameID()) {
                throw new DataAccessException("game already exists");
            }
        }
        gameDataList.add(game);
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        for (GameData g : gameDataList) {
            if (g.gameID() == game.gameID()) {
                g = game;
                break;
            }
        }
        throw new DataAccessException("game not found");
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData g : gameDataList) {
            if (g.gameID() == gameID) {
                return g;
            }
        }
        throw new DataAccessException("game not found");
    }

    @Override
    public Collection<GameData> getAllGames() {
        return gameDataList;
    }
}
