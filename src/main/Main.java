package main;

import pieces.Piece;

public class Main {
    public static void main(String[] args) {

        Board board = BoardFactory.standardBoard();
        board.updateObservers();
        Piece piece = board.getPlayers().get(0).getPiece();
        Coordinate newCoords = board.getPlayers().get(0).getMove(piece);
        System.out.println(newCoords);
        piece.setPosition(newCoords);
    }
}