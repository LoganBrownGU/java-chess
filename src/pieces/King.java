package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class King extends Piece {

    //todo make a sovereign interface
    private boolean checked = false;

    @Override
    public ArrayList<Coordinate> attackingMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();
        Coordinate position = this.getPosition();

        moves.add(new Coordinate(position.x, position.y + 1));
        moves.add(new Coordinate(position.x, position.y - 1));
        moves.add(new Coordinate(position.x + 1, position.y));
        moves.add(new Coordinate(position.x - 1, position.y));
        moves.add(new Coordinate(position.x - 1, position.y - 1));
        moves.add(new Coordinate(position.x + 1, position.y + 1));
        moves.add(new Coordinate(position.x + 1, position.y - 1));
        moves.add(new Coordinate(position.x - 1, position.y + 1));

        return board.sanitiseMoves(moves, this);
    }

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        return board.sanitiseMoves(this.attackingMoves(), this);
    }

    public King(Player player, Coordinate position, Board board) {
        super(player, position, PieceType.KING, board, "kg");
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
