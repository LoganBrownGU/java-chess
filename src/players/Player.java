package players;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import pieces.Sovereign;

public abstract class Player {
    public final char representation;
    public final Board board;
    private Sovereign sovereign;
    public final PlayerType type;

    public abstract Piece getPiece();
    public abstract Coordinate getMove(Piece pieceToMove);

    //public abstract void cancelMove();

    public Player(char representation, Board board, PlayerType type) {
        this.representation = representation;
        this.board = board;

        this.board.addPlayer(this);
        this.type = type;
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
