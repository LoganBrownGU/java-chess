package pieces;

import main.Coordinate;
import players.Player;

public class Bishop extends Piece {
    @Override
    public boolean move(Coordinate coords) {

        if (coords == null || this.getPosition().equals(coords) || !this.getPosition().lineOfSight(coords, board)) return false;

        return this.getPosition().sameDiagonal(coords);
    }

    public Bishop(Player player, Coordinate position) {
        super(player, position, PieceType.BISHOP);
    }
}
