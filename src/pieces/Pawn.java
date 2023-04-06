package pieces;

import main.Coordinate;
import players.Player;

public class Pawn extends Piece {

    private boolean canEnPassant = false;

    @Override
    public void setPosition(Coordinate position) {
        canEnPassant = Math.abs(position.y - this.getPosition().y) == 2;

        super.setPosition(position);
    }

    @Override
    public boolean move(Coordinate coords) {
        /* 5 possible things could happen: normal forward move, two space forward move, capture,
        en passant, or promotion. */

        int direction = this.getPlayer().direction;
        if (coords == null || this.getPosition().equals(coords)) return false;

        int x = coords.x - this.getPosition().x;
        int y = coords.y - this.getPosition().y;

        /* if y has the same sign as direction, then multiplying them will always give a positive number,
        or 0 if one of them is 0. This line essentially checks if the piece is being moved in the correct direction */
        if (y * direction < 0) return false;

        x = Math.abs(x);
        y = Math.abs(y);

        // check for normal move
        if (x == 0 && y == 1 && board.pieceAt(coords) == null) return true;

        // check for two space move
        if (x == 0 && y == 2 && this.getPosition().lineOfSight(coords, this.board) && this.board.pieceAt(coords) == null) return true;

        // check for normal capture / en passant
        if (x == 1 && y == 1) {
            // if taking a piece normally
            if (board.pieceAt(coords) != null) return true;

            // todo not quite right; need to check if potentialEnPassant just moved
            // if taking a piece en passant
            Piece potentialEnPassant = board.pieceAt(new Coordinate(coords.x, coords.y - direction));
            Coordinate lastMove = potentialEnPassant.getLastMove();
            if (potentialEnPassant instanceof Pawn && ((Pawn) potentialEnPassant).canEnPassant()) {
                board.removePiece(potentialEnPassant);
                return true;
            }
        }

        return false;
    }

    public Pawn(Player player, Coordinate position) {
        super(player, position, PieceType.PAWN);
    }

    public boolean canEnPassant() {
        return canEnPassant;
    }
}
