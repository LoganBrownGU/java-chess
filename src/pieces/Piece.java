package pieces;

import main.Coordinate;

import java.util.ArrayList;

public abstract class Piece implements PieceStrategy {
    private ArrayList<Coordinate> previousMoves;
    private final boolean player; // false = black, true = white
    private PieceType type;
    private Coordinate position;

    public Coordinate getLastMove() {
        return previousMoves.get(0);
    }

    public Piece(boolean player, Coordinate position) {
        this.previousMoves = new ArrayList<>();
        this.player = player;
        this.position = position;
    }

    public boolean getPlayer() {
        return player;
    }

    public PieceType getType() {
        return type;
    }
}
