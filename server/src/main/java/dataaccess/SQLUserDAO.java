package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLUserDAO implements UserDAO {
    @Override
    public void clear() throws DataAccessException {
        DatabaseManager.execute("DELETE FROM users");
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (null == DatabaseManager.selectOne("SELECT * FROM users WHERE username = '" + user.username() + "'")) {
            ArrayList<Object> values = new ArrayList<>(List.of(user.username(), user.email(), user.password()));
            ArrayList<String> stringValues = new ArrayList<>();

            for (Object value : values) {stringValues.add(value.toString());}

            DatabaseManager.executeWithValues("INSERT INTO users (username, email, password) VALUES (?, ?, ?)", stringValues);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        HashMap<String, String> userMap = DatabaseManager.selectOne("SELECT * FROM users WHERE username = '" + username +"'");
        if (userMap != null && !userMap.isEmpty()) {
            return new UserData(userMap.get("username"), userMap.get("password"), userMap.get("email"));
        }
        return null;
    }
}
