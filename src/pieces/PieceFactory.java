package pieces;

import board.Board;
import main.Coordinate;
import players.Player;

public class PieceFactory {
    public static Piece pieceFromString(String str, Player player, Coordinate position, Board board) {
        Piece piece;
        switch (str) {
            case "ROOK" -> piece = new Rook(player, position, board);
            case "QUEEN" -> piece = new Queen(player, position, board);
            case "KNIGHT" -> piece = new Knight(player, position, board);
            case "KING" -> piece = new King(player, position, board);
            case "BISHOP" -> piece = new Bishop(player, position, board);
            default -> throw new RuntimeException("piece " + str + " not recognised");
        }

        return piece;
    }

    public static Piece promotePawn(String str, Pawn pawn) {
        return pieceFromString(str, pawn.getPlayer(), pawn.getPosition(), pawn.getPlayer().board);
    }

    public static Piece clonePiece(Piece piece, Player newPlayer, Board newBoard) {
        Piece clone = null;

        switch (piece.getType()) {
            case PAWN -> clone = new Pawn(newPlayer, piece.getPosition(), ((Pawn) piece).direction, newBoard);
            case ROOK -> clone = new Rook(newPlayer, piece.getPosition(), newBoard);
            case QUEEN -> clone = new Queen(newPlayer, piece.getPosition(), newBoard);
            case KNIGHT -> clone = new Knight(newPlayer, piece.getPosition(), newBoard);
            case KING -> clone = new King(newPlayer, piece.getPosition(), newBoard);
            case BISHOP -> clone = new Bishop(newPlayer, piece.getPosition(), newBoard);
        }

        return clone;
    }
}
