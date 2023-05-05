package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Bishop extends Piece {

    @Override
    public ArrayList<Coordinate> attackingMoves() {
        return board.sanitiseMoves(this.diagonalMoves(), this);
    }

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        return board.sanitiseMoves(this.attackingMoves(), this);
    }

    public Bishop(Player player, Coordinate position, Board board) {
        super(player, position, PieceType.BISHOP, board, "bp");
    }
}
