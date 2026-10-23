package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{
    ArrayList<AuthData> authDataList = new ArrayList<>();

    @Override
    public void clear() {
        authDataList.clear();
    }

    @Override
    public void createAuth(AuthData auth) {
        authDataList.add(auth);
    }

    @Override
    public boolean deleteAuth(String authToken) {
        for (int i = 0; i < authDataList.size(); i++) {
            if (Objects.equals(authDataList.get(i).authToken(), authToken)) {
                authDataList.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public String verifyAuth(String authToken) {
        for (AuthData auth : authDataList) {
            if (Objects.equals(auth.authToken(), authToken)) {
                return auth.username();
            }
        }
        return null;
    }
}
