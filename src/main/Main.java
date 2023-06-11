package main;

import board.Board;
import board.BoardFactory;
import board.SaveGame;
import userlayers.CommandLineUserLayer;
import userlayers.G3DUserLayer;

public class Main {
    public static void main(String[] args) {

        Board board = SaveGame.loadGame("assets/savegames/save.xml", new CommandLineUserLayer());
        //Board board = BoardFactory.standardBoard(new CommandLineUserLayer());

        board.updateUserLayer();
        board.start();

        SaveGame.saveGame("assets/savegames/save.xml", board);
    }
}