package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Rook extends Piece {

    private final ArrayList<Coordinate> castlingMoves = new ArrayList<>();

    @Override
    public void setPosition(Coordinate position) {
        if (castlingMoves.contains(position) && board.getUserLayer().confirmCastling()) {
            Piece sovereign = this.getPlayer().getSovereign();
            int direction = this.getPosition().x < sovereign.getPosition().x ? -1 : 1;
            sovereign.setPosition(new Coordinate(sovereign.getPosition().x + 2 * direction, sovereign.getPosition().y));
            castlingMoves.clear();
        }

        super.setPosition(position);
    }

    @Override
    public ArrayList<Coordinate> attackingMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();

        moves.addAll(this.rankMoves());
        moves.addAll(this.fileMoves());

        return board.sanitiseMoves(moves, this);
    }

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>(this.attackingMoves());

        Piece sovereign = this.getPlayer().getSovereign();
        // first, check if there are pieces between king and rook and if either have moved
        if (sovereign.getPosition().lineOfSight(this.getPosition(), board) && !this.hasMoved() && !sovereign.hasMoved()) {
            // now check if any squares between king and final king position are under attack
            int direction = this.getPosition().x < sovereign.getPosition().x ? -1 : 1;
            Coordinate newSovereignCoord = new Coordinate(sovereign.getPosition().x + 2 * direction, sovereign.getPosition().y);
            ArrayList<Coordinate> coordsBetween = newSovereignCoord.coordsBetween(sovereign.getPosition(), board);
            ArrayList<Coordinate> allMoves = new ArrayList<>();

            for (Piece piece : board.getPieces())
                if (piece.getPlayer() != this.getPlayer())
                    allMoves.addAll(piece.attackingMoves());

            boolean underAttack = false;
            for (Coordinate move : allMoves)
                if (coordsBetween.contains(move)) {
                    underAttack = true;
                    break;
                }

            // now we can add castling as a move
            if (!underAttack) {
                Coordinate move = new Coordinate(sovereign.getPosition().x + direction, sovereign.getPosition().y);
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
