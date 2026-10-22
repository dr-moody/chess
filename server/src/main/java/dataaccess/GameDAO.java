package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear();
    boolean createGame(GameData game);
    boolean updateGame(GameData game);
    GameData getGame(int gameID);
    Collection<GameData> getAllGames();
}
