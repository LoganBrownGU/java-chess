package pieces;

import main.Coordinate;

import java.util.ArrayList;

public interface PieceStrategy {
    boolean move(Coordinate coords);

    ArrayList<Coordinate> possibleMoves();
}
