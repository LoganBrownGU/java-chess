package pieces;

import main.Coordinate;
import players.Player;

public class Pawn extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public Pawn(Player player, Coordinate position) {
        super(player, position, PieceType.PAWN);
    }
}
