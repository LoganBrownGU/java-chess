package board;

import main.Coordinate;
import pieces.*;
import players.HumanPlayer;
import players.Player;
import userlayers.UserLayer;

public class BoardFactory {
    public static Board standardBoard(UserLayer userLayer) {
        Board board = new StandardGameBoard();

        Player black = new HumanPlayer('b', board, -1);
        Player white = new HumanPlayer('w', board, 1);
        black.setSovereign(new King(black, new Coordinate(3, 7)));
        white.setSovereign(new King(white, new Coordinate(4, 0)));

        board.addPlayer(white);
        board.addPlayer(black);

        // add rooks
        board.addPiece(new Rook(white, new Coordinate(0, 0)));
        board.addPiece(new Rook(white, new Coordinate(7, 0)));
        board.addPiece(new Rook(black, new Coordinate(0, 7)));
        board.addPiece(new Rook(black, new Coordinate(7, 7)));

        // add knights
        board.addPiece(new Knight(white, new Coordinate(1, 0)));
        board.addPiece(new Knight(white, new Coordinate(6, 0)));
        board.addPiece(new Knight(black, new Coordinate(1, 7)));
        board.addPiece(new Knight(black, new Coordinate(6, 7)));

        // add bishops
        board.addPiece(new Bishop(white, new Coordinate(2, 0)));
        board.addPiece(new Bishop(white, new Coordinate(5, 0)));
        board.addPiece(new Bishop(black, new Coordinate(2, 7)));
        board.addPiece(new Bishop(black, new Coordinate(5, 7)));

        // add queens
        board.addPiece(new Queen(white, new Coordinate(3, 0)));
        board.addPiece(new Queen(white, new Coordinate(4, 7)));

        // add kings
        board.addPiece(black.getSovereign());
        board.addPiece(white.getSovereign());
        
        //add pawns
        for (int i = 0; i < 8; i++) {
            board.addPiece(new Pawn(white, new Coordinate(i, 1)));
            board.addPiece(new Pawn(black, new Coordinate(i, 6)));
        }

        for (Piece p: board.getPieces())
            p.register(board);

        board.setUserLayer(userLayer);
        userLayer.setBoard(board);

        return board;
    }
}
