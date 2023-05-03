package board;

import main.Coordinate;
import pieces.Piece;
import players.Player;

import java.util.ArrayList;

public interface BoardStrategy {
    void play();
    ArrayList<Coordinate> sanitiseMoves(ArrayList<Coordinate> moves, Piece piece);
}
