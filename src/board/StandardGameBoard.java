package board;

import main.Coordinate;
import pieces.Piece;
import players.Player;
import userlayers.CommandLineUserLayer;
import userlayers.UserLayer;

public class StandardGameBoard extends Board {

    public boolean check(Player player) {
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

    // todo implement this
    public Player checkWinner() {
        return null;
    }

    @Override
    public void play() {
        while (true) {
            for (Player player: super.getPlayers()) {
                if (check(player)) System.out.println("CHECK");

                Piece pieceToMove = player.getPiece();
                Coordinate move = player.getMove(pieceToMove);
                Piece pieceAtMove = super.pieceAt(move);

                if (pieceAtMove != null) super.removePiece(pieceAtMove);
                pieceToMove.setPosition(move);

                Player winner = checkWinner();
                if (winner != null) System.out.println(winner + " wins");
            }
        }
    }

    public StandardGameBoard() {
        super(8, 8, new CommandLineUserLayer());
    }
    public StandardGameBoard(UserLayer userLayer) {
        super(8, 8, userLayer);
    }
}
