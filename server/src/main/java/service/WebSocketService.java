package service;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DAOManager;
import dataaccess.DataAccessException;
import dataaccess.dainterface.AuthDAI;
import dataaccess.dainterface.GameDAI;
import dataaccess.dainterface.UserDAI;
import handlers.PlayerSession;
import model.GameData;
import server.WebsocketServer;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Objects;

import static server.WebsocketServer.broadcastToGame;
import static server.WebsocketServer.sendToPlayer;


public class WebSocketService {
    static final GameDAI games = DAOManager.games;
    static final AuthDAI auths = DAOManager.auths;
    static final UserDAI users = DAOManager.users;
    static final Gson GSON = new Gson();

    public static void handleConnect(PlayerSession playerSession) {
        try {
            if (verifyAuth(playerSession)) {
                WebsocketServer.connectUserToGame(playerSession);
                String connected = auths.getAuthFromToken(playerSession.authToken()).username();
                GameData gameData = games.getGame(playerSession.gameID());
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
            }
        } catch (DataAccessException e) {
            System.out.println("Error: " + e);
            try {
                ServerMessage connectionFailedError = ServerMessage.newErrorMessage(e.getMessage());
                playerSession.session().getRemote().sendString(GSON.toJson(connectionFailedError));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    public static void handleMakeMove(UserGameCommand request, PlayerSession playerSession) {
        try {
            GameData gameData = games.getGame(request.getGameID());
            ChessGame game = gameData.game();
            if (game.getWinner() != null) {
                sendToPlayer(playerSession, ServerMessage.newErrorMessage("The game is over."));
                return;
            }

            if (verifyAuth(playerSession) && playerIsColor(playerSession,
                    game.getBoard().getPiece(request.getMove().getStartPosition()).getTeamColor())) {
                try {
                    String sendingPlayerName = auths.getAuthFromToken(playerSession.authToken()).username();
                    String movingTeam = "<" + game.getTeamTurn().toString() + ">";
                    game.makeMove(request.getMove());

                    GameData data = new GameData(
                            gameData.gameID(),
                            gameData.whiteUsername(),
                            gameData.blackUsername(),
                            gameData.gameName(),
                            game);

                    games.updateGameData(data);

                    String newMoveMessage = sendingPlayerName + movingTeam +
                            "made the move " + request.getMove().toDisplayString();
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
            try {
                ServerMessage connectionFailedError = ServerMessage.newErrorMessage(e.getMessage());
                playerSession.session().getRemote().sendString(GSON.toJson(connectionFailedError));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
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

            String loser = auths.getAuthFromToken(playerSession.authToken()).username();
            GameData gameData = games.getGame(playerSession.gameID());

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

            games.updateGameData(updated);

        } catch (DataAccessException e) {
            System.out.println("Error: " + e);
            try {
                ServerMessage connectionFailedError = ServerMessage.newErrorMessage(e.getMessage());
                playerSession.session().getRemote().sendString(GSON.toJson(connectionFailedError));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    public static void handleLeave(PlayerSession playerSession) {
        try {
            if (verifyAuth(playerSession)) {
                String user = auths.getAuthFromToken(playerSession.authToken()).username();
                ServerMessage leaveNotification = ServerMessage.newNotification(user + " has left the game");
                broadcastToGame(playerSession.gameID(), leaveNotification, playerSession);
                WebsocketServer.removeUserFromGame(playerSession);
            }
        } catch (DataAccessException e) {
            System.out.println("Error: " + e);
            try {
                ServerMessage connectionFailedError = ServerMessage.newErrorMessage(e.getMessage());
                playerSession.session().getRemote().sendString(GSON.toJson(connectionFailedError));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private static boolean verifyAuth(PlayerSession playerSession) throws DataAccessException {
        return Objects.nonNull(auths.getAuthFromToken(playerSession.authToken()));
    }

    private static boolean playerIsColor(PlayerSession playerSession, ChessGame.TeamColor color) throws DataAccessException {
        String username = auths.getAuthFromToken(playerSession.authToken()).username();
        GameData game = games.getGame(playerSession.gameID());
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
