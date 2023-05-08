package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

import java.awt.*;
import java.awt.event.*;

public class GUIUserLayer implements UserLayer, MouseListener, MouseMotionListener {

    // chess icons: <a href="https://iconscout.com/icon-pack/chess" target="_blank">Free Chess Icon Pack</a> on <a href="https://iconscout.com">IconScout</a>

    // todo checkmark doesnt disappear when king goes out of check

    private final Frame frame;
    private final Canvas boardCanvas, takenPieceCanvas;
    private final Graphics2D g;
    private Board board;
    private static MouseEvent mouseEvent = null;
    private Piece highlighted = null;
    private boolean moving = false;
    private Player checked = null, checking = null;
    private static final Object lock = new Object();
    private boolean active = false;

    private MouseEvent waitForMouse() {
        mouseEvent = null;

        synchronized (lock) {
            while (mouseEvent == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return mouseEvent;
    }

    private void showMessage(String message) {
        g.setFont(new Font("Helvetica", Font.BOLD, 36));
        FontMetrics metrics = g.getFontMetrics();
        int x = (boardCanvas.getWidth() - metrics.stringWidth(message)) / 2;
        int y = (metrics.getAscent() + (boardCanvas.getHeight() - (metrics.getAscent() + metrics.getDescent())) / 2);

        g.setColor(Color.white);
        g.fill3DRect(x - 20, y - metrics.getAscent() - 10, metrics.stringWidth(message) + 40, metrics.getDescent() + metrics.getAscent() + 20, true);

        g.setColor(boardCanvas.backgroundColour);
        g.drawString(message, x, y);
    }

    @Override
    public Piece getPiece(Player p) {
        if (p.equals(checking)) {
            checked = null;
            checking = null;
            update();
        }

        waitForMouse();

        Coordinate coord = boardCanvas.coordinateOf(mouseEvent);
        highlighted = board.pieceAt(coord);
        boardCanvas.drawBoard(checked, board, highlighted);

        return board.pieceAt(coord);
    }

    @Override
    public Coordinate getMove() {
        moving = true;
        MouseEvent e = waitForMouse();

        highlighted = null;
        moving = false;
        return boardCanvas.coordinateOf(e);
    }

    @Override
    public void update() {
        if (!active) return;

        boardCanvas.drawBoard(checked, board, highlighted);
    }

    @Override
    public void showWinner(Player winner) {
        String message = winner + " wins!";

        showMessage(message);

        boardCanvas.removeMouseListener(this);
        boardCanvas.removeMouseMotionListener(this);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        frame.dispose();
    }

    @Override
    public void showCheck(Player checking, Player checked) {
        this.checked = checked;
        this.checking = checking;
        update();
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
        this.boardCanvas.setDivSize((int) Math.ceil((float) boardCanvas.getWidth() / Math.max(board.maxX, board.maxY)));

        takenPieceCanvas.setSize(boardCanvas.getDivSize() * 3, boardCanvas.getHeight());
        frame.setSize(takenPieceCanvas.getWidth() + boardCanvas.getWidth() + boardCanvas.getX(), frame.getHeight());
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
    public void setActive(boolean active) {
        this.active = active;

        if (active) {
            showMessage("loading...");
            for (Piece piece: board.getPieces()) {
                String iconName = piece.getPlayer().representation + piece.representation;
                boardCanvas.loadIcon(iconName);
            }

            boardCanvas.addMouseMotionListener(this);
            boardCanvas.addMouseListener(this);

            update();
        } else {
            boardCanvas.removeMouseListener(this);
            boardCanvas.removeMouseMotionListener(this);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseEvent = e;

        synchronized (lock) { lock.notify(); }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (moving) return;

        Piece piece = board.pieceAt(boardCanvas.coordinateOf(e));

        if (piece != null && piece != highlighted) {
            highlighted = board.pieceAt(boardCanvas.coordinateOf(e));
            boardCanvas.drawBoard(checked, board, highlighted);
        }
    }

    public GUIUserLayer() {
        frame = new Frame("Chess");
        int size = (int) (Math.min(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height) * 0.8);

        boardCanvas = new Canvas((int) (size * .9f), (int) (size * .9f), new Color(0xd0, 0xd0, 0xd0), new Color(0x30, 0x30, 0x30));
        boardCanvas.setLocation((int) (size * 0.05), (int) (size * 0.05));
        frame.add(boardCanvas);

        takenPieceCanvas = new Canvas(0, 0, new Color(0xd0, 0xd0, 0xd0), new Color(0xe5, 0xe5, 0xcc));
        takenPieceCanvas.setLocation(boardCanvas.getWidth(), boardCanvas.getY());
        frame.add(takenPieceCanvas);

        frame.setLayout(null);
        frame.setSize(takenPieceCanvas.getWidth() + boardCanvas.getWidth() + 2 * boardCanvas.getX(), size);
        frame.setVisible(true);

        g = ((Graphics2D) boardCanvas.getGraphics());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
