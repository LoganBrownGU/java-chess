package board;

import main.Coordinate;
import pieces.*;
import players.BadAIPlayer;
import players.HumanPlayer;
import players.Player;
import players.PlayerType;
import userlayers.UserLayer;

public class BoardFactory {

    private static StandardGameBoard standardBoard(StandardGameBoard board, Player player1, Player player2) {

        // add sovereigns
        player2.setSovereign(new King(player2, new Coordinate(3, 7), board));
        player1.setSovereign(new King(player1, new Coordinate(4, 0), board));

        // add rooks
        new Rook(player1, new Coordinate(0, 0), board);
        new Rook(player1, new Coordinate(7, 0), board);
        new Rook(player2, new Coordinate(0, 7), board);
        new Rook(player2, new Coordinate(7, 7), board);

        // add knights
        new Knight(player1, new Coordinate(1, 0), board);
        new Knight(player1, new Coordinate(6, 0), board);
        new Knight(player2, new Coordinate(1, 7), board);
        new Knight(player2, new Coordinate(6, 7), board);

        // add bishops
        new Bishop(player1, new Coordinate(2, 0), board);
        new Bishop(player1, new Coordinate(5, 0), board);
        new Bishop(player2, new Coordinate(2, 7), board);
        new Bishop(player2, new Coordinate(5, 7), board);

        // add queens
        new Queen(player1, new Coordinate(3, 0), board);
        new Queen(player2, new Coordinate(4, 7), board);

        //add pawns
        for (int i = 0; i < 8; i++) {
            new Pawn(player1, new Coordinate(i, 1), 1, board);
            new Pawn(player2, new Coordinate(i, 6), -1, board);
        }

        board.setUserLayerActive(true);

        return board;
    }

    public static StandardGameBoard standardBoard(UserLayer userLayer) {
        StandardGameBoard board = new StandardGameBoard(userLayer);

        Player player1 = new HumanPlayer('w', board);
        Player player2 = new HumanPlayer('b', board);


        return standardBoard(board, player1, player2);
    }

    public static StandardGameBoard standardBoardAgainstBadAI(UserLayer userLayer) {
        StandardGameBoard board = new StandardGameBoard(userLayer);

        Player player1 = new HumanPlayer('w', board);
        Player player2 = new BadAIPlayer('b', board);

        return standardBoard(board, player1, player2);
    }

    public static StandardGameBoard castlingTest(UserLayer userLayer) {
        StandardGameBoard board = new StandardGameBoard(userLayer);
        Player player = new HumanPlayer('b', board);

        new Rook(player, new Coordinate(0, 7), board);
        Sovereign king = new King(player, new Coordinate(3, 7), board);
        player.setSovereign(king);

        player = new HumanPlayer('w', board);

        new Rook(player, new Coordinate(0, 0), board);
        king = new King(player, new Coordinate(3, 0), board);
        player.setSovereign(king);


        board.setUserLayerActive(true);
        board.updateUserLayer();

        return board;
    }

    public static StandardGameBoard promotionTest(UserLayer userLayer) {
        StandardGameBoard board = new StandardGameBoard(userLayer);
        Player player = new HumanPlayer('w', board);

        Pawn pawn = new Pawn(player, new Coordinate(1, 6), 1, board);

        board.setUserLayerActive(true);

        return board;
    }

    public static StandardGameBoard checkTest(UserLayer userLayer) {
        StandardGameBoard board = standardBoard(userLayer);

        board.removePiece(board.pieceAt(new Coordinate(4, 6)));
        board.pieceAt(new Coordinate(5, 1)).setPosition(new Coordinate(4, 5));

        return board;
    }

    public static StandardGameBoard standardBoardFromPlayers(PlayerType player1, PlayerType player2, UserLayer userLayer) {
        StandardGameBoard board = new StandardGameBoard(userLayer);

        Player white = player1.toPlayer('w', board);
        Player black = player2.toPlayer('b', board);

        return standardBoard(board, white, black);
    }

    public static StandardGameBoard cloneBoard(Board board) {
        StandardGameBoard clone = new StandardGameBoard();

        for (int i = 0; i < board.getPlayers().size(); i++) {
            Player pc = board.getPlayers().get(i);
            Player playerClone = new Player(pc.representation, clone, pc.type) {
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
                    if (piece == pc.getSovereign()) playerClone.setSovereign((Sovereign) clonePiece);
                }
            }

        }

        return clone;
    }

    public static StandardGameBoard hugeBoard(UserLayer userLayer) {
        StandardGameBoard board = new StandardGameBoard(userLayer, 16, 8);

        Player player1 = new HumanPlayer('w', board);
        Player player2 = new HumanPlayer('b', board);

        player2.setSovereign(new King(player2, new Coordinate(9, 7), board));
        player1.setSovereign(new King(player1, new Coordinate(6, 0), board));

        // add rooks
        new Rook(player1, new Coordinate(0, 0), board);
        new Rook(player1, new Coordinate(1, 0), board);
        new Rook(player1, new Coordinate(14, 0), board);
        new Rook(player1, new Coordinate(15, 0), board);
        new Rook(player2, new Coordinate(0, 7), board);
        new Rook(player2, new Coordinate(1, 7), board);
        new Rook(player2, new Coordinate(14, 7), board);
        new Rook(player2, new Coordinate(15, 7), board);

        // add knights
        new Knight(player1, new Coordinate(2, 0), board);
        new Knight(player1, new Coordinate(3, 0), board);
        new Knight(player1, new Coordinate(12, 0), board);
        new Knight(player1, new Coordinate(13, 0), board);
        new Knight(player2, new Coordinate(2, 7), board);
        new Knight(player2, new Coordinate(3, 7), board);
        new Knight(player2, new Coordinate(12, 7), board);
        new Knight(player2, new Coordinate(13, 7), board);


        // add bishops
        new Bishop(player1, new Coordinate(4, 0), board);
        new Bishop(player1, new Coordinate(5, 0), board);
        new Bishop(player1, new Coordinate(10, 0), board);
        new Bishop(player1, new Coordinate(11, 0), board);
        new Bishop(player1, new Coordinate(7, 0), board);
        new Bishop(player2, new Coordinate(4, 7), board);
        new Bishop(player2, new Coordinate(5, 7), board);
        new Bishop(player2, new Coordinate(10, 7), board);
        new Bishop(player2, new Coordinate(11, 7), board);
        new Bishop(player2, new Coordinate(8, 7), board);

        // add queens
        new Queen(player1, new Coordinate(8, 0), board);
        new Queen(player1, new Coordinate(9, 0), board);
        new Queen(player2, new Coordinate(6, 7), board);
        new Queen(player2, new Coordinate(7, 7), board);

        //add pawns
        for (int i = 0; i < 16; i++) {
            new Pawn(player1, new Coordinate(i, 1), 1, board);
            new Pawn(player2, new Coordinate(i, 6), -1, board);
        }

        board.setUserLayerActive(true);


        return board;
    }
}
