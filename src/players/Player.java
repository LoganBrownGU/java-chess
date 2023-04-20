package players;

import board.Board;
import main.Coordinate;
import pieces.Piece;

public abstract class Player {

    public final char representation;
    public final Board board;
    public final int direction;
    private Piece sovereign;

    public abstract Piece getPiece();
    public abstract Coordinate getMove(Piece pieceToMove);

    public Player(char representation, Board board, int direction) {
        this.representation = representation;
        this.board = board;
        this.direction = direction;
    }

    public Piece getSovereign() {
        return sovereign;
    }

    public void setSovereign(Piece sovereign) {
        this.sovereign = sovereign;
    }

    @Override
    public String toString() {
        return "player " + representation;
    }
}
