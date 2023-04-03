package pieces;

import main.Board;
import main.Coordinate;
import players.Player;

public class Knight extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public Knight(Player player, Coordinate position) {
        super(player, position, PieceType.KNIGHT);
    }
}
