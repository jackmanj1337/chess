package dataaccess.localstorage;

import dataaccess.DataAccessException;
import dataaccess.dainterface.AuthDAI;
import model.AuthData;

import java.util.ArrayList;

public class AuthDAO implements AuthDAI {
    static ArrayList<AuthData> auths = new ArrayList<>();

    public AuthDAO() {
    }

    ;

    @Override
    public AuthData addAuth(AuthData auth) throws DataAccessException {
        try {
            auths.add(auth);
        } catch (Exception e) {
            throw new DataAccessException("oops, failed to create auth");
        }
        return auth;
    }

    @Override
    public AuthData getAuthFromToken(String auth) throws DataAccessException {
        for (AuthData authdata : auths) {
            if (auth.equals(authdata.authToken())) {
                return authdata;
            }
        }
        return null;
    }

    @Override
    public AuthData getAuthFromUsername(String username) throws DataAccessException {
        for (AuthData auth : auths) {
            if (username.equals(auth.username())) {
                return auth;
            }
        }
        return null;
    }


    @Override
    public void deleteAuth(String authkey) throws DataAccessException {
        auths.removeIf(auth -> authkey.equals(auth.authToken()));
    }

    @Override
    public void deleteAllAuths() throws DataAccessException {
        auths.clear();
    }
}
