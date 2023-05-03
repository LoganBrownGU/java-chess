package tests;

import board.Board;
import board.BoardFactory;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import players.HumanPlayer;
import players.Player;
import userlayers.CommandLineUserLayer;

import static org.junit.jupiter.api.Assertions.*;

class StandardGameBoardTest {

    @Test
    void canBlock() {
        StandardGameBoard board = BoardFactory.standardBoard(new CommandLineUserLayer());
        //board.canBlock(null, null);
    }

    @Test
    void cloneNoneSame() {
        StandardGameBoard board = BoardFactory.standardBoard(new CommandLineUserLayer());
        StandardGameBoard clone = BoardFactory.cloneBoard(board);
        clone.getPieces().get(0).setPosition(new Coordinate(5, 5));
        board.updateUserLayer();

        for (Piece oldPiece : board.getPieces()) {
            for (Piece clonePiece : clone.getPieces()) {
                assertNotSame(oldPiece, clonePiece);
            }
        }

        for (Player oldPlayer : board.getPlayers()) {
            for (Player clonePlayer : clone.getPlayers()) {
                assertNotSame(oldPlayer, clonePlayer);
            }
        }
    }

    @Test
    void cloneAreEqual() {
        StandardGameBoard board = BoardFactory.standardBoard(new CommandLineUserLayer());
        StandardGameBoard clone = BoardFactory.cloneBoard(board);

        for (Piece piece : board.getPieces()) {
            boolean oneEqual = false;
            for (Piece clonePiece : clone.getPieces()) {
                if (piece.equals(clonePiece)) {
                    if (oneEqual) throw new AssertionFailedError("more than one piece equal to " + piece);
                    oneEqual = true;
                }
            }
            assertTrue(oneEqual);
        }
    }

    @Test
    void checkmateHasCheckmate() {
        StandardGameBoard board = new StandardGameBoard();
        Player player1 = new HumanPlayer('a', board);
        Player player2 = new HumanPlayer('b', board);
        board.addPlayer(player1);
        board.addPlayer(player2);
        Piece king = new King(player1, new Coordinate(0, 0), board);
        player1.setSovereign(king);

        board.addPiece(new Pawn(player1, new Coordinate(1, 0), 1, board));
        board.addPiece(new Knight(player1, new Coordinate(0, 1), board));
        board.addPiece(new Pawn(player1, new Coordinate(1, 1), 1, board));
        board.addPiece(king);
        board.addPiece(new Knight(player2, new Coordinate(1, 2), board));
        board.setUserLayerActive(true);
        board.updateUserLayer();

        assertSame(board.check(player2), player1);
        assertFalse(board.canBlock(player2.representation, player1.representation));
    }

    @Test
    void checkmateHasNoCheckmate() {
        StandardGameBoard board = new StandardGameBoard();
        Player player1 = new HumanPlayer('a', board);
        Player player2 = new HumanPlayer('b', board);
        board.addPlayer(player1);
        board.addPlayer(player2);
        Piece king = new King(player1, new Coordinate(0, 0), board);
        player1.setSovereign(king);

        board.addPiece(new Knight(player1, new Coordinate(0, 1), board));
        board.addPiece(new Pawn(player1, new Coordinate(1, 1), 1, board));
        board.addPiece(king);
        board.addPiece(new Knight(player2, new Coordinate(1, 2), board));
        board.setUserLayerActive(true);
        board.updateUserLayer();

        assertSame(board.check(player2), player1);
        assertTrue(board.canBlock(player2.representation, player1.representation));
    }
}