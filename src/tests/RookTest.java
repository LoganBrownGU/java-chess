package pieces;

import board.Board;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import players.HumanPlayer;
import players.Player;

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

    @Test
    public void castling() {
        Board board = new StandardGameBoard();
        Player player = new HumanPlayer('a', board);
        Piece rook = new Rook(player, new Coordinate(0, 0), board);
        board.addPiece(rook);
        Piece king = new King(player, new Coordinate(4, 0), board);
        board.addPiece(king);
        player.setSovereign(king);
        board.setUserLayerActive(true);
        board.updateUserLayer();

        assertTrue(rook.getPosition().lineOfSight(king.getPosition(), board));
        assertTrue(rook.possibleMoves().contains(new Coordinate(3, 0)));
    }
}