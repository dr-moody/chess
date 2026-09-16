package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] board = new ChessPiece[9][9];

    public ChessBoard() {
//
    }

//    Currently the board prints upsidedown--I should fix this

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();

        boardString.append("Chess Board:\n");

        for (ChessPiece[] col : Arrays.copyOfRange(board, 1, 9)) {
            for (ChessPiece square : Arrays.copyOfRange(col, 1, 9)) {
                if (square != null) {
                    boardString.append(square.toString()).append(" ");
                }
                else {
                    boardString.append(". ");
                }
            }
            boardString.append("\n");
        }

        return boardString.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getColumn()][position.getRow()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    private void clearBoard() {
        for (ChessPiece[] col : board) {
            for (ChessPiece square : col) {
                square = null;
            }
        }
    }

    public void resetBoard() {
        clearBoard();

        // Set up white pieces
        addPiece(new ChessPosition(1,1), new ChessPiece(WHITE, ROOK));
        addPiece(new ChessPosition(1,2), new ChessPiece(WHITE, KNIGHT));
        addPiece(new ChessPosition(1,3), new ChessPiece(WHITE, BISHOP));
        addPiece(new ChessPosition(1,4), new ChessPiece(WHITE, QUEEN));
        addPiece(new ChessPosition(1,5), new ChessPiece(WHITE, KING));
        addPiece(new ChessPosition(1,6), new ChessPiece(WHITE, BISHOP));
        addPiece(new ChessPosition(1,7), new ChessPiece(WHITE, KNIGHT));
        addPiece(new ChessPosition(1,8), new ChessPiece(WHITE, ROOK));

        // Set up white pawns
        for (int i = 1; i <= 8; i++) {
            addPiece(new ChessPosition(2,i), new ChessPiece(WHITE, PAWN));
        }

        // Set up black pieces
        addPiece(new ChessPosition(8,1), new ChessPiece(BLACK, ROOK));
        addPiece(new ChessPosition(8,2), new ChessPiece(BLACK, KNIGHT));
        addPiece(new ChessPosition(8,3), new ChessPiece(BLACK, BISHOP));
        addPiece(new ChessPosition(8,4), new ChessPiece(BLACK, QUEEN));
        addPiece(new ChessPosition(8,5), new ChessPiece(BLACK, KING));
        addPiece(new ChessPosition(8,6), new ChessPiece(BLACK, BISHOP));
        addPiece(new ChessPosition(8,7), new ChessPiece(BLACK, KNIGHT));
        addPiece(new ChessPosition(8,8), new ChessPiece(BLACK, ROOK));

        // Set up black pawns
        for (int i = 1; i <= 8; i++) {
            addPiece(new ChessPosition(7,i), new ChessPiece(BLACK, PAWN));
        }
    }

    public static void main(String[] args) {
        ChessBoard myBoard = new ChessBoard();
        myBoard.resetBoard();
        System.out.println(myBoard);
    }

}
