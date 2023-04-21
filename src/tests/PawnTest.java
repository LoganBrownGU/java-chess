package tests;

import board.Board;
import board.StandardGameBoard;
import main.Coordinate;
import org.junit.jupiter.api.Test;
import pieces.Pawn;
import players.HumanPlayer;
import players.Player;
import userlayers.CommandLineUserLayer;

class PawnTest {

    @Test
    void promotion() {
        Board board = new StandardGameBoard();
        board.setUserLayer(new CommandLineUserLayer());
        Player player = new HumanPlayer('a', board);
        Pawn pawn = new Pawn(player, new Coordinate(0, 6), 1, board);
        pawn.setPosition(new Coordinate(0, 7));
    }

}