package dataaccess;

import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.GameDAI;
import dataaccess.dainterface.UserDAI;
import dataaccess.localstorage.AuthDAOLocal;
import dataaccess.localstorage.GameDAOLocal;
import dataaccess.localstorage.UserDAOLocal;

public class DAOManager {
    public static AuthDAI auths = new AuthDAOLocal();
    public static UserDAI users = new UserDAOLocal();
    public static GameDAI games = new GameDAOLocal();
}
