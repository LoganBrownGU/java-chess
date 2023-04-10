package pieces;

import board.Board;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import players.HumanPlayer;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    @Test
    public void testSameFileTake() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board, 1), new Coordinate(0, 0));
        toTake.register(board);
        board.addPiece(toTake);
        Piece taker = new Rook(new HumanPlayer('b', board, 1), new Coordinate(0, 5));
        taker.register(board);

        assertTrue(taker.possibleMoves().contains(toTake.getPosition()));
    }
}