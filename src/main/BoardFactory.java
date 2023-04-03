package main;

import pieces.*;

public class BoardFactory {
    public static Board standardBoard() {
        Board board = new Board(8, 8);

        // add rooks
        board.addPiece(new Rook(true, new Coordinate(0, 0)));
        board.addPiece(new Rook(true, new Coordinate(7, 0)));
        board.addPiece(new Rook(false, new Coordinate(0, 7)));
        board.addPiece(new Rook(false, new Coordinate(7, 7)));

        //add bishops
        board.addPiece(new Bishop(true, new Coordinate(1, 0)));
        board.addPiece(new Bishop(true, new Coordinate(7, 0)));

        return board;
    }
}
