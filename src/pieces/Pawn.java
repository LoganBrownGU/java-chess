package pieces;

import main.Coordinate;

public class Pawn extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public Pawn(boolean player, Coordinate position) {
        super(player, position, PieceType.PAWN);
    }
}
