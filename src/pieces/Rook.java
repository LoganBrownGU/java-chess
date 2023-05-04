package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Rook extends Piece {

    private ArrayList<Coordinate> castlingMoves = new ArrayList<>();

    @Override
    public void setPosition(Coordinate position) {
        if (castlingMoves.contains(position) && board.getUserLayer().dialogue("Would you like to castle?").equals("y")) {
            Piece sovereign = this.getPlayer().getSovereign();
            int direction = this.getPosition().x < sovereign.getPosition().x ? -1 : 1;
            sovereign.setPosition(new Coordinate(sovereign.getPosition().x + 2 * direction, sovereign.getPosition().y));
        }

        super.setPosition(position);
    }

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();

        // todo uncomment this when castling works
        //moves.addAll(this.rankMoves());
        moves.addAll(this.fileMoves());

        // castling
        // todo decide if castling should be with sovereign or king specifically

        Piece sovereign = this.getPlayer().getSovereign();
        // first, check if there are pieces between king and rook and if either have moved
        if (sovereign.getPosition().lineOfSight(this.getPosition(), board) && !this.hasMoved() && !sovereign.hasMoved()) {
            // now check if any squares between king and rook are under attack
            ArrayList<Coordinate> coordsBetween = this.getPosition().coordsBetween(sovereign.getPosition(), board);
            ArrayList<Coordinate> allMoves = new ArrayList<>();

            for (Piece piece : board.getPieces())
                if (piece.getPlayer() != this.getPlayer())
                    allMoves.addAll(piece.possibleMoves());

            boolean underAttack = false;
            for (Coordinate move : allMoves)
                if (coordsBetween.contains(move)) {
                    underAttack = true;
                    break;
                }

            // now we can add castling as a move
            if (!underAttack) {
                int direction = this.getPosition().x < sovereign.getPosition().x ? -1 : 1;
                Coordinate move = new Coordinate(sovereign.getPosition().x - direction, sovereign.getPosition().y);
                castlingMoves.add(move);
                moves.add(move);
            }
        }

        return board.sanitiseMoves(moves, this);
    }

    public Rook(Player player, Coordinate position, Board board) {
        super(player, position, PieceType.ROOK, board, "rk");
    }
}
