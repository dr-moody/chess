package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public void clear() {

    }

    @Override
    public void createAuth(AuthData auth) {

    }

    @Override
    public boolean deleteAuth(String authToken) {
        return false;
    }

    @Override
    public String verifyAuth(String authToken) {
        return "";
    }
}
