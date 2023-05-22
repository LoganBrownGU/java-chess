package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

public class G3DUserLayer implements UserLayer {

    // todo camera update shouldn't be tied to framerate

    private boolean active = false;
    private Thread displayThread;
    private DisplayUpdater updater;
    private final Object lock = new Object();
    private Board board;

    protected void endGame() {
        System.exit(0);
    }

    @Override
    public Piece getPiece(Player p) {
        updater.setSelectingPiece(true);
        updater.clearSelected();
        synchronized (updater) {
            while (updater.getSelectedPiece() == null) {
                try {
                    updater.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Piece selected = updater.getSelectedPiece();
        System.out.println(selected);
        updater.setSelectingPiece(false);
        return selected;
    }

    @Override
    public Coordinate getMove() {
        updater.setSelectingSquare(true);
        updater.clearSelectedSquare();
        synchronized (updater) {
            try {
                updater.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Coordinate movePosition = updater.getSelectedSquare();
        updater.setSelectingSquare(false);

        if (movePosition == null) {
            updater.setSelectingPiece(false);
            updater.setSelectingSquare(false);
            updater.clearSelected();
            updater.clearSelectedSquare();
            return null;
        }

        return movePosition;
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public String getPromotion() {
        PromotionDialogueMenu pm = new PromotionDialogueMenu(100, 30, lock);

        synchronized (lock) {
            while (pm.getPiece() == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return pm.getPiece().toUpperCase();
    }

    @Override
    public boolean confirmCastling() {
        CastlingDialogueMenu cm = new CastlingDialogueMenu(100, 100, lock);

        synchronized (lock) {
            while (!cm.isFinished()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return cm.getConfirm();
    }

    @Override
    public void update() {
        if (!active) return;

        if (!displayThread.isAlive())
            displayThread.start();
        else if (displayThread.isInterrupted())
            displayThread.start();

        updater.clearCheckmarkLocation();
    }

    @Override
    public void showWinner(Player winner) {

    }

    @Override
    public void showCheck(Player checking, Player checked) {
        updater.setCheckmarkLocation(checked.getSovereign().getPosition());
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;

        if (active) {
            updater = new DisplayUpdater(this.board, 2, this);
            displayThread = new Thread(updater);
        } else {
            displayThread.interrupt();
        }
    }

    public G3DUserLayer() {

    }
}