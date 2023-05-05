package players;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import pieces.Sovereign;

public abstract class Player {
    // todo could add self to board in constructor

    public final char representation;
    public final Board board;
    private Sovereign sovereign;

    public abstract Piece getPiece();
    public abstract Coordinate getMove(Piece pieceToMove);

    public Player(char representation, Board board) {
        this.representation = representation;
        this.board = board;

        this.board.addPlayer(this);
    }

    public Sovereign getSovereign() {
        return sovereign;
    }

    public void setSovereign(Sovereign sovereign) {
        this.sovereign = sovereign;
    }

    @Override
    public String toString() {
        return "player " + representation;
    }
}
