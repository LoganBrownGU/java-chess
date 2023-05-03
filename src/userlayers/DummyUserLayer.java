package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

public class DummyUserLayer implements UserLayer {

    @Override
    public Piece getPiece(Player p) {
        return null;
    }

    @Override
    public Coordinate getMove() {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public void setBoard(Board board) {

    }

    @Override
    public String dialogue(String message) {
        return null;
    }
}
