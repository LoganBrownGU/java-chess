package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

import java.util.ArrayList;

public class Pawn extends Piece {

    // todo pawn seems to be able to jump over other pieces on first go
    // todo pawn seems to disappear sometimes: probably something to do with en passant

    public final int direction;
    public final int promotionRank;
    private boolean takingEnPassant;

    public boolean doubleFirstMove() {
        boolean firstMove = this.getPreviousMoves().size() == 2;
        boolean doubleMove = Math.abs(this.getPosition().y - this.getPreviousMoves().get(0).y) == 2;
        return firstMove && doubleMove;
    }

    @Override
    public void setPosition(Coordinate position) {

        // if promoting
        if (position.y == this.promotionRank) {
            //String str = board.getUserLayer().dialogue("What piece would you like to promote to?");
            String str = "QUEEN";
            Piece piece = PieceFactory.promotePawn(str, this);
            board.removePiece(this);
            piece.setPosition(position);
            board.addPiece(piece);
        }

        // if taking en passant
        if (takingEnPassant) {
            board.removePiece(board.pieceAt(new Coordinate(position.x, position.y - this.direction)));
            takingEnPassant = false;
        }

        super.setPosition(position);
    }

    @Override
    public ArrayList<Coordinate> possibleMoves() {
        ArrayList<Coordinate> moves = new ArrayList<>();
        Coordinate position = this.getPosition();

        // moving forward
        Coordinate test = new Coordinate(position.x, position.y + direction);
        if (board.pieceAt(test) == null) moves.add(test);
        test = new Coordinate(test.x, test.y + direction);
        if (!this.hasMoved() && board.pieceAt(test) == null) moves.add(test);

        // taking diagonally
        test = new Coordinate(position.x + 1, position.y + 1);
        if (board.pieceAt(test) != null && board.pieceAt(test).getPlayer() != this.getPlayer()) moves.add(test);
        test = new Coordinate(position.x - 1, position.y + 1);
        if (board.pieceAt(test) != null && board.pieceAt(test).getPlayer() != this.getPlayer()) moves.add(test);

        // en passant
        Piece rightPiece = board.pieceAt(new Coordinate(this.getPosition().x + 1, this.getPosition().y));
        Piece leftPiece = board.pieceAt(new Coordinate(this.getPosition().x - 1, this.getPosition().y));
        boolean pawnRight = rightPiece instanceof Pawn && rightPiece.getPlayer() != this.getPlayer();
        boolean pawnLeft = leftPiece instanceof Pawn && leftPiece.getPlayer() != this.getPlayer();
        if (pawnRight && ((Pawn) rightPiece).doubleFirstMove()) {
            moves.add(new Coordinate(rightPiece.getPosition().x, rightPiece.getPosition().y + this.direction));
            takingEnPassant = true;
        } if (pawnLeft && ((Pawn) leftPiece).doubleFirstMove()) {
            moves.add(new Coordinate(leftPiece.getPosition().x, leftPiece.getPosition().y + this.direction));
            takingEnPassant = true;
        }

        return board.sanitiseMoves(moves, this);
    }

    public Pawn(Player player, Coordinate position, int direction, Board board) {
        super(player, position, PieceType.PAWN, board, "pn");
        this.direction = direction;
        this.promotionRank = direction == 1 ? board.maxY - 1 : 0;
    }
}
