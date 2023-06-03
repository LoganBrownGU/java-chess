package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Pawn extends Piece {

    public final int direction;
    public final int promotionRank;
    private final ArrayList<Coordinate> enPassantMoves = new ArrayList<>();

    public boolean doubleFirstMove() {
        boolean firstMove = this.getPreviousMoves().size() == 2;
        boolean doubleMove = Math.abs(this.getPosition().y - this.getPreviousMoves().get(0).y) == 2;
        return firstMove && doubleMove;
    }

    @Override
    public void setPosition(Coordinate position) {

        // if promoting
        if (position.y == this.promotionRank) {
            String str = null;
            // todo this will tank performance; REMOVE
            while (str == null)
                str = board.getUserLayer().getPromotion();
            Piece piece = PieceFactory.promotePawn(str.toUpperCase(), this);
            board.removePiece(this);
            piece.setPosition(position);
        }

        // if taking en passant
        if (enPassantMoves.contains(position)) {
            board.removePiece(board.pieceAt(new Coordinate(position.x, position.y - this.direction)));
            enPassantMoves.clear();
        }

        super.setPosition(position);
    }

    @Override
    public ArrayList<Coordinate> attackingMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();
        Coordinate position = this.getPosition();

        // taking diagonally
        Coordinate test = new Coordinate(position.x + 1, position.y + this.direction);
        if (board.pieceAt(test) != null && board.pieceAt(test).getPlayer() != this.getPlayer()) moves.add(test);
        test = new Coordinate(position.x - 1, position.y + this.direction);
        if (board.pieceAt(test) != null && board.pieceAt(test).getPlayer() != this.getPlayer()) moves.add(test);

        // en passant
        Piece rightPiece = board.pieceAt(new Coordinate(this.getPosition().x + 1, this.getPosition().y));
        Piece leftPiece = board.pieceAt(new Coordinate(this.getPosition().x - 1, this.getPosition().y));
        boolean pawnRight = rightPiece instanceof Pawn && rightPiece.getPlayer() != this.getPlayer();
        boolean pawnLeft = leftPiece instanceof Pawn && leftPiece.getPlayer() != this.getPlayer();
        if (pawnRight && ((Pawn) rightPiece).doubleFirstMove() && board.getLastMove() == rightPiece.getPosition()) {
            Coordinate move = new Coordinate(rightPiece.getPosition().x, rightPiece.getPosition().y + this.direction);
            moves.add(move);
            enPassantMoves.add(move);
        } if (pawnLeft && ((Pawn) leftPiece).doubleFirstMove() && board.getLastMove() == leftPiece.getPosition()) {
            Coordinate move = new Coordinate(leftPiece.getPosition().x, leftPiece.getPosition().y + this.direction);
            moves.add(move);
            enPassantMoves.add(move);
        }

        return board.sanitiseMoves(moves, this);
    }

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>(this.attackingMoves());
        Coordinate position = this.getPosition();

        // moving forward
        Coordinate test = new Coordinate(position.x, position.y + direction);
        if (board.pieceAt(test) == null) {
            // can only move forward two if space in front of pawn is empty
            moves.add(test);
            test = new Coordinate(test.x, test.y + direction);
            if (this.hasNotMoved() && board.pieceAt(test) == null) moves.add(test);
        }

        return board.sanitiseMoves(moves, this);
    }

    public Pawn(Player player, Coordinate position, int direction, Board board) {
        super(player, position, PieceType.PAWN, board, "pn");
        this.direction = direction;
        this.promotionRank = direction == 1 ? board.maxY - 1 : 0;
    }
}
