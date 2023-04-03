package pieces;

import main.Coordinate;

public class Knight extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        return true;
    }

    public Knight(boolean player, Coordinate position) {
        super(player, position, PieceType.KNIGHT);
    }
}
