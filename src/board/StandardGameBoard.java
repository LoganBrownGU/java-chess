package board;

import main.Coordinate;
import pieces.Piece;
import players.Player;
import userlayers.CommandLineUserLayer;
import userlayers.UserLayer;

import java.util.ArrayList;

public class StandardGameBoard extends Board {

    // todo need to check for all possible players, as a player can make a move that puts themself into check
    public Player check(Player player) {        // returns player that player is checking or null if it is not
        for (Player other : this.getPlayers()) {
            if (other == player) continue;

            if (other.getSovereign().checkedBy() == player) return other;
        }

        return null;
    }

    public boolean canPreventCheck(char checkingRep, char checkedRep) { // returns true if there's a move that would prevent checking from winning
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

    public Player checkWinner() {
        for (Player p : getPlayers()) {
            Player checked = check(p);
            if (checked == null) continue;

            // now need to play every possible move of checked to see if it would prevent p from winning
            if (canPreventCheck(p.representation, checked.representation)) return null;
        }

        return null;
    }

    @Override
    public ArrayList<Coordinate> sanitiseMoves(ArrayList<Coordinate> moves, Piece piece) {
        ArrayList<Coordinate> newMoves = new ArrayList<>();
        for (Coordinate coord : moves) {
            Piece test = this.pieceAt(coord);
            if (test != null && test.getPlayer() == piece.getPlayer()) continue;

            if (coord.x < 0 || coord.x >= this.maxX) continue;
            if (coord.y < 0 || coord.y >= this.maxY) continue;

            newMoves.add(coord);
        }

        return newMoves;
    }

    @Override
    public void play() {
        boolean hasWon = false;

        while (!hasWon) {
            Player checking = null, checked = null;

            for (Player player : super.getPlayers()) {

                Piece pieceToMove = player.getPiece();
                Coordinate move = player.getMove(pieceToMove);
                Piece pieceAtMove = super.pieceAt(move);

                if (pieceAtMove != null) super.removePiece(pieceAtMove);
                pieceToMove.setPosition(move);

                Player winner = checkWinner();
                if (winner != null) {
                    getUserLayer().showWinner(winner);
                    hasWon = true;
                    break;
                }

                if (checked != null && check(checking) == checked) {
                    getUserLayer().showWinner(checking);
                    hasWon = true;
                    break;
                }

                checked = check(player);
                if (checked != null) {
                    checking = player;
                    getUserLayer().showCheck(checking, checked);
                }
            }
        }

        System.out.println("game end");
    }

    public StandardGameBoard() {
        super(8, 8, new CommandLineUserLayer());
    }

    public StandardGameBoard(UserLayer userLayer) {
        super(8, 8, userLayer);
    }
}
