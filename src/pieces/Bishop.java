package pieces;

import main.Coordinate;
import players.Player;

public class Bishop extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public Bishop(Player player, Coordinate position) {
        super(player, position, PieceType.BISHOP);
    }
}
