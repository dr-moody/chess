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
                if (myPosition.getRow() == 2) {
                    ChessPosition target1 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    ChessPosition target2 = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());

                    if (board.getPiece(target1) == null && board.getPiece(target2) == null) {
                        movesList.add(new ChessMove(myPosition, target1, null));
                        movesList.add(new ChessMove(myPosition, target2, null));
                    } else if (board.getPiece(target1) == null) {
                        movesList.add(new ChessMove(myPosition, target1, null));
                    }
                } else if (myPosition.getRow() == 7) {
                    ChessPosition target1 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());

                    if (board.getPiece(target1) == null) {
                        movesList.add(new ChessMove(myPosition, target1, KNIGHT));
                        movesList.add(new ChessMove(myPosition, target1, ROOK));
                        movesList.add(new ChessMove(myPosition, target1, BISHOP));
                        movesList.add(new ChessMove(myPosition, target1, QUEEN));
                    }
                } else {
                    ChessPosition target1 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());

                    if (board.getPiece(target1) == null) {
                        movesList.add(new ChessMove(myPosition, target1, null));
                    }
                }
                if (myPosition.getRow() == 7) {
                    if (myPosition.getColumn() != 1) {
                        ChessPosition target = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                        if (board.getPiece(target) != null && board.getPiece(target).getTeamColor() != color) {
                            movesList.add(new ChessMove(myPosition, target, KNIGHT));
                            movesList.add(new ChessMove(myPosition, target, ROOK));
                            movesList.add(new ChessMove(myPosition, target, BISHOP));
                            movesList.add(new ChessMove(myPosition, target, QUEEN));
                        }

                    } if (myPosition.getColumn() != 8) {
                        ChessPosition target = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                        if (board.getPiece(target) != null && board.getPiece(target).getTeamColor() != color) {
                            movesList.add(new ChessMove(myPosition, target, KNIGHT));
                            movesList.add(new ChessMove(myPosition, target, ROOK));
                            movesList.add(new ChessMove(myPosition, target, BISHOP));
                            movesList.add(new ChessMove(myPosition, target, QUEEN));
                        }
                    }
                } else {
                    if (myPosition.getColumn() != 1) {
                        ChessPosition target = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                        if (board.getPiece(target) != null && board.getPiece(target).getTeamColor() != color) {
                            movesList.add(new ChessMove(myPosition, target, null));
                        }

                    } if (myPosition.getColumn() != 8) {
                        ChessPosition target = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                        if (board.getPiece(target) != null && board.getPiece(target).getTeamColor() != color) {
                            movesList.add(new ChessMove(myPosition, target, null));
                        }
                    }
                }
            } if (color == BLACK) {
                if (myPosition.getRow() == 7) {
                    ChessPosition target1 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    ChessPosition target2 = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());

                    if (board.getPiece(target1) == null && board.getPiece(target2) == null) {
                        movesList.add(new ChessMove(myPosition, target1, null));
                        movesList.add(new ChessMove(myPosition, target2, null));
                    } else if (board.getPiece(target1) == null) {
                        movesList.add(new ChessMove(myPosition, target1, null));
                    }
                } else if (myPosition.getRow() == 2) {
                    ChessPosition target1 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());

                    if (board.getPiece(target1) == null) {
                        movesList.add(new ChessMove(myPosition, target1, KNIGHT));
                        movesList.add(new ChessMove(myPosition, target1, ROOK));
                        movesList.add(new ChessMove(myPosition, target1, BISHOP));
                        movesList.add(new ChessMove(myPosition, target1, QUEEN));
                    }
                } else {
                    ChessPosition target1 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());

                    if (board.getPiece(target1) == null) {
                        movesList.add(new ChessMove(myPosition, target1, null));
                    }
                }
                if (myPosition.getRow() == 2) {
                    if (myPosition.getColumn() != 1) {
                        ChessPosition target = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                        if (board.getPiece(target) != null && board.getPiece(target).getTeamColor() != color) {
                            movesList.add(new ChessMove(myPosition, target, KNIGHT));
                            movesList.add(new ChessMove(myPosition, target, ROOK));
                            movesList.add(new ChessMove(myPosition, target, BISHOP));
                            movesList.add(new ChessMove(myPosition, target, QUEEN));
                        }

                    } if (myPosition.getColumn() != 8) {
                        ChessPosition target = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                        if (board.getPiece(target) != null && board.getPiece(target).getTeamColor() != color) {
                            movesList.add(new ChessMove(myPosition, target, KNIGHT));
                            movesList.add(new ChessMove(myPosition, target, ROOK));
                            movesList.add(new ChessMove(myPosition, target, BISHOP));
                            movesList.add(new ChessMove(myPosition, target, QUEEN));
                        }
                    }
                } else {
                    if (myPosition.getColumn() != 1) {
                        ChessPosition target = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                        if (board.getPiece(target) != null && board.getPiece(target).getTeamColor() != color) {
                            movesList.add(new ChessMove(myPosition, target, null));
                        }

                    } if (myPosition.getColumn() != 8) {
                        ChessPosition target = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                        if (board.getPiece(target) != null && board.getPiece(target).getTeamColor() != color) {
                            movesList.add(new ChessMove(myPosition, target, null));
                        }
                    }
                }
            }
        }

        if (type == ROOK || type == QUEEN) {
            for (int i=1; myPosition.getRow() + i <= 8; i++) {
                ChessPosition target = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
                if (board.getPiece(target) == null) {
                    movesList.add(new ChessMove(myPosition, target, null));
                } else if (board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i=1; myPosition.getRow() - i >= 1; i++) {
                ChessPosition target = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
                if (board.getPiece(target) == null) {
                    movesList.add(new ChessMove(myPosition, target, null));
                } else if (board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i=1; myPosition.getColumn() + i <= 8; i++) {
                ChessPosition target = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
                if (board.getPiece(target) == null) {
                    movesList.add(new ChessMove(myPosition, target, null));
                } else if (board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i=1; myPosition.getColumn() - i >= 1; i++) {
                ChessPosition target = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
                if (board.getPiece(target) == null) {
                    movesList.add(new ChessMove(myPosition, target, null));
                } else if (board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                    break;
                } else {
                    break;
                }
            }
        }

        if (type == BISHOP || type == QUEEN) {
            for (int i=1; myPosition.getRow() + i <= 8 && myPosition.getColumn() + i <= 8; i++) {
                ChessPosition target = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
                if (board.getPiece(target) == null) {
                    movesList.add(new ChessMove(myPosition, target, null));
                } else if (board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i=1; myPosition.getRow() + i <= 8 && myPosition.getColumn() - i >= 1; i++) {
                ChessPosition target = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
                if (board.getPiece(target) == null) {
                    movesList.add(new ChessMove(myPosition, target, null));
                } else if (board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i=1; myPosition.getRow() - i >= 1 && myPosition.getColumn() + i <= 8; i++) {
                ChessPosition target = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
                if (board.getPiece(target) == null) {
                    movesList.add(new ChessMove(myPosition, target, null));
                } else if (board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i=1; myPosition.getRow() - i >= 1 && myPosition.getColumn() - i >= 1; i++) {
                ChessPosition target = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
                if (board.getPiece(target) == null) {
                    movesList.add(new ChessMove(myPosition, target, null));
                } else if (board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                    break;
                } else {
                    break;
                }
            }
        }

        if (type == KNIGHT) {
            if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn() + 1 <= 8) {
                ChessPosition target = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn() - 1 >= 1) {
                ChessPosition target = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn() + 1 <= 8) {
                ChessPosition target = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn() - 1 >= 1) {
                ChessPosition target = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() - 2 >= 1) {
                ChessPosition target = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() + 2 <= 8) {
                ChessPosition target = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 2 >= 1) {
                ChessPosition target = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 2 <= 8) {
                ChessPosition target = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
        }

        if (type == KING) {
            if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() - 1 >= 1) {
                ChessPosition target = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() + 1 <= 8) {
                ChessPosition target = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 1 >= 1) {
                ChessPosition target = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 1 <= 8) {
                ChessPosition target = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() + 1 <= 8) {
                ChessPosition target = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getColumn() + 1 <= 8) {
                ChessPosition target = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getRow() - 1 >= 1) {
                ChessPosition target = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
            if (myPosition.getColumn() - 1 >= 1) {
                ChessPosition target = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
                if (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color) {
                    movesList.add(new ChessMove(myPosition, target, null));
                }
            }
        }

        return movesList;
    }
}
