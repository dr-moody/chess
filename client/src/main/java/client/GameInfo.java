package client;

import chess.ChessGame;

public record GameInfo(int gameID, ChessGame.TeamColor playerColor) {
}
