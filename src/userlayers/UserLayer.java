package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

public interface UserLayer {
    Piece getPiece(Player p);
    Coordinate getMove(Piece pieceToMove);

    void update();

    void setBoard(Board board);

    String dialogue(String message);
}
