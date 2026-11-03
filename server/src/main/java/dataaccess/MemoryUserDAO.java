package dataaccess;

import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        userDataList.add(new UserData(user.username(), BCrypt.hashpw(user.password(), BCrypt.gensalt()), user.email()));
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
