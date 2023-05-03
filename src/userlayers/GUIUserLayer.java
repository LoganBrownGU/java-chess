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
    private Board board;
    private static volatile MouseEvent mouseEvent = null;
    private int divSize, max;
    private Piece highlighted = null;
    private HashMap<String, Image> icons = new HashMap<>();

    private void drawSquares(Graphics g, int max, int divSize) {
        g.setColor(canvas.squareColour);
        for (int i = 0; i < max; i++) {
            for (int j = i % 2 == 0 ? 0 : 1; j < max; j += 2)
                g.fillRect(i * divSize, j * divSize, divSize, divSize);
        }

        if (highlighted != null) {
            g.setColor(Color.red);
            g.fillRect(highlighted.getPosition().x * divSize, highlighted.getPosition().y * divSize, divSize, divSize);
        }
    }

    private void drawPieces(Graphics g, int max, int divSize) {
        for (Piece p : board.getPieces()) {
            String iconName = p.getPlayer().representation + p.representation;
            Image image = icons.get(iconName);

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

    @Override
    public Piece getPiece(Player p) {
        mouseEvent = null;

        while (mouseEvent == null) {
            Thread.onSpinWait();
        }

        int x = mouseEvent.getX() / divSize;
        int y = mouseEvent.getY() / divSize;
        highlighted = board.pieceAt(new Coordinate(x, y));
        update();

        return board.pieceAt(new Coordinate(x, y));
    }

    @Override
    public Coordinate getMove() {
        mouseEvent = null;

        while (mouseEvent == null) {
            Thread.onSpinWait();
        }

        int x = mouseEvent.getX() / divSize;
        int y = mouseEvent.getY() / divSize;

        highlighted = null;
        return new Coordinate(x, y);
    }

    @Override
    public void update() {
        Graphics g = canvas.getGraphics();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        max = Math.max(board.maxX, board.maxY);
        divSize = (int) Math.ceil((float) canvas.getWidth() / max);
        drawSquares(g, max, divSize);
        drawPieces(g, max, divSize);
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
        int size = (int) (Math.min(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height) * 0.95);
        //canvas = new Canvas((int) (size * 0.9), new Color(0x00, 0x99, 0x33), new Color(0xff, 0xcc, 0x66));
        canvas = new Canvas((int) (size * 0.9), new Color(0x30, 0x30, 0x30), new Color(0xd0, 0xd0, 0xd0));
        canvas.setLocation((int) (size * 0.05), (int) (size * 0.05));
        frame.add(canvas);
        canvas.addMouseListener(this);
        frame.setLayout(null);
        frame.setSize(size, size);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
