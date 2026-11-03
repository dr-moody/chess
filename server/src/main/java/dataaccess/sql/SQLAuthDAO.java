package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public void clear() throws DataAccessException {
        DatabaseManager.execute("DELETE FROM auth");
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        ArrayList<String> stringValues = new ArrayList<>(List.of(auth.authToken(), auth.username()));
        DatabaseManager.executeWithValues("INSERT INTO auth (authtoken, username) VALUES (?, ?)", stringValues);
    }

    @Override
    public boolean deleteAuth(String authToken) throws DataAccessException {
        if (null == DatabaseManager.selectOne("SELECT * FROM auth WHERE authtoken = '" + authToken + "'")) {
            return false;
        }
        DatabaseManager.execute("DELETE FROM auth WHERE authtoken = '" + authToken + "'");
        return true;
    }

    @Override
    public String verifyAuth(String authToken) throws DataAccessException {
        HashMap<String, String> authMap = DatabaseManager.selectOne("SELECT * FROM auth WHERE authtoken = '" + authToken + "'");
        if (authMap == null) {
            return null;
        }
        return authMap.get("username");
    }
}
