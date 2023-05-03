package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Bishop extends Piece {

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        ArrayList<Coordinate> moves = this.diagonalMoves();
        moves = removeAlliesFromPossibleMoves(moves);

        return moves;
    }

    public Bishop(Player player, Coordinate position, Board board) {
        super(player, position, PieceType.BISHOP, board);
    }
}
