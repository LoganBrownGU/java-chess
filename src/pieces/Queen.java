package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Queen extends Piece {

    @Override
    public ArrayList<Coordinate> attackingMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();

        moves.addAll(this.rankMoves());
        moves.addAll(this.fileMoves());
        moves.addAll(this.diagonalMoves());

        return board.sanitiseMoves(moves, this);
    }

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        return board.sanitiseMoves(attackingMoves(), this);
    }

    public Queen(Player player, Coordinate position, Board board) {
        super(player, position, PieceType.QUEEN, board, "qn");
    }
}
