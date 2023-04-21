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

        Piece toTake = new Queen(new HumanPlayer('a', board), new Coordinate(0, 0), board);
        board.addPiece(toTake);
        Piece taker = new Rook(new HumanPlayer('b', board), new Coordinate(0, 5), board);

        assertTrue(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameRankTake() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board), new Coordinate(0, 0), board);
        board.addPiece(toTake);
        Piece taker = new Rook(new HumanPlayer('b', board), new Coordinate(5, 0), board);

        assertTrue(taker.possibleMoves().contains(toTake.getPosition()));
    }

    // todo write castling tests
}