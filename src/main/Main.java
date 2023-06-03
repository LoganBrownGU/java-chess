package main;

import board.Board;
import board.BoardFactory;
import players.PlayerType;
import userlayers.CommandLineUserLayer;
import userlayers.G3DUserLayer;
import userlayers.GUIUserLayer;

public class Main {
    public static void main(String[] args) {

        Board board = BoardFactory.checkTest(new G3DUserLayer());
        board.updateUserLayer();

        board.play();
    }
}