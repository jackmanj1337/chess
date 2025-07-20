package serverfacade;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import serverfacade.results.*;
import serverfacade.requests.*;

public class ServerFacade {
    String urlBase;
    Gson json = new Gson();

    public ServerFacade(String urlBase) {
        this.urlBase = urlBase;
    }

    public RegisterResult register(RegisterRequest req) {
        try {
            String body = json.toJson(req);

            StringBuilder response = requestMaker("/user", "POST", null, body);

            return json.fromJson(response.toString(), RegisterResult.class);

        } catch (IOException e) {
            return new RegisterResult(500, "Error: " + e.getMessage(), null, null);
        }
    }

    public LoginResult login(LoginRequest req) {
        try {
            String body = json.toJson(req);

            StringBuilder response = requestMaker("/session", "POST", null, body);

            return json.fromJson(response.toString(), LoginResult.class);
        } catch (IOException e) {
            return new LoginResult(500, "Error: " + e.getMessage(), null, null);
        }
    }

    public LogoutResult logout(LogoutRequest req) {
        try {
            String body = json.toJson(req);

            StringBuilder response = requestMaker("/session", "DELETE", req.authToken(), body);

            return json.fromJson(response.toString(), LogoutResult.class);
        } catch (IOException e) {
            return new LogoutResult(500, "Error: " + e.getMessage());
        }
    }

    public CreateGameResult createGame(CreateGameRequest req) {
        try {
            String body = json.toJson(req);

            StringBuilder response = requestMaker("/game", "POST", req.authToken(), body);

            return json.fromJson(response.toString(), CreateGameResult.class);
        } catch (IOException e) {
            return new CreateGameResult(500, "Error: " + e.getMessage(), null);
        }
    }

    public ListGamesResult listGames(ListGamesRequest req) {
        try {
            String body = json.toJson(req);

            StringBuilder response = requestMaker("/game", "GET", req.authToken(), body);

            return json.fromJson(response.toString(), ListGamesResult.class);

        } catch (IOException e) {
            return new ListGamesResult(500, "Error: " + e.getMessage(), null);
        }
    }

    public JoinGameResult joinGame(JoinGameRequest req) {
        try {
            String body = json.toJson(req);

            StringBuilder response = requestMaker("/game", "PUT", req.authToken(), body);

            return json.fromJson(response.toString(), JoinGameResult.class);

        } catch (IOException e) {
            return new JoinGameResult(500, "Error: " + e.getMessage());
        }
    }


    private StringBuilder requestMaker(String path, String method, String auth, String body) throws IOException {
        URL url = new URL(urlBase + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", auth);


        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        System.out.println("Status Code: " + status);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        connection.disconnect();

        System.out.println(response);
        return response;
    }
}
