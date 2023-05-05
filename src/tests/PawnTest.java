package tests;

import board.Board;
import board.BoardFactory;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import pieces.Bishop;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import players.HumanPlayer;
import players.Player;
import userlayers.CommandLineUserLayer;
import userlayers.UserLayer;

import java.util.ArrayList;

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
            public void showWinner(Player winner) {

            }

            @Override
            public void showCheck(Player checking, Player checked) {

            }

            @Override
            public String getPromotion() {
                return "QUEEN";
            }

            @Override
            public boolean confirmCastling() {
                return false;
            }

            @Override
            public void setActive(boolean active) {

            }
        });
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 6), 1, board);
        pawn.setPosition(new Coordinate(0, 7));
        assertTrue(board.pieceAt(new Coordinate(0, 7)) instanceof Queen);
    }

    @Test
    void cantMoveBack() {
        Board board = new StandardGameBoard();
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 6), 1, board);

        assertFalse(pawn.possibleMoves().contains(new Coordinate(0, 5)));
    }

    @Test
    void cantTakeNoPiece() {
        Board board = new StandardGameBoard();
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 0), 1, board);

        assertFalse(pawn.possibleMoves().contains(new Coordinate(1, 1)));
    }

    @Test
    void cantTakeSamePlayer() {
        Board board = new StandardGameBoard();
        Player player1 = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player1, new Coordinate(0, 0), 1, board);
        Pawn toTake = new Pawn(player1, new Coordinate(1, 1), 1, board);

        assertFalse(pawn.possibleMoves().contains(new Coordinate(1, 1)));
    }

    @Test
    void cantTakeBackwards() {
        Board board = new StandardGameBoard();
        Player player1 = new HumanPlayer('a', board);
        Player player2 = new HumanPlayer('b', board);
        Pawn pawn = new Pawn(player1, new Coordinate(1, 1), 1, board);
        Pawn toTake = new Pawn(player2, new Coordinate(0, 0), 1, board);

        assertFalse(pawn.possibleMoves().contains(new Coordinate(0, 0)));
    }

    @Test
    void canTake() {
        Board board = new StandardGameBoard();
        Player player1 = new HumanPlayer('a', board);
        Player player2 = new HumanPlayer('b', board);
        Pawn pawn = new Pawn(player1, new Coordinate(0, 0), 1, board);
        Pawn toTake = new Pawn(player2, new Coordinate(1, 1), 1, board);

        assertTrue(pawn.possibleMoves().contains(new Coordinate(1, 1)));
    }

    @Test
    void cantMoveDoubleAfterFirstMove() {
        Board board = new StandardGameBoard();
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 0), 1, board);

        pawn.setPosition(new Coordinate(0, 1));
        assertFalse(pawn.possibleMoves().contains(new Coordinate(0, 3)));
    }

    @Test
    void recreateTakingBug() {
        Board board = BoardFactory.standardBoard(new CommandLineUserLayer());
        Piece pawn1 = board.pieceAt(new Coordinate(1, 1));
        Piece pawn2 = board.pieceAt(new Coordinate(0, 6));
        pawn1.setPosition(new Coordinate(1, 3));
        pawn2.setPosition(new Coordinate(0, 4));

        assertTrue(pawn1.possibleMoves().contains(pawn2.getPosition()));
    }

    @Test
    void recreateTakingBug2() {
        Board board = BoardFactory.standardBoard(new CommandLineUserLayer());
        Piece pawn1 = board.pieceAt(new Coordinate(1, 1));
        Piece pawn2 = board.pieceAt(new Coordinate(0, 6));
        pawn1.setPosition(new Coordinate(1, 3));
        pawn2.setPosition(new Coordinate(0, 4));

        assertTrue(pawn2.possibleMoves().contains(pawn1.getPosition()));
    }

    @Test
    void enPassant() {
        Board board = BoardFactory.standardBoard(new CommandLineUserLayer());
        Piece pawn1 = board.pieceAt(new Coordinate(5, 1));
        Piece pawn2 = board.pieceAt(new Coordinate(4, 6));
        Piece pawn3 = board.pieceAt(new Coordinate(6, 6));

        pawn1.setPosition(new Coordinate(5, 3));
        pawn2.setPosition(new Coordinate(4, 4));
        pawn1.setPosition(new Coordinate(5, 4));
        pawn3.setPosition(new Coordinate(6, 4));
        ArrayList<Coordinate> moves = pawn1.possibleMoves();

        assertFalse(moves.contains(new Coordinate(pawn2.getPosition().x, pawn2.getPosition().y + 1)));
        assertTrue(moves.contains(new Coordinate(pawn3.getPosition().x, pawn3.getPosition().y + 1)));

        board = BoardFactory.standardBoard(new CommandLineUserLayer());
        pawn1 = board.pieceAt(new Coordinate(5, 6));
        pawn2 = board.pieceAt(new Coordinate(4, 1));
        pawn3 = board.pieceAt(new Coordinate(6, 1));

        pawn1.setPosition(new Coordinate(5, 4));
        pawn2.setPosition(new Coordinate(4, 3));
        pawn1.setPosition(new Coordinate(5, 3));
        pawn3.setPosition(new Coordinate(6, 3));
        moves = pawn1.possibleMoves();

        assertFalse(moves.contains(new Coordinate(pawn2.getPosition().x, pawn2.getPosition().y - 1)));
        assertTrue(moves.contains(new Coordinate(pawn3.getPosition().x, pawn3.getPosition().y - 1)));
    }

    @Test
    void recreateEnPassantBug() {
        Board board = BoardFactory.standardBoard(new CommandLineUserLayer());
        Piece pawn1 = board.pieceAt(new Coordinate(5, 1));
        Piece pawn2 = board.pieceAt(new Coordinate(4, 6));
        Piece pawn3 = board.pieceAt(new Coordinate(6, 6));

        pawn1.setPosition(new Coordinate(5, 3));
        pawn2.setPosition(new Coordinate(4, 4));
        pawn1.setPosition(new Coordinate(5, 4));
        pawn3.setPosition(new Coordinate(6, 4));
        ArrayList<Coordinate> moves = pawn1.possibleMoves();
        pawn1.setPosition(new Coordinate(5, 5));

        assertSame(board.pieceAt(pawn1.getPosition()), pawn1);
    }

    @Test
    void recreateJumpingBug() {
        Board board = BoardFactory.standardBoard(new CommandLineUserLayer());

        Pawn pawn = (Pawn) board.pieceAt(new Coordinate(0, 1));
        Bishop test = new Bishop(board.getPlayers().get(1), new Coordinate(0, 2), board);

        assertFalse(pawn.possibleMoves().contains(new Coordinate(0, 3)));
    }
}