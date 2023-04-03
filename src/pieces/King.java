package pieces;

import main.Board;
import main.Coordinate;
import players.Player;

public class King extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public King(Player player, Coordinate position) {
        super(player, position, PieceType.KING);
    }
}
