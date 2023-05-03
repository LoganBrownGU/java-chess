package main;

import board.Board;
import board.BoardFactory;
import userlayers.CommandLineUserLayer;
import userlayers.GUIUserLayer;

public class Main {
    public static void main(String[] args) {

        Board board = BoardFactory.standardGameBoardAgainstBadAI(new GUIUserLayer());
        board.updateUserLayer();

        board.play();
    }
}