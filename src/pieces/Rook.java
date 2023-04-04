package pieces;

import main.Coordinate;
import players.Player;

public class Rook extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        // todo deal with castling

        if (coords == null || this.getPosition().equals(coords) || !this.getPosition().lineOfSight(coords, this.board)) return false;

        if (!(this.getPosition().sameFile(coords) || this.getPosition().sameRank(coords))) return false;
        
        return true;
    }

    public Rook(Player player, Coordinate position) {
        super(player, position, PieceType.ROOK);
    }
}
