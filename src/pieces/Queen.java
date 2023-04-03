package pieces;

import main.Coordinate;

public class Queen extends Piece {

    @Override
    public boolean move(Coordinate coords) {
        return false;
    }

    public Queen(boolean player, Coordinate position) {
        super(player, position, PieceType.QUEEN);
    }
}
