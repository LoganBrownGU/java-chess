package pieces;

import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Knight extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        if (coords == null || this.getPosition().equals(coords)) return false;

        int x = Math.abs(this.getPosition().x - coords.x);
        int y = Math.abs(this.getPosition().y - coords.y);

        return (x == 1 && y == 2) || (x == 2 && y == 1);
    }

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();

        

        return moves;
    }

    public Knight(Player player, Coordinate position) {
        super(player, position, PieceType.KNIGHT);
    }
}
