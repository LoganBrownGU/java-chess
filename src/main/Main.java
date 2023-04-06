package main;

import pieces.Pawn;
import pieces.Piece;
import players.HumanPlayer;

public class Main {
    public static void main(String[] args) {

        Board board = BoardFactory.standardBoard();
        board.updateUserLayer();

        Piece piece = board.getPlayers().get(0).getPiece();
        System.out.println(piece);
        Coordinate newCoords = board.getPlayers().get(0).getMove(piece);
        System.out.println(newCoords);
        piece.setPosition(newCoords);

        piece = board.getPlayers().get(0).getPiece();
        newCoords = board.getPlayers().get(0).getMove(piece);
        piece.setPosition(newCoords);
    }
}