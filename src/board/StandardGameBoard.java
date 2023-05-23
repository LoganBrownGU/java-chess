package board;

import main.Coordinate;
import pieces.Piece;
import players.Player;
import userlayers.CommandLineUserLayer;
import userlayers.UserLayer;

public class StandardGameBoard extends Board {

    // todo add a way to cancel piece selection

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
    public void play() {
        boolean hasWon = false;

        while (!hasWon) {
            Player checking = null, checked = null;

            gameLoop: for (Player player : super.getPlayers()) {

                // todo could set both to null and while loop
                Piece pieceToMove = null;
                Coordinate move = null;
                // since getMove could return null if player cancels move, we loop until getMove returns true
                while (move == null) {
                    pieceToMove = player.getPiece();
                    move = player.getMove(pieceToMove);
                }
                Piece pieceAtMove = super.pieceAt(move);

                if (pieceAtMove != null) {
                    super.getUserLayer().showPieceTaken(pieceAtMove);
                    super.removePiece(pieceAtMove);
                }
                pieceToMove.setPosition(move);

                // for normal checkmate where player checkmates opponent
                Player winner = checkWinner();
                if (winner != null) {
                    getUserLayer().showWinner(winner);
                    hasWon = true;
                    break;
                }

                // if opponent puts player into check, and then player does not prevent the check
                if (checked != null && check(checking) == checked) {
                    getUserLayer().showWinner(checking);
                    hasWon = true;
                    break;
                }

                // if player puts themself into check
                for (Player other : this.getPlayers()) {
                    if (other == player) continue;

                    if (check(other) == player) {
                        getUserLayer().showWinner(other);
                        hasWon = true;
                        break gameLoop;
                    }
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
