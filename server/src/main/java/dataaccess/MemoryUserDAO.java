package dataaccess;

import model.GameData;
import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    HashSet<UserData> userDataList = new HashSet<>();

    @Override
    public void clear() {
        userDataList.clear();
    }

    @Override
    public void createUser(UserData user) {
        userDataList.add(user);
    }

    @Override
    public UserData getUser(String username) {
        for (UserData u : userDataList) {
            if (Objects.equals(u.username(), username)) {
                return u;
            }
        }
        return null;
    }
}
