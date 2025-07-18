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

    public RegisterResult register(RegisterRequest regReq) {
        try {
            URL url = new URL(urlBase + "/user");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String body = json.toJson(regReq);

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

            return json.fromJson(response.toString(), RegisterResult.class);

        } catch (IOException e) {
            return new RegisterResult(500, "Error: " + e.getMessage(), null, null);
        }
    }

    public LoginResult login(LoginRequest linReq) {
        try {
            URL url = new URL(urlBase + "/session");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String body = json.toJson(linReq);

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

            return json.fromJson(response.toString(), LoginResult.class);

        } catch (IOException e) {
            return new LoginResult(500, "Error: " + e.getMessage(), null, null);
        }
    }

    public LogoutResult logout(String token) {
        return null;
    }

    public ClearDBResult clearDB() {
        return null;
    }

    public CreateGameResult createGame() {
        return null;
    }

    public ListGamesResult listGames() {
        return null;
    }

    public JoinGameResult joinGame() {
        return null;
    }
}
