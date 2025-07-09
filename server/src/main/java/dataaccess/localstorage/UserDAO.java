package dataaccess.localstorage;

import dataaccess.DataAccessException;
import dataaccess.dainterface.UserDAI;
import model.UserData;

import java.util.ArrayList;

public class UserDAO implements UserDAI {

    static ArrayList<UserData> users = new ArrayList<>();

    public UserDAO() {
    }

    ;

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        try {
            users.add(user);
        } catch (Exception e) {
            throw new DataAccessException("oops, failed to create user");
        }
        return user;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : users) {
            if (username.equals(user.username())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        users.removeIf(user -> username.equals(user.username()));
    }

    @Override
    public void deleteAllUsers() throws DataAccessException {
        users.clear();
    }
}
