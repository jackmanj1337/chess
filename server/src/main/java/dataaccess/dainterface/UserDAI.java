package dataaccess.dainterface;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDAI {

    UserData createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void deleteUser(String username) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;
}
