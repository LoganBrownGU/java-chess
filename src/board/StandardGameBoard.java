package board;

import main.Coordinate;
import pieces.Piece;
import players.Player;
import userlayers.CommandLineUserLayer;
import userlayers.UserLayer;

import java.util.ArrayList;

public class StandardGameBoard extends Board {

    // todo this should probably return an arraylist; if there are multiple players then a player could be checking multiple players
    public Player check(Player player) {        // returns player that player is checking or null if it is not
        for (Player other : this.getPlayers()) {
            if (other == player) continue;

            for (Piece piece : super.getPieces()) {
                if (piece.getPlayer() != player) continue;

                for (Coordinate move : piece.possibleMoves())
                    if (move.equals(other.getSovereign().getPosition())) return other;

            }
        }

        return null;
    }

    public boolean canBlock(Player checking, Player checked) { // returns true if there's a move that would prevent checking from winning
        // need to clone positions
        ArrayList<Coordinate> oldCoords = new ArrayList<>();
        for (Piece p: this.getPieces())
            oldCoords.add(new Coordinate(p.getPosition().x, p.getPosition().y));
        

        return false;
    }

    // todo implement this
    public Player checkWinner() {
        for (Player p : getPlayers()) {
            Player checked = check(p);
            if (checked == null) continue;

            // now need to play every possible move of checked to see if it would prevent p from winning
        }

        return null;
    }

    @Override
    public void play() {
        while (true) {
            for (Player player : super.getPlayers()) {
                if (check(player) != null) System.out.println(player + " checks " + check(player));

                Piece pieceToMove = player.getPiece();
                Coordinate move = player.getMove(pieceToMove);
                Piece pieceAtMove = super.pieceAt(move);

                if (pieceAtMove != null) super.removePiece(pieceAtMove);
                pieceToMove.setPosition(move);

                Player winner = checkWinner();
                if (winner != null) {
                    System.out.println(winner + " wins");
                    break;
                }
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
