package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public abstract class Piece implements PieceStrategy {
    // todo for pieces that do special moves, make sure there is no piece at the point they are moving to
    // todo could use player's instance of board rather than storing its own

    private ArrayList<Coordinate> previousMoves;
    private final Player player;
    private PieceType type;
    private Coordinate position;
    protected Board board;

    public Coordinate getLastMove() {
        return previousMoves.get(0);
    }

    public void updateBoard() {
        board.update();
    }

    public boolean hasMoved() {
        return this.previousMoves.size() != 0;
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

    public Piece(Player player, Coordinate position, PieceType type, Board board) {
        this.previousMoves = new ArrayList<>();
        this.player = player;
        this.position = position;
        this.type = type;
        this.board = board;
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
