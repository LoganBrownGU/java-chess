package tests;

import board.Board;
import board.BoardFactory;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import players.HumanPlayer;
import userlayers.CommandLineUserLayer;
import userlayers.DummyUserLayer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QueenTest {

    @Test
    public void testSameFileTake() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board, 1), new Coordinate(0, 0));
        toTake.register(board);
        board.addPiece(toTake);
        Piece taker = new Queen(new HumanPlayer('b', board, 1), new Coordinate(0, 5));
        taker.register(board);

        assertTrue(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameRankTake() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board, 1), new Coordinate(0, 0));
        toTake.register(board);
        board.addPiece(toTake);
        Piece taker = new Queen(new HumanPlayer('b', board, 1), new Coordinate(5, 0));
        taker.register(board);

        assertTrue(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameDiagonalTake() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board, 1), new Coordinate(0, 0));
        toTake.register(board);
        board.addPiece(toTake);
        Piece taker = new Queen(new HumanPlayer('b', board, 1), new Coordinate(5, 5));
        taker.register(board);

        assertTrue(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameFileTakePieceInWay() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board, 1), new Coordinate(0, 0));
        toTake.register(board);
        board.addPiece(toTake);
        Piece blocker = new Queen(new HumanPlayer('c', board, 1), new Coordinate(0,3));
        blocker.register(board);
        board.addPiece(blocker);
        Piece taker = new Queen(new HumanPlayer('b', board, 1), new Coordinate(0, 5));
        taker.register(board);

        assertFalse(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameRankTakePieceInWay() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board, 1), new Coordinate(0, 0));
        toTake.register(board);
        board.addPiece(toTake);
        Piece blocker = new Queen(new HumanPlayer('c', board, 1), new Coordinate(3,0));
        blocker.register(board);
        board.addPiece(blocker);
        Piece taker = new Queen(new HumanPlayer('b', board, 1), new Coordinate(5, 0));
        taker.register(board);

        assertFalse(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void testSameDiagonalTakePieceInWay() {
        Board board = new StandardGameBoard();

        Piece toTake = new Queen(new HumanPlayer('a', board, 1), new Coordinate(0, 0));
        toTake.register(board);
        board.addPiece(toTake);
        Piece blocker = new Queen(new HumanPlayer('c', board, 1), new Coordinate(3,3));
        blocker.register(board);
        board.addPiece(blocker);
        Piece taker = new Queen(new HumanPlayer('b', board, 1), new Coordinate(5, 5));
        taker.register(board);

        assertFalse(taker.possibleMoves().contains(toTake.getPosition()));
    }

    @Test
    public void recreateAllSameCoordBugFullBoard() {
        Board board = BoardFactory.standardBoard(new CommandLineUserLayer());

        Piece pieceToTake = board.pieceAt(new Coordinate(3, 1));
        pieceToTake.setPosition(new Coordinate(4, 3));
        board.removePiece(board.pieceAt(new Coordinate(4, 6)));
        board.updateUserLayer();
        Piece taker = board.pieceAt(new Coordinate(4, 7));

        ArrayList<Coordinate> moves = taker.diagonalMoves();

        assertTrue(taker.possibleMoves().contains(pieceToTake.getPosition()));
    }
}