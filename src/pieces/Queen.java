package pieces;

import main.Board;
import main.Coordinate;
import players.Player;

public class Queen extends Piece {

    @Override
    public boolean move(Coordinate coords) {
        return false;
    }

    public Queen(Player player, Coordinate position) {
        super(player, position, PieceType.QUEEN);
    }
}
