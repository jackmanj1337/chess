package dataaccess;

import dataaccess.dainterface.AuthDAI;
import dataaccess.*;
import dataaccess.dainterface.GameDAI;
import dataaccess.dainterface.UserDAI;
import dataaccess.localstorage.AuthDAO;
import dataaccess.localstorage.GameDAO;
import dataaccess.localstorage.UserDAO;

public class DAOManager {
    public static AuthDAI auths = new AuthDAO();
    public static UserDAI users = new UserDAO();
    public static GameDAI games = new GameDAO();
}
