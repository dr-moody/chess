package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLUserDAO implements UserDAO {
    @Override
    public void clear() { DatabaseManager.execute("DELETE FROM users"); }

    @Override
    public void createUser(UserData user) {
        if (null == DatabaseManager.selectOne("SELECT * FROM users WHERE username = '" + user.username() + "'")) {
            ArrayList<Object> values = new ArrayList<>(List.of(user.username(), user.email(), user.password()));
            ArrayList<String> stringValues = new ArrayList<>();

            for (Object value : values) {stringValues.add(value.toString());}

            DatabaseManager.executeWithValues("INSERT INTO users (username, email, password) VALUES (?, ?, ?)", stringValues);
        }
    }

    @Override
    public UserData getUser(String username) {
        HashMap<String, String> userMap = DatabaseManager.selectOne("SELECT * FROM users WHERE username = '" + username +"'");
        if (userMap != null) {
            return new UserData(userMap.get("username"), userMap.get("email"), userMap.get("password"));
        }
        return null;
    }
}
