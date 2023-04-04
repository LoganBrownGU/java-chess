package pieces;

import main.Board;
import main.Coordinate;
import players.Player;

public class King extends Piece {
    @Override
    public boolean move(Coordinate coords) {

        if (coords == null || this.getPosition().equals(coords)) return false;

        return this.getPosition().adjacent(coords) || this.getPosition().adjacentDiagonally(coords);
    }

    public King(Player player, Coordinate position) {
        super(player, position, PieceType.KING);
    }
}
