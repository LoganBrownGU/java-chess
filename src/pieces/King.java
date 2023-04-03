package pieces;

import main.Coordinate;

public class King extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public King(boolean player, Coordinate position) {
        super(player, position, PieceType.KING);
    }
}
