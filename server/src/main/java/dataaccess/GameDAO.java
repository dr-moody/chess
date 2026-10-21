package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear();
    void createGame(GameData game) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> getAllGames();
}
