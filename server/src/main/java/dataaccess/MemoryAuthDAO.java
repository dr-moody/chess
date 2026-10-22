package dataaccess;

import model.AuthData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{
    HashSet<AuthData> authDataList = new HashSet<>();

    @Override
    public void clear() {
        authDataList.clear();
    }

    @Override
    public void createAuth(AuthData auth) {
        authDataList.add(auth);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (authDataList.stream().anyMatch(a -> Objects.equals(a.authToken(), authToken))) {
            authDataList.removeIf(u -> Objects.equals(u.authToken(), authToken));
        }

        throw new DataAccessException("authToken not found");
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
