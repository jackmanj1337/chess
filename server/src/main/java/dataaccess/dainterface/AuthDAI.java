package dataaccess.dainterface;

import dataaccess.DataAccessException;
import model.AuthData;


public interface AuthDAI {


    public AuthData addAuth(AuthData auth) throws DataAccessException;

    public AuthData getAuth(String auth) throws DataAccessException;

    public void deleteAuth(String authkey) throws DataAccessException;

    public void deleteAllAuths() throws DataAccessException;


}
