package pieces;

import main.Coordinate;
import players.Player;

public class King extends Piece {

    private boolean checked = false;

    @Override
    public boolean move(Coordinate coords) {

        if (coords == null || this.getPosition().equals(coords)) return false;

        return this.getPosition().adjacent(coords) || this.getPosition().adjacentDiagonally(coords);
    }

    public King(Player player, Coordinate position) {
        super(player, position, PieceType.KING);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
