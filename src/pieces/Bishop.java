package pieces;

import main.Coordinate;

public class Bishop extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public Bishop(boolean player, Coordinate position) {
        super(player, position, PieceType.BISHOP);
    }
}
