package main;

import board.Board;
import board.BoardFactory;
import userlayers.CommandLineUserLayer;

public class Main {
    public static void main(String[] args) {

        Board board = BoardFactory.standardBoard(new CommandLineUserLayer());
        board.updateUserLayer();

        board.play();
    }
}