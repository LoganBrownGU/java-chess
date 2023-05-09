package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

public class G3DUserLayer implements UserLayer {

    private Board board;
    private boolean active = false;
    private Thread displayThread;

    private void addPieces() {
        if (!displayThread.isAlive())
            displayThread.start();
        else if (displayThread.isInterrupted())
            displayThread.start();

    }

    @Override
    public Piece getPiece(Player p) {
        return board.getPieces().get(0);
    }

    @Override
    public Coordinate getMove() {
        return null;
    }

    @Override
    public void update() {
        if (!active) return;

        addPieces();
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public void showWinner(Player winner) {

    }

    @Override
    public void showCheck(Player checking, Player checked) {

    }

    @Override
    public String getPromotion() {
        return null;
    }

    @Override
    public boolean confirmCastling() {
        return false;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;

        if (active) {
            displayThread = new Thread(new DisplayUpdater(board, 2));
        } else {
            displayThread.interrupt();
        }
    }

    public G3DUserLayer() {

    }
}