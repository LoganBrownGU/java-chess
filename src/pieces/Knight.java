package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Knight extends Piece {

    @Override
    public ArrayList<Coordinate> attackingMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();
        Coordinate position = this.getPosition();

        moves.add(new Coordinate(position.x + 2, position.y + 1));
        moves.add(new Coordinate(position.x - 2, position.y - 1));
        moves.add(new Coordinate(position.x + 2, position.y - 1));
        moves.add(new Coordinate(position.x - 2, position.y + 1));
        moves.add(new Coordinate(position.x + 1, position.y + 2));
        moves.add(new Coordinate(position.x - 1, position.y - 2));
        moves.add(new Coordinate(position.x + 1, position.y - 2));
        moves.add(new Coordinate(position.x - 1, position.y + 2));

        return board.sanitiseMoves(moves, this);
    }

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        return board.sanitiseMoves(this.attackingMoves(), this);
    }

    public Knight(Player player, Coordinate position, Board board) {
        super(player, position, PieceType.KNIGHT, board, "kt");
    }
}
