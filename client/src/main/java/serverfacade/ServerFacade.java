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
            System.out.println(e.getMessage());
            return new RegisterResult(501, "Error: " + e.getMessage(), null, null);
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

            StringBuilder response = requestMaker("/session", "DELETE", req.authToken(), null);

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

            StringBuilder response = requestMaker("/game", "GET", req.authToken(), null);

            return json.fromJson(response.toString(), ListGamesResult.class);

        } catch (IOException e) {
            return new ListGamesResult(500, "Error: " + e.getMessage(), null);
        }
    }

    public ClearDBResult clearDB() {
        try {
            StringBuilder response = requestMaker("/db", "DELETE", null, null);

            return json.fromJson(response.toString(), ClearDBResult.class);

        } catch (IOException e) {
            return new ClearDBResult(500, "Error: " + e.getMessage());
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
        if (auth != null) {
            connection.setRequestProperty("Authorization", auth);
        }

        if (body != null && !body.isEmpty()) {
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        int status = connection.getResponseCode();
        InputStreamReader reader;
        if (status >= 400) {
            reader = new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8);
        } else {
            reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(reader)) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        connection.disconnect();
        return response;
    }
}
