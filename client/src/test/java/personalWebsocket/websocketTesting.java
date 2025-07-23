package personalWebsocket;

import dataaccess.DAOManager;
import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.GameDAI;
import dataaccess.dainterface.UserDAI;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import serverfacade.ServerFacade;
import serverfacade.WebsocketFacade;
import utilities.ServerConnectionSettings;

import java.net.URI;

public class websocketTesting {

    private static Server server;
    private static ServerFacade facade;
    UserDAI users = DAOManager.users;
    AuthDAI auths = DAOManager.auths;
    GameDAI games = DAOManager.games;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @Test
    public void establishWebsocketConnection() {

        WebSocketClient client = new WebSocketClient();
        WebsocketFacade socket = new WebsocketFacade("badToken", 1);

        try {
            client.start();
            URI uri = new URI("ws://" + ServerConnectionSettings.SERVER_LOCATION + "/ws");
            client.connect(socket, uri).get();


            Thread.sleep(60000); // 1 min

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
