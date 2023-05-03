package tests;

import board.Board;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import players.HumanPlayer;
import players.Player;
import userlayers.UserLayer;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    @Test
    void promotion() {
        Board board = new StandardGameBoard(new UserLayer() {
            @Override
            public Piece getPiece(Player p) {
                return null;
            }

            @Override
            public Coordinate getMove() {
                return null;
            }

            @Override
            public void update() {

            }

            @Override
            public void setBoard(Board board) {

            }

            @Override
            public String dialogue(String message) {
                return "QUEEN";
            }
        });
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 6), 1, board);
        board.addPiece(pawn);
        pawn.setPosition(new Coordinate(0, 7));
        assertTrue(board.pieceAt(new Coordinate(0, 7)) instanceof Queen);
    }

    @Test
    void cantMoveBack() {
        Board board = new StandardGameBoard();
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 6), 1, board);
        board.addPiece(pawn);

        assertFalse(pawn.possibleMoves().contains(new Coordinate(0, 5)));
    }

    @Test
    void cantTakeNoPiece() {
        Board board = new StandardGameBoard();
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 0), 1, board);
        board.addPiece(pawn);

        assertFalse(pawn.possibleMoves().contains(new Coordinate(1, 1)));
    }

    @Test
    void cantTakeSamePlayer() {
        Board board = new StandardGameBoard();
        Player player1 = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player1, new Coordinate(0, 0), 1, board);
        board.addPiece(pawn);
        Pawn toTake = new Pawn(player1, new Coordinate(1, 1), 1, board);
        board.addPiece(toTake);

        assertFalse(pawn.possibleMoves().contains(new Coordinate(1, 1)));
    }

    @Test
    void cantTakeBackwards() {
        Board board = new StandardGameBoard();
        Player player1 = new HumanPlayer('a', board);
        Player player2 = new HumanPlayer('b', board);
        Pawn pawn = new Pawn(player1, new Coordinate(1, 1), 1, board);
        board.addPiece(pawn);
        Pawn toTake = new Pawn(player2, new Coordinate(0, 0), 1, board);
        board.addPiece(toTake);

        assertFalse(pawn.possibleMoves().contains(new Coordinate(0, 0)));
    }

    @Test
    void canTake() {
        Board board = new StandardGameBoard();
        Player player1 = new HumanPlayer('a', board);
        Player player2 = new HumanPlayer('b', board);
        Pawn pawn = new Pawn(player1, new Coordinate(0, 0), 1, board);
        board.addPiece(pawn);
        Pawn toTake = new Pawn(player2, new Coordinate(1, 1), 1, board);
        board.addPiece(toTake);

        assertTrue(pawn.possibleMoves().contains(new Coordinate(1, 1)));
    }

    @Test
    void cantMoveDoubleAfterFirstMove() {
        Board board = new StandardGameBoard();
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 0), 1, board);
        board.addPiece(pawn);

        pawn.setPosition(new Coordinate(0, 1));
        assertFalse(pawn.possibleMoves().contains(new Coordinate(0, 3)));
    }

    @Test
    void cloneWorks() {
        StandardGameBoard board = new StandardGameBoard();
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 0), 1, board);


    }
}