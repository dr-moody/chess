package dataaccess;

import model.UserData;

public interface UserDAO {
    void clear();
    void deleteUser(String username) throws DataAccessException;
    void createUser(UserData user);
    UserData getUser(String username);

}
