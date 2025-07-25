package service;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DAOManager;
import dataaccess.DataAccessException;
import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.GameDAI;
import handlers.PlayerSession;
import model.GameData;
import server.WebsocketServer;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Objects;

import static server.WebsocketServer.broadcastToGame;
import static server.WebsocketServer.sendToPlayer;


public class WebSocketService {
    static final GameDAI GAMES = DAOManager.games;
    static final AuthDAI AUTHS = DAOManager.auths;
    static final Gson GSON = new Gson();

    public static void handleConnect(PlayerSession playerSession) {
        try {
            if (verifyAuth(playerSession)) {
                WebsocketServer.connectUserToGame(playerSession);
                String connected = AUTHS.getAuthFromToken(playerSession.authToken()).username();
                GameData gameData = GAMES.getGame(playerSession.gameID());
                if (gameData == null) {
                    sendToPlayer(playerSession, ServerMessage.newErrorMessage("Your game ID was corrupted"));
                    return;
                }
                String status;
                if (Objects.equals(connected, gameData.whiteUsername())) {
                    status = "<WHITE>";
                } else if (Objects.equals(connected, gameData.blackUsername())) {
                    status = "<BLACK>";
                } else {
                    status = "<OBSERVER>";
                }

                ServerMessage newConnection = ServerMessage.newNotification(connected + " joined as " + status);
                broadcastToGame(playerSession.gameID(), newConnection, playerSession);
                sendToPlayer(playerSession, ServerMessage.newLoadGame(gameData));
            } else {
                sendToPlayer(playerSession, ServerMessage.newErrorMessage("Your authentication was corrupted"));
                return;
            }
        } catch (DataAccessException e) {
            System.out.println("Error: " + e);
            sendDBConnectionFailedError(playerSession, e);
        }
    }

    private static void sendDBConnectionFailedError(PlayerSession playerSession, DataAccessException e) {
        try {
            ServerMessage connectionFailedError = ServerMessage.newErrorMessage(e.getMessage());
            playerSession.session().getRemote().sendString(GSON.toJson(connectionFailedError));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public static void handleMakeMove(UserGameCommand request, PlayerSession playerSession) {
        try {
            GameData gameData = GAMES.getGame(request.getGameID());
            ChessGame game = gameData.game();
            if (game.getWinner() != null) {
                sendToPlayer(playerSession, ServerMessage.newErrorMessage("The game is over."));
                return;
            }

            if (verifyAuth(playerSession) && playerIsColor(playerSession,
                    game.getBoard().getPiece(request.getMove().getStartPosition()).getTeamColor())) {
                try {
                    String sendingPlayerName = AUTHS.getAuthFromToken(playerSession.authToken()).username();
                    String movingTeam = "<" + game.getTeamTurn().toString() + ">";
                    game.makeMove(request.getMove());

                    GameData data = new GameData(
                            gameData.gameID(),
                            gameData.whiteUsername(),
                            gameData.blackUsername(),
                            gameData.gameName(),
                            game);

                    GAMES.updateGameData(data);

                    String newMoveMessage = sendingPlayerName + movingTeam +
                            " made the move " + request.getMove().toDisplayString();
                    WebsocketServer.broadcastToGame(
                            request.getGameID(),
                            ServerMessage.newNotification(newMoveMessage),
                            playerSession);

                    WebsocketServer.broadcastToGame(request.getGameID(), ServerMessage.newLoadGame(data), null);
                    ChessGame.TeamColor activeTeam = game.getTeamTurn();
                    String activePlayer;
                    if (activeTeam == ChessGame.TeamColor.WHITE) {
                        activePlayer = gameData.whiteUsername();
                    } else {
                        activePlayer = gameData.blackUsername();
                    }

                    if (game.isInCheckmate(activeTeam)) {
                        String message = activePlayer + "<" + activeTeam + "> has been put in checkmate.";
                        WebsocketServer.broadcastToGame(request.getGameID(), ServerMessage.newNotification(message), null);
                        return;
                    }
                    if (game.isInCheck(activeTeam)) {
                        String message = activePlayer + "<" + activeTeam + "> has been put in check.";
                        WebsocketServer.broadcastToGame(request.getGameID(), ServerMessage.newNotification(message), null);
                    }
                    if (game.isInStalemate(activeTeam)) {
                        String message = activePlayer + "<" + activeTeam + "> has been put in stalemate.";
                        WebsocketServer.broadcastToGame(request.getGameID(), ServerMessage.newNotification(message), null);
                    }
                } catch (InvalidMoveException e) {
                    sendToPlayer(playerSession, ServerMessage.newErrorMessage(e.getMessage()));
                }
            } else {
                sendToPlayer(playerSession, ServerMessage.newErrorMessage("You don't have authority to make that move"));
            }
        } catch (DataAccessException e) {
            System.out.println("Error: " + e);
            sendDBConnectionFailedError(playerSession, e);
        }
    }

    public static void handleResign(PlayerSession playerSession) {
        System.out.println("called Handle resign");
        try {
            if (!verifyAuth(playerSession)) {
                sendToPlayer(playerSession, ServerMessage.newErrorMessage("Invalid authentication."));
                return;
            }

            boolean isWhite = playerIsColor(playerSession, ChessGame.TeamColor.WHITE);
            boolean isBlack = playerIsColor(playerSession, ChessGame.TeamColor.BLACK);

            if (!isWhite && !isBlack) {
                sendToPlayer(playerSession, ServerMessage.newErrorMessage("Observers cannot resign."));
                return;
            }

            String loser = AUTHS.getAuthFromToken(playerSession.authToken()).username();
            GameData gameData = GAMES.getGame(playerSession.gameID());

            if (gameData.game().getWinner() != null) {
                sendToPlayer(playerSession, ServerMessage.newErrorMessage("The game has already been decided"));
                return;
            }

            String winner = isWhite ? gameData.blackUsername() : gameData.whiteUsername();
            if (winner == null) {
                winner = "<NO_PLAYER_FOUND>";
            }

            String message = loser + " has conceded the game to " + winner;
            broadcastToGame(playerSession.gameID(), ServerMessage.newNotification(message), null);

            ChessGame endedGame = gameData.game();
            endedGame.setWinner(winner);

            GameData updated = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    endedGame);

            GAMES.updateGameData(updated);

        } catch (DataAccessException e) {
            System.out.println("Error: " + e);
            sendDBConnectionFailedError(playerSession, e);
        }
    }


    public static void handleLeave(PlayerSession playerSession) {
        WebsocketServer.removeUserFromGame(playerSession);
    }


    private static boolean verifyAuth(PlayerSession playerSession) throws DataAccessException {
        return Objects.nonNull(AUTHS.getAuthFromToken(playerSession.authToken()));
    }

    private static boolean playerIsColor(PlayerSession playerSession, ChessGame.TeamColor color) throws DataAccessException {
        String username = AUTHS.getAuthFromToken(playerSession.authToken()).username();
        GameData game = GAMES.getGame(playerSession.gameID());
        if (color == ChessGame.TeamColor.WHITE) {
            if (Objects.equals(game.whiteUsername(), username)) {
                return true;
            }
        }
        if (color == ChessGame.TeamColor.BLACK) {
            if (Objects.equals(game.blackUsername(), username)) {
                return true;
            }
        }
        return false;
    }


}
