package players;

import board.Board;
import main.Coordinate;
import pieces.Piece;

public abstract class Player {

    public final char representation;
    public final Board board;
    private Piece sovereign;

    public abstract Piece getPiece();
    public abstract Coordinate getMove(Piece pieceToMove);

    public Player(char representation, Board board) {
        this.representation = representation;
        this.board = board;
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
