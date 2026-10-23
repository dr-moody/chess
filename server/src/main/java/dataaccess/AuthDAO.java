package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData auth);
    boolean deleteAuth(String authToken);
    String verifyAuth(String authToken);
}
