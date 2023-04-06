package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;

public interface UserLayer {
    Piece getPiece();
    Coordinate getMove(Piece pieceToMove);

    void update();

    void setBoard(Board board);
}
