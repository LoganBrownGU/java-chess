package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

public class G3DUserLayer extends CommandLineUserLayer {

    private boolean active = false;
    private Thread displayThread;

    protected void endGame() {
        System.exit(0);
    }

    @Override
    public void update() {
        if (!active) return;

        if (!displayThread.isAlive())
            displayThread.start();
        else if (displayThread.isInterrupted())
            displayThread.start();
    }

    @Override
    public void showWinner(Player winner) {

    }

    @Override
    public void showCheck(Player checking, Player checked) {

    }

    @Override
    public void setActive(boolean active) {
        this.active = active;

        if (active) {
            displayThread = new Thread(new DisplayUpdater(super.getBoard(), 2, this));
        } else {
            displayThread.interrupt();
        }
    }

    public G3DUserLayer() {

    }
}