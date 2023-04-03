package pieces;

import main.Coordinate;

public class Rook extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public Rook(boolean player) {
        super(player, PieceType.ROOK);
    }
}
