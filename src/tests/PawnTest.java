package tests;

import board.Board;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import pieces.Pawn;
import pieces.Queen;
import players.HumanPlayer;
import players.Player;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    @Test
    void promotion() {
        Board board = new StandardGameBoard();
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 6), 1, board);
        board.addPiece(pawn);
        pawn.setPosition(new Coordinate(0, 7));
        assertTrue(board.pieceAt(new Coordinate(0, 7)) instanceof Queen);
    }

}