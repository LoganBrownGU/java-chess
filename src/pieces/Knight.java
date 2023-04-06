package pieces;

import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Knight extends Piece {

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();
        Coordinate position = this.getPosition();

        moves.add(new Coordinate(position.x + 2, position.y + 1));
        moves.add(new Coordinate(position.x - 2, position.y - 1));
        moves.add(new Coordinate(position.x + 2, position.y - 1));
        moves.add(new Coordinate(position.x - 2, position.y + 1));

        return moves;
    }

    public Knight(Player player, Coordinate position) {
        super(player, position, PieceType.KNIGHT);
    }
}
