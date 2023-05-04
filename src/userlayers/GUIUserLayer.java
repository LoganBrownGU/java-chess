package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GUIUserLayer implements UserLayer, MouseListener {

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

    private static final Object lock = new Object();

    /*private static BufferedImage addBorder(BufferedImage image) {
        BufferedImage result = new BufferedImage(
                image.getWidth() + 2, image.getHeight() + 2,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 1, 1, null);
        g.dispose();
        return result;
    }*/

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
        for (Coordinate coord : highlighted.possibleMoves())
            g.fillRect((int) (coord.x * divSize + 0.05 * divSize), (int) (coord.y * divSize + divSize * 0.05), (int) (divSize * 0.9), (int) (divSize * 0.9));
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

            /*Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(image, 0, 0, divSize, divSize, null);*/
            /*AffineTransform transform = new AffineTransform();
            transform.scale((double) divSize / image.getWidth(), (double) divSize / image.getHeight());*/
            /*Image scaled = image.getScaledInstance(divSize, divSize, Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(divSize, divSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D imgGraphics = (Graphics2D) outputImage.getGraphics();
            imgGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            imgGraphics.drawImage(scaled, 0, 0, null);*/

            BufferedImage outputImage = new BufferedImage(divSize, divSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D oig = (Graphics2D) outputImage.getGraphics();
            oig.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            oig.drawImage(image, AffineTransform.getScaleInstance((double) divSize / image.getWidth(), (double) divSize / image.getHeight()), null);

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //g.drawImage(image, p.getPosition().x * divSize, p.getPosition().y * divSize, divSize, divSize, null);
            g.drawImage(outputImage, p.getPosition().x * divSize, p.getPosition().y * divSize, null);
        }
    }

    private void waitForMouse() {
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
    }

    @Override
    public Piece getPiece(Player p) {
        waitForMouse();

        int x = mouseEvent.getX() / divSize;
        int y = mouseEvent.getY() / divSize;
        highlighted = board.pieceAt(new Coordinate(x, y));
        update();

        return board.pieceAt(new Coordinate(x, y));
    }

    @Override
    public Coordinate getMove() {
        waitForMouse();

        int x = mouseEvent.getX() / divSize;
        int y = mouseEvent.getY() / divSize;

        highlighted = null;
        return new Coordinate(x, y);
    }

    @Override
    public void update() {
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        max = Math.max(board.maxX, board.maxY);
        divSize = (int) Math.ceil((float) canvas.getWidth() / max);
        drawSquares(g, max, divSize);
        drawPieces(g, max, divSize);
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
    }

    @Override
    public void showCheck(Player checking, Player checked) {
        int x = checked.getSovereign().getPosition().x;
        int y = checked.getSovereign().getPosition().y;

        g.drawImage(checkmark, divSize * x + divSize / 20, divSize * y + divSize / 20, divSize / 3, divSize / 3, null);
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public String dialogue(String message) {
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseEvent = e;

        synchronized (lock) { lock.notify(); }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public GUIUserLayer() {
        Frame frame = new Frame("Chess");
        int size = (int) (Math.min(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height) * 0.5);
        //canvas = new Canvas((int) (size * 0.9), new Color(0x00, 0x99, 0x33), new Color(0xff, 0xcc, 0x66));
        canvas = new Canvas((int) (size * 0.9), new Color(0x30, 0x30, 0x30), new Color(0xd0, 0xd0, 0xd0));
        canvas.setLocation((int) (size * 0.05), (int) (size * 0.05));
        frame.add(canvas);
        canvas.addMouseListener(this);
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
