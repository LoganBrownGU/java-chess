package pieces;

import main.Coordinate;
import players.Player;

public class Rook extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public Rook(Player player, Coordinate position) {
        super(player, position, PieceType.ROOK);
    }
}
