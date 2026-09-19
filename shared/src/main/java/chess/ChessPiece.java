package chess;

import java.util.*;

import static chess.ChessGame.TeamColor;
import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPiece(TeamColor pieceColor, ChessPiece.PieceType pieceType) {
        this.color = pieceColor;
        this.type = pieceType;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN,
        OUT_OF_BOUNDS
    }

    @Override
    public String toString() {
        String pieceString;

        if (type == KNIGHT) {
            pieceString = "N";
        }
        else {
            pieceString = String.valueOf(type.toString().charAt(0));
        }

        if (color == BLACK) {
            pieceString = pieceString.toLowerCase();
        }
        return pieceString;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> movesList = new HashSet<ChessMove>();

        if (type == PAWN) {
            if (color == WHITE) {
                // Pawn second rank forward
                if ((myPosition.getRow() == 2) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null) && (board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn())) == null)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()), null));
                }
                // Pawn forward, not promoting
                if ((board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null) && (myPosition.getRow() != 7)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                }
                // Pawn forward, promoting
                if ((board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null) && (myPosition.getRow() == 7)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), KNIGHT));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), BISHOP));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ROOK));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), QUEEN));
                }
                // Pawn capturing right, not promoting
                if ((myPosition.getColumn() != 8) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor() == BLACK) && (myPosition.getRow() != 7)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
                }
                // Pawn capturing left, not promoting
                if ((myPosition.getColumn() != 1) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)).getTeamColor() == BLACK) && (myPosition.getRow() != 7)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                }
                // Pawn capturing right, promoting
                if ((myPosition.getColumn() != 8) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor() == BLACK) && (myPosition.getRow() == 7)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), KNIGHT));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), BISHOP));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ROOK));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), QUEEN));
                }
                // Pawn capturing left, promoting
                if ((myPosition.getColumn() != 1) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)).getTeamColor() == BLACK) && (myPosition.getRow() == 7)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), KNIGHT));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), BISHOP));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ROOK));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), QUEEN));
                }

            }

            else if (color == BLACK) {
                // Pawn seventh rank forward
                if ((myPosition.getRow() == 7) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null) && (board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn())) == null)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()), null));
                }
                // Pawn forward, not promoting
                if ((board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null) && (myPosition.getRow() != 2)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                }
                // Pawn forward, promoting
                if ((board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null) && (myPosition.getRow() == 2)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), KNIGHT));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), BISHOP));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ROOK));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), QUEEN));
                }
                // Pawn capturing right, not promoting
                if ((myPosition.getColumn() != 8) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)).getTeamColor() == WHITE) && (myPosition.getRow() != 2)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
                }
                // Pawn capturing left, not promoting
                if ((myPosition.getColumn() != 1) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)).getTeamColor() == WHITE) && (myPosition.getRow() != 2)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                }
                // Pawn capturing right, promoting
                if ((myPosition.getColumn() != 8) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)).getTeamColor() == WHITE) && (myPosition.getRow() == 2)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), KNIGHT));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), BISHOP));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ROOK));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), QUEEN));
                }
                // Pawn capturing left, promoting
                if ((myPosition.getColumn() != 1) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)).getTeamColor() == WHITE) && (myPosition.getRow() == 2)) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), KNIGHT));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), BISHOP));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ROOK));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), QUEEN));
                }
            }

        } else if (type == ROOK) {
            // Up
            for (int i = myPosition.getColumn() + 1; i <= 8; i++) {
                if (board.getPiece(new ChessPosition(myPosition.getRow(), i)) == null) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
                } else if (board.getPiece(new ChessPosition(myPosition.getRow(), i)).getTeamColor() != color) {
                        movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
                        break;
                } else {
                    break;
                }
            }

            // Down
            for (int i = myPosition.getColumn() - 1; i >= 1; i--) {
                if (board.getPiece(new ChessPosition(myPosition.getRow(), i)) == null) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
                } else if (board.getPiece(new ChessPosition(myPosition.getRow(), i)).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
                    break;
                } else {
                    break;
                }
            }

            // Left
            for (int i = myPosition.getRow() - 1; i >= 1; i--) {
                if (board.getPiece(new ChessPosition(i, myPosition.getColumn())) == null) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
                } else if (board.getPiece(new ChessPosition(i, myPosition.getColumn())).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
                    break;
                } else {
                    break;
                }
            }

            // Right
            for (int i = myPosition.getRow() + 1; i <= 8; i++) {
                if (board.getPiece(new ChessPosition(i, myPosition.getColumn())) == null) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
                } else if (board.getPiece(new ChessPosition(i, myPosition.getColumn())).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
                    break;
                } else {
                    break;
                }
            }
        }
    //        else if (type == KNIGHT) {
    //
    //        } else if (type == BISHOP) {
    //
    //        } else if (type == KING) {
    //
    //        } else if (type == QUEEN) {
    //
    //        }


        return movesList;
}
}
