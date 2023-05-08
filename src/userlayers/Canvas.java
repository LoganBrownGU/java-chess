package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Canvas extends java.awt.Canvas {
    public final Color backgroundColour, squareColour;
    private final HashMap<String, Image> icons = new HashMap<>();
    private final Image checkmark;
    private Graphics2D g;
    private int divSize;

    public void paint(Graphics g) {}

    public void loadIcon(String iconName) {
        if (icons.get(iconName) != null) return;

        try {
            File f = new File("assets/default_icons/" + iconName + ".png");
            Image image = ImageIO.read(f);
            icons.put(iconName, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Coordinate coordinateOf(MouseEvent e) {
        int x = e.getX() / divSize;
        int y = e.getY() / divSize;

        return new Coordinate(x, y);
    }

    private void drawSquares(Graphics2D g, int max, int divSize, Piece highlighted) {
        g.setColor(this.squareColour);
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

    private void drawPieces(Graphics2D g, int divSize, Board board) {
        for (Piece piece : board.getPieces()) {
            String iconName = piece.getPlayer().representation + piece.representation;
            BufferedImage image = (BufferedImage) icons.get(iconName);

            if (image == null)
                loadIcon(iconName);

            g.drawImage(image, piece.getPosition().x * divSize, piece.getPosition().y * divSize, divSize, divSize, null);
        }
    }

    public void drawBoard(Player checked, Board board, Piece highlighted) {
        if (this.g == null) this.g = (Graphics2D) this.getGraphics();

        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        int max = Math.max(board.maxX, board.maxY);
        divSize = (int) Math.ceil((float) this.getWidth() / max);
        drawSquares(g, max, divSize, highlighted);
        drawPieces(g, divSize, board);

        if (checked != null) {
            int x = checked.getSovereign().getPosition().x;
            int y = checked.getSovereign().getPosition().y;
            g.drawImage(checkmark, divSize * x + divSize / 20, divSize * y + divSize / 20, divSize / 3, divSize / 3, null);
        }
    }

    public Canvas(int width, int height, Color squareColour, Color backgroundColour) {
        this.setBackground(backgroundColour);
        this.setSize(width, height);
        this.backgroundColour = backgroundColour;
        this.squareColour = squareColour;

        try {
            checkmark = ImageIO.read(new File("assets/checkmark.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDivSize(int divSize) {
        this.divSize = divSize;
    }
}
