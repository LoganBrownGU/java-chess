package userlayers;

import main.Coordinate;
import observer.Observer;
import pieces.Piece;

public interface UserLayer extends Observer {
    Piece getPiece();
    Coordinate getMove(Piece pieceToMove);
}
