package dataaccess.localstorage;

import dataaccess.DataAccessException;
import dataaccess.dainterface.UserDAI;
import model.UserData;

import java.util.ArrayList;

public class UserDAOLocal implements UserDAI {

    static ArrayList<UserData> users = new ArrayList<>();

    public UserDAOLocal() {
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
    public void deleteAllUsers() throws DataAccessException {
        users.clear();
    }
}
