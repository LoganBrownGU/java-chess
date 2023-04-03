package players;

import main.Board;
import main.Coordinate;
import pieces.Piece;

public abstract class Player {

    public final char representation;
    public final Board board;

    public abstract Piece getPiece();
    public abstract Coordinate getMove(Piece pieceToMove);

    public Player(char representation, Board board) {
        this.representation = representation;
        this.board = board;
    }
}
