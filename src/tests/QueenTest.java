package tests;

import board.Board;
import board.BoardFactory;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import players.HumanPlayer;
import players.Player;
import userlayers.CommandLineUserLayer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QueenTest {

    @Test
    public void testSameFileTake() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board), new Coordinate(0, 0), board);
        Piece taker = new Queen(new HumanPlayer('b', board), new Coordinate(0, 5), board);

        assertTrue(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameRankTake() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board), new Coordinate(0, 0), board);
        Piece taker = new Queen(new HumanPlayer('b', board), new Coordinate(5, 0), board);

        assertTrue(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameDiagonalTake() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board), new Coordinate(0, 0), board);
        Piece taker = new Queen(new HumanPlayer('b', board), new Coordinate(5, 5), board);

        assertTrue(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameFileTakePieceInWay() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board), new Coordinate(0, 0), board);
        Piece blocker = new Queen(new HumanPlayer('c', board), new Coordinate(0,3), board);
        Piece taker = new Queen(new HumanPlayer('b', board), new Coordinate(0, 5), board);

        assertFalse(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameRankTakePieceInWay() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board), new Coordinate(0, 0), board);
        Piece blocker = new Queen(new HumanPlayer('c', board), new Coordinate(3,0), board);
        Piece taker = new Queen(new HumanPlayer('b', board), new Coordinate(5, 0), board);

        assertFalse(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameDiagonalTakePieceInWay() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board), new Coordinate(0, 0), board);
        Piece blocker = new Queen(new HumanPlayer('c', board), new Coordinate(3,3), board);
        Piece taker = new Queen(new HumanPlayer('b', board), new Coordinate(5, 5), board);


        assertFalse(taker.possibleMoves().contains(toTake.getPosition()));
    }

    /*@Test
    public void recreateAllSameCoordBugFullBoard() {
        Board board = BoardFactory.standardBoard(new CommandLineUserLayer());

        Piece pieceToTake = board.pieceAt(new Coordinate(3, 1));
        board.addPiece(pieceToTake);
        pieceToTake.setPosition(new Coordinate(4, 3));
        board.removePiece(board.pieceAt(new Coordinate(4, 6)));
        board.updateUserLayer();
        Piece taker = board.pieceAt(new Coordinate(4, 7));

        ArrayList<Coordinate> moves = taker.diagonalMoves();

        assertTrue(taker.possibleMoves().contains(pieceToTake.getPosition()));
    }
*/
    @Test
    void cantTakeOwnPiece() {
        StandardGameBoard board = BoardFactory.standardBoard(new CommandLineUserLayer());
        Piece queen = board.pieceAt(new Coordinate(4, 7));
        System.out.println(queen);
        assertFalse(queen.possibleMoves().contains(board.pieceAt(new Coordinate(4, 6)).getPosition()));
    }

    @Test
    public void recreateWrongCheckBug() {
        StandardGameBoard board = BoardFactory.standardBoard(new CommandLineUserLayer());
        board.removePiece(board.pieceAt(new Coordinate(3, 1)));
        board.removePiece(board.pieceAt(new Coordinate(4, 6)));

        Piece queen = board.pieceAt(new Coordinate(4, 7));
        queen.setPosition(new Coordinate(4, 4));
        Player player3 = new HumanPlayer('z', board);
        player3.setSovereign(new King(player3, new Coordinate(2, 2), board));
        new Pawn(player3, new Coordinate(4, 6), 1, board);
        ArrayList<Coordinate> moves = queen.possibleMoves();
        assertNull(board.check(queen.getPlayer()));
    }

    @Test
    void noOutOfBoundsMoves() {
        StandardGameBoard board = new StandardGameBoard();
        Piece queen = new Queen(new HumanPlayer('a', board), new Coordinate(0, 0), board);

        ArrayList<Coordinate> coords = queen.possibleMoves();
        for (Coordinate coord : coords) {
            assertTrue(coord.x >= 0 && coord.x <= board.maxX);
            assertTrue(coord.y >= 0 && coord.y <= board.maxY);
        }
    }
}