package board;

import main.Coordinate;
import pieces.Piece;
import pieces.PieceFactory;
import players.Player;
import userlayers.CommandLineUserLayer;
import userlayers.UserLayer;

import java.util.ArrayList;
import java.util.Arrays;

public class StandardGameBoard extends Board {

    // todo this should probably return an arraylist; if there are > 1 players then a player could be checking > 1 players
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

    public boolean canBlock(char checkingRep, char checkedRep) { // returns true if there's a move that would prevent checking from winning
        Player checked = playerWithRep(checkedRep);

        for (Piece piece : this.getPiecesBelongingTo(checked)) {
            for (int i = 0; i < piece.possibleMoves().size(); i++) { // need to iterate over piece's possible moves
                StandardGameBoard clone = BoardFactory.cloneBoard(this);
                Piece possibleBlocker = null;
                for (Piece clonePiece : clone.getPieces()) {
                    if (clonePiece.equals(piece)) {
                        possibleBlocker = clonePiece;
                        break;
                    }
                }

                // get move to check
                assert possibleBlocker != null;
                Coordinate move = possibleBlocker.possibleMoves().get(i);

                // make move
                if (clone.pieceAt(move) != null) clone.removePiece(clone.pieceAt(move));
                possibleBlocker.setPosition(move);

                // check if checking is still checking checked
                Player checkedClone = clone.playerWithRep(checkedRep);
                Player checkingClone = clone.playerWithRep(checkingRep);
                if (clone.check(checkingClone) != checkedClone)
                    return true;
            }
        }

        return false;
    }

    // todo implement this
    public Player checkWinner() {
        for (Player p : getPlayers()) {
            Player checked = check(p);
            if (checked == null) continue;

            // now need to play every possible move of checked to see if it would prevent p from winning
            if (canBlock(p.representation, checked.representation)) return null;
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
