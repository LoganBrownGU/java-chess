package pieces;

import main.Coordinate;

import java.util.ArrayList;

public interface PieceStrategy {

    ArrayList<Coordinate> possibleMoves();
}
