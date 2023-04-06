package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public abstract class Piece implements PieceStrategy {
    // todo for pieces that do special moves, make sure there is no piece at the point they are moving to

    private ArrayList<Coordinate> previousMoves;
    private final Player player;
    private PieceType type;
    private Coordinate position;
    protected Board board;

    public Coordinate getLastMove() {
        return previousMoves.get(0);
    }

    public void register(Board b) {
        this.board = b;
    }

    public void updateBoard() {
        board.update();
    }

    public boolean hasMoved() {
        return this.previousMoves.size() != 0;
    }

    @Override
    public String toString() {
        return type + " belonging to " + player.representation + " at " + position;
    }

    public Piece(Player player, Coordinate position, PieceType type) {
        this.previousMoves = new ArrayList<>();
        this.player = player;
        this.position = position;
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public PieceType getType() {
        return type;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
        previousMoves.add(position);
        updateBoard();
    }

    public ArrayList<Coordinate> getPreviousMoves() {
        return previousMoves;
    }
}
