package players;

import board.Board;
import main.Coordinate;
import pieces.Piece;

import java.util.ArrayList;

public class HumanPlayer extends Player {

    @Override
    public Piece getPiece() {
        Piece pieceToMove = null;

        while (pieceToMove == null)
            pieceToMove = board.getUserLayer().getPiece(this);


        return pieceToMove;
    }

    @Override
    public Coordinate getMove(Piece pieceToMove) {
        Coordinate movePosition = null;

        // userLayer.getMove should never return null EXCEPT when the player cancels a move
        // if the player does cancel a move, getMove will return null.
        while (movePosition == null) {
            movePosition = board.getUserLayer().getMove();
            if (movePosition == null) break;
        }

        return movePosition;
    }

    public HumanPlayer(char representation, Board board) {
        super(representation, board);
    }
}
