package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Rook extends Piece {

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        // todo castling pain
        ArrayList<Coordinate> moves = new ArrayList<>();

        moves.addAll(this.rankMoves());
        moves.addAll(this.fileMoves());

        return removeAlliesFromPossibleMoves(moves);
    }

    public Rook(Player player, Coordinate position, Board board) {
        super(player, position, PieceType.ROOK, board);
    }
}
