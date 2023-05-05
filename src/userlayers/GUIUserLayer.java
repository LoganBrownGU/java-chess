package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import pieces.PieceType;
import players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class GUIUserLayer implements UserLayer, MouseListener, MouseMotionListener {

    // chess icons: <a href="https://iconscout.com/icon-pack/chess" target="_blank">Free Chess Icon Pack</a> on <a href="https://iconscout.com">IconScout</a>

    private static class Canvas extends java.awt.Canvas {

        public final Color backgroundColour, squareColour;

        public void paint(Graphics g) {
        }

        public Canvas(int size, Color backgroundColour, Color squareColour) {
            this.setBackground(backgroundColour);
            this.setSize(size, size);
            this.backgroundColour = backgroundColour;
            this.squareColour = squareColour;
        }
    }

    private Canvas canvas;
    private Graphics2D g;
    private Board board;
    private static volatile MouseEvent mouseEvent = null;
    private int divSize, max;
    private Piece highlighted = null;
    private HashMap<String, Image> icons = new HashMap<>();
    private Image checkmark;
    private boolean moving = false;
    private Player checked = null, checking = null;

    private static final Object lock = new Object();

    private void drawSquares(Graphics2D g, int max, int divSize) {
        g.setColor(canvas.squareColour);
        for (int i = 0; i < max; i++) {
            for (int j = i % 2 == 0 ? 0 : 1; j < max; j += 2)
                g.fillRect(i * divSize, j * divSize, divSize, divSize);
        }

        if (highlighted == null) return;

        g.setColor(Color.red);
        g.fillRect(highlighted.getPosition().x * divSize, highlighted.getPosition().y * divSize, divSize, divSize);

        g.setColor(Color.yellow);
        Composite defaultComposite = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, .5f));
        for (Coordinate coord : highlighted.possibleMoves())
            g.fillRect(coord.x * divSize, coord.y * divSize, divSize, divSize);
        g.setComposite(defaultComposite);
    }

    private void drawPieces(Graphics2D g, int max, int divSize) {
        for (Piece p : board.getPieces()) {
            String iconName = p.getPlayer().representation + p.representation;
            BufferedImage image = (BufferedImage) icons.get(iconName);

            if (image == null) {
                try {
                    File f = new File("assets/default_icons/" + iconName + ".png");
                    image = ImageIO.read(f);
                    icons.put(iconName, image);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            g.drawImage(image, p.getPosition().x * divSize, p.getPosition().y * divSize, divSize, divSize, null);
        }
    }

    private void drawBoard() {
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        max = Math.max(board.maxX, board.maxY);
        divSize = (int) Math.ceil((float) canvas.getWidth() / max);
        drawSquares(g, max, divSize);
        drawPieces(g, max, divSize);

        if (this.checked != null) {
            int x = checked.getSovereign().getPosition().x;
            int y = checked.getSovereign().getPosition().y;
            g.drawImage(checkmark, divSize * x + divSize / 20, divSize * y + divSize / 20, divSize / 3, divSize / 3, null);
        }
    }

    private Coordinate coordinateOf(MouseEvent e) {
        int x = e.getX() / divSize;
        int y = e.getY() / divSize;

        return new Coordinate(x, y);
    }

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

    @Override
    public Piece getPiece(Player p) {
        if (p.equals(checking)) {
            checked = null;
            checking = null;
            update();
        }

        waitForMouse();

        Coordinate coord = coordinateOf(mouseEvent);
        highlighted = board.pieceAt(coord);
        drawBoard();

        return board.pieceAt(coord);
    }

    @Override
    public Coordinate getMove() {
        moving = true;
        MouseEvent e = waitForMouse();

        highlighted = null;
        moving = false;
        return coordinateOf(e);
    }

    @Override
    public void update() {
        drawBoard();
    }

    @Override
    public void showWinner(Player winner) {
        String message = winner + " wins!";

        g.setFont(new Font("Helvetica", Font.BOLD, 36));
        FontMetrics metrics = g.getFontMetrics();
        int x = (canvas.getWidth() - metrics.stringWidth(message)) / 2;
        int y = (metrics.getAscent() + (canvas.getHeight() - (metrics.getAscent() + metrics.getDescent())) / 2);

        g.setColor(Color.white);
        g.fill3DRect(x - 20, y - metrics.getAscent() - 10, metrics.stringWidth(message) + 40, metrics.getDescent() + metrics.getAscent() + 20, true);

        g.setColor(canvas.backgroundColour);
        g.drawString(message, x, y);

        canvas.removeMouseListener(this);
        canvas.removeMouseMotionListener(this);
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

        Piece piece = board.pieceAt(coordinateOf(e));

        if (piece != null && piece != highlighted) {
            highlighted = board.pieceAt(coordinateOf(e));
            drawBoard();
        }
    }

    public GUIUserLayer() {
        Frame frame = new Frame("Chess");
        int size = (int) (Math.min(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height) * 0.8);
        canvas = new Canvas((int) (size * 0.9), new Color(0x30, 0x30, 0x30), new Color(0xd0, 0xd0, 0xd0));
        canvas.setLocation((int) (size * 0.05), (int) (size * 0.05));
        frame.add(canvas);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        frame.setLayout(null);
        frame.setSize(size, size);
        frame.setVisible(true);

        g = ((Graphics2D) canvas.getGraphics());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        try {
            checkmark = ImageIO.read(new File("assets/checkmark.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
