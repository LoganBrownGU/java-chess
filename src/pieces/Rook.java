package pieces;

import main.Coordinate;

public class Rook extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public Rook(boolean player, Coordinate position) {
        super(player, position, PieceType.ROOK);
    }
}
