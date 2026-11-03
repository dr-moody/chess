package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
    boolean deleteAuth(String authToken) throws DataAccessException;
    String verifyAuth(String authToken) throws DataAccessException;
}
