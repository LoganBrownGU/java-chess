package pieces;

import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Rook extends Piece {
    @Override
    public boolean move(Coordinate coords) {
        // todo castling is not quite right: king cannot pass through square under attack

        if (coords == null || this.getPosition().equals(coords) || !this.getPosition().lineOfSight(coords, this.board)) return false;

        Coordinate pos = this.getPosition();

        if (!(pos.sameFile(coords) || pos.sameRank(coords))) return false;

        // castling
        King king = board.getKing(this.getPlayer());   // don't have to check for null because king will always be in play

        boolean isLeft = pos.lineOfSight(new Coordinate(king.getPosition().x - 1, king.getPosition().y), this.board);
        boolean isRight = pos.lineOfSight(new Coordinate(king.getPosition().x + 1, king.getPosition().y), this.board);
        if (!isLeft && !isRight) return false;

        if (king.hasMoved() || this.hasMoved()) return false;
        if (!king.getPosition().lineOfSight(pos, board)) return false;

        return !king.isChecked();
    }

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        // todo castling pain
        ArrayList<Coordinate> moves = new ArrayList<>();

        moves.addAll(this.rankMoves());
        moves.addAll(this.fileMoves());

        return moves;
    }

    public Rook(Player player, Coordinate position) {
        super(player, position, PieceType.ROOK);
    }
}
