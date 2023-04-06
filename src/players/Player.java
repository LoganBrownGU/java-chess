package players;

import main.Board;
import main.Coordinate;
import pieces.Piece;

public abstract class Player {

    // todo write tests

    public final char representation;
    public final Board board;
    public final int direction;

    public abstract Piece getPiece();
    public abstract Coordinate getMove(Piece pieceToMove);

    public Player(char representation, Board board, int direction) {
        this.representation = representation;
        this.board = board;
        this.direction = direction;
    }
}
