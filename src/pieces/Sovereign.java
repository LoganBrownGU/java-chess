package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

public abstract class Sovereign extends Piece {

    public abstract Player checkedBy();

    public Sovereign(Player player, Coordinate position, PieceType type, Board board, String representation) {
        super(player, position, type, board, representation);
    }
}
