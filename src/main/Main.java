package main;

import board.Board;
import board.BoardFactory;
import players.PlayerType;
import userlayers.GUIUserLayer;

public class Main {
    public static void main(String[] args) {

        Board board = BoardFactory.standardBoardFromPlayers(PlayerType.HUMAN, PlayerType.BAD_AI, new GUIUserLayer());
        board.updateUserLayer();

        board.play();
    }
}