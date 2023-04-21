package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Pawn extends Piece {

    private boolean canEnPassant = false;
    public final int direction;
    public final int promotionRank;

    @Override
    public void setPosition(Coordinate position) {
        canEnPassant = Math.abs(position.y - this.getPosition().y) == 2;
        if (position.y == this.promotionRank) {
            String piece = board.getUserLayer().dialogue("What piece would you like to promote to?");
            // todo switch here

        }

        super.setPosition(position);
    }

    // todo promotion

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();
        Coordinate position = this.getPosition();

        // moving forward
        Coordinate test = new Coordinate(position.x, position.y + direction);
        if (board.pieceAt(test) == null) moves.add(test);
        test = new Coordinate(test.x, test.y + direction);
        if (!this.hasMoved() && board.pieceAt(test) == null) moves.add(test);

        // taking diagonally
        test = new Coordinate(position.x + 1, position.y + 1);
        if (board.pieceAt(test) != null) moves.add(test);
        test = new Coordinate(position.x + 1, position.y - 1);
        if (board.pieceAt(test) != null) moves.add(test);

        // todo en passant

        return moves;
    }

    public Pawn(Player player, Coordinate position, int direction, Board board) {
        super(player, position, PieceType.PAWN, board);
        this.direction = direction;
        this.promotionRank = direction == 1 ? board.maxY : 0;
    }

    public boolean canEnPassant() {
        return canEnPassant;
    }
}
