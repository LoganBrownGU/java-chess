package userlayers;

import board.Board;
import gui.GUIMaster;
import main.Coordinate;
import pieces.Piece;
import players.Player;
import userlayers.graphics3d.DisplayUpdater;
import userlayers.graphics3d.Service;

public class G3DUserLayer implements UserLayer {

    // todo camera update shouldn't be tied to framerate
    // todo make camera abstract
    // todo read in graphics settings from config file

    private boolean active = false;
    private Thread displayThread;
    private DisplayUpdater updater;
    private final Object lock = new Object();
    private Board board;

    public void endGame() {
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

        synchronized (updater) {
            updater.addService((updater) -> {
                GUIMaster.addFromFile("assets/gui/promotion.xml");
                GUIMaster.applyActionEventToGroup("promotion", (element) -> {
                    updater.promotedPiece = element.getId();
                    GUIMaster.removeGroup(element.getGroup());
                    synchronized (updater) {updater.notify();}
                });
            });

            while (updater.promotedPiece.equals("")) {
                try {
                    updater.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return updater.promotedPiece;
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
        updater.setWinner(winner);
    }

    @Override
    public void showCheck(Player checking, Player checked) {
        updater.setCheckmarkLocation(checked.getSovereign().getPosition());
    }

    @Override
    public void showPieceTaken(Piece takenPiece) {
        updater.updateTakenPieces(takenPiece);
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