package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class Service {
    AuthDAO serviceAuthDAO;
    GameDAO serviceGameDAO;
    UserDAO serviceUserDAO;

    public Service (String DAOType) {
        if (Objects.equals(DAOType, "Memory")) {
            serviceAuthDAO = new MemoryAuthDAO();
            serviceGameDAO = new MemoryGameDAO();
            serviceUserDAO = new MemoryUserDAO();

        } else if (Objects.equals(DAOType, "SQL")) {
            serviceAuthDAO = null;
            serviceGameDAO = null;
            serviceUserDAO = null;
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void clear() {
        serviceAuthDAO.clear();
        serviceUserDAO.clear();
        serviceGameDAO.clear();
    }

    public ServiceResponse Register(UserData RegisterRequest) {
        try {
            if (serviceUserDAO.getUser(RegisterRequest.username()) != null) {
                return new ServiceResponse("status code:403,message:Error: already taken");
            }
        } catch (Exception e) {
            serviceUserDAO.createUser(RegisterRequest);
            String authToken = generateToken();
            serviceAuthDAO.createAuth(new AuthData(authToken, RegisterRequest.username()));
            return new ServiceResponse("status code:200,username:"+RegisterRequest.username()+"authToken:"+authToken);
        }
        return null;
    }
}
