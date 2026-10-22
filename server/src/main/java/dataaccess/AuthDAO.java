package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData auth);
    void deleteAuth(String authToken) throws DataAccessException;
    String verifyAuth(String authToken);
}
