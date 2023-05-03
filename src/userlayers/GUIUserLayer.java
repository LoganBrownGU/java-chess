package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUIUserLayer implements UserLayer {

    private static class Canvas extends java.awt.Canvas {
        public void paint(Graphics g) {}

        public Canvas(int size) {
            this.setBackground(Color.BLACK);
            this.setSize(size, size);
        }
    }

    private Canvas canvas;
    private Board board;

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
        Graphics g = canvas.getGraphics();
        int max = Math.max(board.maxX, board.maxY);
        int divSize = canvas.getWidth() / max;
        int lineThickness = divSize / 30;

        g.setColor(Color.white);
        for (int i = 1; i < max; i++)
            g.fillRect(i * divSize, 0, lineThickness, canvas.getHeight());
        for (int i = 1; i < max; i++)
            g.fillRect(0, i * divSize, canvas.getWidth(), lineThickness);


    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public String dialogue(String message) {
        return null;
    }

    public GUIUserLayer() {
        Frame f = new Frame("Chess");
        int size = (int) (Math.min(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height) * 0.95);
        canvas = new Canvas((int) (size * 0.9));
        canvas.setLocation((int) (size * 0.05), (int) (size * 0.05));
        f.add(canvas);
        f.setLayout(null);
        f.setSize(size, size);
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
