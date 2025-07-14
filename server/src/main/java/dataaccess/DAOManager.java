package dataaccess;

import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.GameDAI;
import dataaccess.dainterface.UserDAI;
import dataaccess.localstorage.AuthDAOLocal;
import dataaccess.localstorage.GameDAOLocal;
import dataaccess.localstorage.UserDAOLocal;
import dataaccess.mysqlimplemtation.AuthDAOSQL;
import dataaccess.mysqlimplemtation.GameDAOSQL;
import dataaccess.mysqlimplemtation.UserDAOSQL;

public class DAOManager {
    public static AuthDAI auths;

    static {
        try {
            auths = new AuthDAOSQL();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserDAI users;

    static {
        try {
            users = new UserDAOSQL();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameDAI games;

    static {
        try {
            games = new GameDAOSQL();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
