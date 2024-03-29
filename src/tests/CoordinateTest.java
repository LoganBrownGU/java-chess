package tests;

import board.Board;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import pieces.*;
import players.HumanPlayer;
import players.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void lineOfSightHasLineOfSightSameFile() {
        Board board = new StandardGameBoard();

        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(0, 5);
        assertTrue(c1.lineOfSight(c2, board));
    }

    @Test
    void lineOfSightHasLineOfSightSameRank() {
        Board board = new StandardGameBoard();

        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(5, 0);

        assertTrue(c1.lineOfSight(c2, board));
    }

    @Test
    void lineOfSightHasLineOfSightSameDiagonal() {
        Board board = new StandardGameBoard();

        // file
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(5, 5);

        assertTrue(c1.lineOfSight(c2, board));
    }


    @Test
    void lineOfSightNoLineOfSightSameFile() {
        Board board = new StandardGameBoard();
        Player testPlayer = new HumanPlayer('a', board);
        Piece testPiece = new Pawn(testPlayer, new Coordinate(0, 2), 1, board);

        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(0, 5);
        assertFalse(c1.lineOfSight(c2, board));
    }

    @Test
    void lineOfSightNoLineOfSightSameRank() {
        Board board = new StandardGameBoard();
        Player testPlayer = new HumanPlayer('a', board);
        Piece testPiece = new Pawn(testPlayer, new Coordinate(2, 0), 1, board);

        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(5, 0);
        assertFalse(c1.lineOfSight(c2, board));
    }

    @Test
    void lineOfSightNoLineOfSightSameDiagonal() {
        Board board = new StandardGameBoard();
        Player testPlayer = new HumanPlayer('a', board);
        Piece testPiece = new Pawn(testPlayer, new Coordinate(2, 2), 1, board);

        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(5, 5);
        assertFalse(c1.lineOfSight(c2, board));
    }

    @Test
    void lineOfSightSameCoord() {
        Board board = new StandardGameBoard();

        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(0, 0);

        assertTrue(c1.lineOfSight(c2, board));
    }

    @Test
    void coordsBetweenSameCoordPieceAtCoord() {
        Board board = new StandardGameBoard();
        Player testPlayer = new HumanPlayer('a', board);
        Piece testPiece = new Pawn(testPlayer, new Coordinate(0, 0), 1, board);

        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(0, 0);

        ArrayList<Coordinate> coords = c1.coordsBetween(c2, board);
        assertTrue(coords.contains(new Coordinate(0, 0)));
    }

    @Test
    void coordsBetweenSameFile() {
        Board board = new StandardGameBoard();
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(0, 3);
        Coordinate c3 = new Coordinate(0, 7);

        ArrayList<Coordinate> coords1 = new ArrayList<>(); // c1.coordsBetween(c2, board);
        coords1.add(new Coordinate(0, 0));
        coords1.add(new Coordinate(0, 1));
        coords1.add(new Coordinate(0, 2));
        coords1.add(new Coordinate(0, 3));

        ArrayList<Coordinate> coords2 = new ArrayList<>(); //c2.coordsBetween(c3, board);
        coords2.add(new Coordinate(0, 3));
        coords2.add(new Coordinate(0, 4));
        coords2.add(new Coordinate(0, 5));
        coords2.add(new Coordinate(0, 6));
        coords2.add(new Coordinate(0, 7));

        assertTrue(c1.coordsBetween(c2, board).containsAll(coords1));
        assertTrue(c2.coordsBetween(c3, board).containsAll(coords2));

        assertTrue(c2.coordsBetween(c1, board).containsAll(coords1));
        assertTrue(c3.coordsBetween(c2, board).containsAll(coords2));
    }

    @Test
    void coordsBetweenSameRank() {
        Board board = new StandardGameBoard();
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(3, 0);
        Coordinate c3 = new Coordinate(7, 0);

        ArrayList<Coordinate> coords1 = new ArrayList<>();
        coords1.add(new Coordinate(0, 0));
        coords1.add(new Coordinate(1, 0));
        coords1.add(new Coordinate(2, 0));
        coords1.add(new Coordinate(3, 0));

        ArrayList<Coordinate> coords2 = new ArrayList<>();
        coords2.add(new Coordinate(3, 0));
        coords2.add(new Coordinate(4, 0));
        coords2.add(new Coordinate(5, 0));
        coords2.add(new Coordinate(6, 0));
        coords2.add(new Coordinate(7, 0));

        assertTrue(c1.coordsBetween(c2, board).containsAll(coords1));
        assertTrue(c2.coordsBetween(c3, board).containsAll(coords2));

        assertTrue(c2.coordsBetween(c1, board).containsAll(coords1));
        assertTrue(c3.coordsBetween(c2, board).containsAll(coords2));
    }

    @Test
    void coordsBetweenSameDiagonal() {
        Board board = new StandardGameBoard();
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(3, 3);
        Coordinate c3 = new Coordinate(7, 7);

        ArrayList<Coordinate> coords1 = new ArrayList<>();
        coords1.add(new Coordinate(0, 0));
        coords1.add(new Coordinate(1, 1));
        coords1.add(new Coordinate(2, 2));
        coords1.add(new Coordinate(3, 3));

        ArrayList<Coordinate> coords2 = new ArrayList<>();
        coords2.add(new Coordinate(3, 3));
        coords2.add(new Coordinate(4, 4));
        coords2.add(new Coordinate(5, 5));
        coords2.add(new Coordinate(6, 6));
        coords2.add(new Coordinate(7, 7));

        assertTrue(c1.coordsBetween(c2, board).containsAll(coords1));
        assertTrue(c2.coordsBetween(c3, board).containsAll(coords2));

        assertTrue(c2.coordsBetween(c1, board).containsAll(coords1));
        assertTrue(c3.coordsBetween(c2, board).containsAll(coords2));
    }

    @Test
    public void recreateCastlingBug() {
        Board board = new StandardGameBoard();
        Player player = new HumanPlayer('a', board);
        Piece rook = new Rook(player, new Coordinate(0, 0), board);
        Sovereign king = new King(player, new Coordinate(4, 0), board);
        player.setSovereign(king);
        board.setUserLayerActive(true);
        board.updateUserLayer();

        assertTrue(rook.getPosition().lineOfSight(king.getPosition(), board));
    }

    /*@Test
    public void recreateAllSameCoordBugFullBoard() {
        Board board = BoardFactory.standardBoard(new CommandLineUserLayer());

        Piece pieceToTake = board.pieceAt(new Coordinate(3, 1));
        pieceToTake.setPosition(new Coordinate(4, 3));
        board.removePiece(board.pieceAt(new Coordinate(4, 6)));
        board.updateUserLayer();
        Piece taker = board.pieceAt(new Coordinate(4, 7));

        ArrayList<Coordinate> coords = taker.getPosition().coordsBetween(new Coordinate(taker.getPosition().x, 0), board);
        assertTrue(taker.getPosition().lineOfSight(new Coordinate(4, 5), board));
        assertTrue(coords.contains(pieceToTake.getPosition()));
        assertFalse(coords.contains(board.pieceAt(new Coordinate(4, 1)).getPosition()));
    }*/
}