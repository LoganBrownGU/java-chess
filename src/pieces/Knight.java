package pieces;

import main.Board;
import main.Coordinate;
import players.Player;

public class Knight extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        if (coords == null || this.getPosition().equals(coords)) return false;

        int x = Math.abs(this.getPosition().x - coords.x);
        int y = Math.abs(this.getPosition().y - coords.y);

        return (x == 1 && y == 2) || (x == 2 && y == 1);
    }

    public Knight(Player player, Coordinate position) {
        super(player, position, PieceType.KNIGHT);
    }
}
