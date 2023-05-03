package board;

import main.Coordinate;
import pieces.*;
import players.BadAIPlayer;
import players.HumanPlayer;
import players.Player;
import userlayers.UserLayer;

public class BoardFactory {
    private static StandardGameBoard standardBoard(StandardGameBoard board, Player player1, Player player2) {

        player2.setSovereign(new King(player2, new Coordinate(3, 7), board));
        player1.setSovereign(new King(player1, new Coordinate(4, 0), board));

        board.addPlayer(player1);
        board.addPlayer(player2);

        // add rooks
        board.addPiece(new Rook(player1, new Coordinate(0, 0), board));
        board.addPiece(new Rook(player1, new Coordinate(7, 0), board));
        board.addPiece(new Rook(player2, new Coordinate(0, 7), board));
        board.addPiece(new Rook(player2, new Coordinate(7, 7), board));

        // add knights
        board.addPiece(new Knight(player1, new Coordinate(1, 0), board));
        board.addPiece(new Knight(player1, new Coordinate(6, 0), board));
        board.addPiece(new Knight(player2, new Coordinate(1, 7), board));
        board.addPiece(new Knight(player2, new Coordinate(6, 7), board));

        // add bishops
        board.addPiece(new Bishop(player1, new Coordinate(2, 0), board));
        board.addPiece(new Bishop(player1, new Coordinate(5, 0), board));
        board.addPiece(new Bishop(player2, new Coordinate(2, 7), board));
        board.addPiece(new Bishop(player2, new Coordinate(5, 7), board));

        // add queens
        board.addPiece(new Queen(player1, new Coordinate(3, 0), board));
        board.addPiece(new Queen(player2, new Coordinate(4, 7), board));

        // add kings
        board.addPiece(player2.getSovereign());
        board.addPiece(player1.getSovereign());

        //add pawns
        for (int i = 0; i < 8; i++) {
            board.addPiece(new Pawn(player1, new Coordinate(i, 1), 1, board));
            board.addPiece(new Pawn(player2, new Coordinate(i, 6), -1, board));
        }

        board.setUserLayerActive(true);

        return board;
    }
    public static StandardGameBoard standardBoard(UserLayer userLayer) {
        StandardGameBoard board = new StandardGameBoard(userLayer);

        Player player2 = new HumanPlayer('b', board);
        Player player1 = new HumanPlayer('w', board);


        return standardBoard(board, player1, player2);
    }

    public static StandardGameBoard standardGameBoardAgainstBadAI(UserLayer userLayer) {
        StandardGameBoard board = new StandardGameBoard(userLayer);

        Player player2 = new BadAIPlayer('b', board);
        Player player1 = new HumanPlayer('w', board);

        return standardBoard(board, player1, player2);
    }

    public static StandardGameBoard cloneBoard(Board board) {
        StandardGameBoard clone = new StandardGameBoard();

        for (int i = 0; i < board.getPlayers().size(); i++) {
            Player pc = board.getPlayers().get(i);
            Player playerClone = new Player(pc.representation, clone) {
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
                if (piece.getPlayer() == board.getPlayers().get(i)) {
                    Piece clonePiece = PieceFactory.clonePiece(piece, playerClone, clone);
                    clone.addPiece(clonePiece);
                    if (piece == pc.getSovereign()) playerClone.setSovereign(clonePiece);
                }
            }
            clone.addPlayer(playerClone);
        }

        return clone;
    }
}
