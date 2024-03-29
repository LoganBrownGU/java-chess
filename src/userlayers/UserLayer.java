package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

public interface UserLayer {
    Piece getPiece(Player p);
    Coordinate getMove();

    void update();

    void setBoard(Board board);

    void showWinner(Player winner);
    void showCheck(Player checking, Player checked);
    void showPieceTaken(Piece takenPiece);

    String getPromotion();
    boolean confirmCastling();

    void setActive(boolean active);
}
