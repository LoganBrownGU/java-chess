package pieces;

import main.Coordinate;
import players.Player;

public class Queen extends Piece {

    @Override
    public boolean move(Coordinate coords) {

        Coordinate pos = this.getPosition();

        if (coords == null || pos.equals(coords) || pos.lineOfSight(coords, this.board)) return false;

        return pos.sameRank(coords) || pos.sameFile(coords) || pos.sameDiagonal(coords);
    }

    public Queen(Player player, Coordinate position) {
        super(player, position, PieceType.QUEEN);
    }
}
