package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    ArrayList<GameData> gameDataList = new ArrayList<>();

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
        for (int i = 0; i <= gameDataList.size(); i++) {
            if (gameDataList.get(i).gameID() == game.gameID()) {
                gameDataList.set(i, game);
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
