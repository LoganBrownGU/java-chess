package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

import java.util.Scanner;

public class G3DUserLayer implements UserLayer {

    // todo camera update shouldn't be tied to framerate

    private boolean active = false;
    private Thread displayThread;
    private final Object lock = new Object();
    private Board board;
    private final Scanner sc = new Scanner(System.in);

    protected void endGame() {
        System.exit(0);
    }

    @Override
    public Piece getPiece(Player p) {
        Coordinate piecePosition = null;
        Piece piece;

        while ((piece = board.pieceAt(piecePosition)) == null || piecePosition == null) {
            System.out.print(p.representation + ": Enter position of piece to move: ");
            String input = sc.nextLine();
            piecePosition = Coordinate.chessCoordToCoordinate(input);
        }

        return piece;
    }

    @Override
    public Coordinate getMove() {
        Coordinate movePosition = null;

        while (movePosition == null) {
            System.out.print("Enter position to move piece to: ");
            String input = sc.nextLine();
            movePosition = Coordinate.chessCoordToCoordinate(input);
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
                } catch (InterruptedException e) { throw new RuntimeException(e); }
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
            displayThread = new Thread(new DisplayUpdater(this.board, 2, this));
        } else {
            displayThread.interrupt();
        }
    }

    public G3DUserLayer() {

    }
}