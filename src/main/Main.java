package main;

import board.Board;
import board.SaveGame;
import userlayers.CommandLineUserLayer;

public class Main {
    public static void main(String[] args) {

        //Board board = BoardFactory.checkTest(new CommandLineUserLayer());
        Board board = SaveGame.loadGame("assets/savegames/save.xml", new CommandLineUserLayer());

        board.updateUserLayer();
        board.start();

        SaveGame.saveGame("assets/savegames/save.xml", board);
    }
}