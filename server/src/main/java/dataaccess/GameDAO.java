package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;
    boolean createGame(GameData game) throws DataAccessException;
    boolean updateGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> getAllGames() throws DataAccessException;
}
