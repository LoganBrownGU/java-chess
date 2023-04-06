package board;

import main.Coordinate;
import pieces.Piece;
import players.Player;

public class StandardGameBoard extends Board {

    private boolean check(Player player) {
        for (Player other: this.getPlayers()) {
            if (other == player) continue;

            for (Piece piece: super.getPieces()) {
                if (piece.getPlayer() != player) continue;

                for (Coordinate move: piece.possibleMoves())
                    if (move.equals(other.getSovereign().getPosition())) return true;

            }
        }

        return false;
    }

    private Player checkWin() {
        return null;
    }

    @Override
    public void play() {
        while (true) {
            // todo main game logic here

            for (Player player: super.getPlayers()) {
                if (check(player)) System.out.println("CHECK");

                Piece pieceToMove = player.getPiece();
                Coordinate move = player.getMove(pieceToMove);
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
