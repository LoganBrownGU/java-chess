package players;

import main.Board;
import main.Coordinate;
import pieces.Piece;

public class HumanPlayer extends Player {

    @Override
    public Piece getPiece() {
        return board.getUserLayer().getPiece();
    }

    @Override
    public Coordinate getMove(Piece pieceToMove) {
        return board.getUserLayer().getMove(pieceToMove);
    }

    public HumanPlayer(char representation, Board board) {
        super(representation, board);
    }
}
