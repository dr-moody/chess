package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDao implements GameDAO {
//    Use DatabaseManager to get a connection to the database
//    and then use that connection to do stuff

//    Constructor
    public SQLGameDao () {
//        Database builder function in here?
//        Is there a better way?
        DatabaseManager.createDatabase();
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean createGame(GameData game) {
        return false;
    }

    @Override
    public boolean updateGame(GameData game) {
        return false;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> getAllGames() {
        return List.of();
    }
}
