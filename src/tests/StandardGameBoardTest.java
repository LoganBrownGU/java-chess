package tests;

import board.Board;
import board.BoardFactory;
import board.StandardGameBoard;
import org.junit.jupiter.api.Test;
import userlayers.CommandLineUserLayer;

import static org.junit.jupiter.api.Assertions.*;

class StandardGameBoardTest {

    @Test
    void canBlock() {
        StandardGameBoard board = BoardFactory.standardBoard(new CommandLineUserLayer());
        board.canBlock(null, null);
    }
}