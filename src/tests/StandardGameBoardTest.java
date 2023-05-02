package tests;

import board.Board;
import board.BoardFactory;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import pieces.Piece;
import players.Player;
import userlayers.CommandLineUserLayer;

import static org.junit.jupiter.api.Assertions.*;

class StandardGameBoardTest {

    @Test
    void canBlock() {
        StandardGameBoard board = BoardFactory.standardBoard(new CommandLineUserLayer());
        board.canBlock(null, null);
    }

    @Test
    void cloneWorks() {
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
}