package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessPiece.PieceType;
import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
        turn = WHITE;
        currentBoard = new ChessBoard();
    }

    public ChessGame(ChessGame other) {
        this.turn = other.turn;
        this.currentBoard = new ChessBoard();
        setBoard(other.currentBoard);
    }

    private TeamColor turn;
    private ChessBoard currentBoard;

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK,
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(currentBoard, chessGame.currentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, currentBoard);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "turn=" + turn +
                ", currentBoard=" + currentBoard +
                '}';
    }

    private boolean testCandidateMove(ChessMove move) {
        ChessGame testGame = new ChessGame(this);

        ChessPiece movingPiece = testGame.getBoard().getPiece(move.getStartPosition());
        testGame.getBoard().removePiece(move.getStartPosition());
        if (move.getPromotionPiece() == null) {
            testGame.getBoard().addPiece(move.getEndPosition(), movingPiece);
        } else {
            testGame.getBoard().addPiece(move.getEndPosition(), new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece()));
        }

        System.out.print(testGame.getBoard().toString());

        return ! testGame.isInCheck(movingPiece.getTeamColor());
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (currentBoard.getPiece(startPosition) == null) {
            return null;
        }
        HashSet<ChessMove> candidateMoves = (HashSet<ChessMove>) currentBoard.getPiece(startPosition).pieceMoves(currentBoard, startPosition);
        HashSet<ChessMove> validMoves = new HashSet<ChessMove>();

        for (ChessMove move : candidateMoves) {
            System.out.print((move.toString()+"\n"));
            if (testCandidateMove(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (validMoves(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No Piece There To Move");
        }
        if (! validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid Move");
        }
        else {
            ChessPiece movingPiece = currentBoard.getPiece(move.getStartPosition());
            currentBoard.removePiece(move.getStartPosition());
            if (move.getPromotionPiece() == null) {
                currentBoard.addPiece(move.getEndPosition(), movingPiece);
            } else {
                currentBoard.addPiece(move.getEndPosition(), new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece()));
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    private ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPosition kingPosition = null;

        outer:
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece pieceToCheck = currentBoard.getPiece(new ChessPosition(i, j));
                if (pieceToCheck != null && pieceToCheck.getTeamColor() == teamColor && pieceToCheck.getPieceType() == KING) {
                    kingPosition = new ChessPosition(i, j);
                    break outer;
                }
            }
        }

        return kingPosition;
    }

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);

        if (kingPosition == null) {
            throw new RuntimeException("Missing " + teamColor.toString() + " King");
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece pieceToCheck = currentBoard.getPiece(new ChessPosition(i, j));

                // In case of pawn promotion checks
                if (pieceToCheck != null) {
                    HashSet<ChessMove>movesToCheck = (HashSet<ChessMove>) pieceToCheck.pieceMoves(currentBoard, new ChessPosition(i, j));

                    for (PieceType promotionPiece : PieceType.values()) {
                        if (movesToCheck.contains(new ChessMove(new ChessPosition(i, j), kingPosition, promotionPiece))) {
                            return true;
                        }
                    }
                    // Normal checks
                    if (movesToCheck.contains(new ChessMove(new ChessPosition(i, j), kingPosition, null))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor) && validMoves(getKingPosition(teamColor)) == null) {
            return true;
        }
        return false;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                currentBoard.addPiece(new ChessPosition(i,j), board.getPiece(new ChessPosition(i,j)));
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }
}
