package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Piece {

    private final ArrayList<Coordinate> previousMoves; // initial position is counted as a move
    private final Player player;
    private final PieceType type;
    private Coordinate position;

    protected Board board;

    public final String representation;

    public abstract ArrayList<Coordinate> attackingMoves();
    public abstract ArrayList<Coordinate> possibleMoves();

    public boolean hasNotMoved() {
        return this.previousMoves.size() <= 1;
    }

    public ArrayList<Coordinate> rankMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();

        moves.addAll(position.coordsBetween(new Coordinate(0, position.y), board));
        moves.addAll(position.coordsBetween(new Coordinate(board.maxX, position.y), board));

        return moves;
    }

    public ArrayList<Coordinate> fileMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();

        moves.addAll(position.coordsBetween(new Coordinate(position.x, 0), board));
        moves.addAll(position.coordsBetween(new Coordinate(position.x, board.maxY), board));

        return moves;
    }

    public ArrayList<Coordinate> diagonalMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();

        Coordinate min = new Coordinate(position.x - board.maxX, position.y - board.maxY);
        Coordinate max = new Coordinate(position.x + board.maxX, position.y + board.maxY);
        moves.addAll(position.coordsBetween(min, board));
        moves.addAll(position.coordsBetween(max, board));
        min = new Coordinate(position.x + board.maxX, position.y - board.maxY);
        max = new Coordinate(position.x - board.maxX, position.y + board.maxY);
        moves.addAll(position.coordsBetween(min, board));
        moves.addAll(position.coordsBetween(max, board));

        return moves;
    }

    @Override
    public String toString() {
        return type + " belonging to " + player.representation + " at " + position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return type == piece.type && Objects.equals(position, piece.position) && player.representation == piece.getPlayer().representation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, position);
    }

    public Piece(Player player, Coordinate position, PieceType type, Board board, String representation) {
        this.previousMoves = new ArrayList<>();
        previousMoves.add(position);
        this.player = player;
        this.position = position;
        this.type = type;
        this.board = board;
        this.representation = representation;

        this.board.addPiece(this);
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
        board.updateUserLayer();
        board.setLastMove(position);
    }

    public ArrayList<Coordinate> getPreviousMoves() {
        return previousMoves;
    }
}
