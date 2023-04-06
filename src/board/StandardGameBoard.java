package board;

import main.Coordinate;
import pieces.Piece;
import players.Player;

public class StandardGameBoard extends Board {

    private boolean check(Player player) {
        for (Piece p: super.getPieces()) {
            if (p.getPlayer() != player) continue;


        }

        return false;
    }

    private Player checkWin() {
        return null;
    }

    @Override
    public void play() {
        while (checkWin() == null) {
            // todo main game logic here

            for (Player p: super.getPlayers()) {
                Piece pieceToMove = p.getPiece();
                Coordinate move = p.getMove(pieceToMove);
                Piece pieceAtMove = super.pieceAt(move);

                if (pieceAtMove != null) super.removePiece(pieceAtMove);
                pieceToMove.setPosition(move);
            }
        }
    }

    public StandardGameBoard() {
        super(8, 8);
    }
}
