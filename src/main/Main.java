package main;

import board.Board;
import board.BoardFactory;
import userlayers.CommandLineUserLayer;
import userlayers.GUIUserLayer;

public class Main {
    public static void main(String[] args) {

        Board board = BoardFactory.standardBoard(new GUIUserLayer());
        board.updateUserLayer();
        /*board.removePiece(board.pieceAt(new Coordinate(0, 6)));
        board.removePiece(board.pieceAt(new Coordinate(0, 1)));
        board.removePiece(board.pieceAt(new Coordinate(3, 0)));
        board.removePiece(board.pieceAt(new Coordinate(2, 0)));
        board.removePiece(board.pieceAt(new Coordinate(1, 0)));*/

        board.play();
    }
}