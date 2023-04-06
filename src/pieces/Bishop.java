package pieces;

import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Bishop extends Piece {

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        return this.diagonalMoves();
    }

    public Bishop(Player player, Coordinate position) {
        super(player, position, PieceType.BISHOP);
    }
}
