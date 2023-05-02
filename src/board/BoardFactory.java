package board;

import main.Coordinate;
import pieces.*;
import players.HumanPlayer;
import players.Player;
import userlayers.UserLayer;

public class BoardFactory {
    public static StandardGameBoard standardBoard(UserLayer userLayer) {
        StandardGameBoard board = new StandardGameBoard(userLayer);

        Player black = new HumanPlayer('b', board);
        Player white = new HumanPlayer('w', board);
        black.setSovereign(new King(black, new Coordinate(3, 7), board));
        white.setSovereign(new King(white, new Coordinate(4, 0), board));

        board.addPlayer(white);
        board.addPlayer(black);

        // add rooks
        board.addPiece(new Rook(white, new Coordinate(0, 0), board));
        board.addPiece(new Rook(white, new Coordinate(7, 0), board));
        board.addPiece(new Rook(black, new Coordinate(0, 7), board));
        board.addPiece(new Rook(black, new Coordinate(7, 7), board));

        // add knights
        board.addPiece(new Knight(white, new Coordinate(1, 0), board));
        board.addPiece(new Knight(white, new Coordinate(6, 0), board));
        board.addPiece(new Knight(black, new Coordinate(1, 7), board));
        board.addPiece(new Knight(black, new Coordinate(6, 7), board));

        // add bishops
        board.addPiece(new Bishop(white, new Coordinate(2, 0), board));
        board.addPiece(new Bishop(white, new Coordinate(5, 0), board));
        board.addPiece(new Bishop(black, new Coordinate(2, 7), board));
        board.addPiece(new Bishop(black, new Coordinate(5, 7), board));

        // add queens
        board.addPiece(new Queen(white, new Coordinate(3, 0), board));
        board.addPiece(new Queen(black, new Coordinate(4, 7), board));

        // add kings
        board.addPiece(black.getSovereign());
        board.addPiece(white.getSovereign());
        
        //add pawns
        for (int i = 0; i < 8; i++) {
            board.addPiece(new Pawn(white, new Coordinate(i, 1), 1, board));
            board.addPiece(new Pawn(black, new Coordinate(i, 6), -1, board));
        }

        return board;
    }

    public static StandardGameBoard cloneBoard(Board board) {
        StandardGameBoard clone = new StandardGameBoard();
        Player playerClones[] = new Player[board.getPlayers().size()];
        for (int i = 0; i < playerClones.length; i++) {
            Player pc = board.getPlayers().get(i);
            playerClones[i] = new Player(pc.representation, clone) {
                @Override
                public Piece getPiece() {
                    return null;
                }

                @Override
                public Coordinate getMove(Piece pieceToMove) {
                    return null;
                }
            };
            for (Piece piece: board.getPieces()) {
                if (piece.getPlayer() == board.getPlayers().get(i))
                    clone.addPiece(PieceFactory.clonePiece(piece, playerClones[i], clone));
            }
        }

        return clone;
    }
}
