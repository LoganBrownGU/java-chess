package pieces;

import board.Board;
import players.Player;

public class PieceFactory {
    public static Piece promotePawn(String str, Pawn pawn) {

        return switch (str) {
            case "ROOK" -> new Rook(pawn.getPlayer(), pawn.getPosition(), pawn.board);
            case "QUEEN" -> new Queen(pawn.getPlayer(), pawn.getPosition(), pawn.board);
            case "KNIGHT" -> new Knight(pawn.getPlayer(), pawn.getPosition(), pawn.board);
            case "KING" -> new King(pawn.getPlayer(), pawn.getPosition(), pawn.board);
            case "BISHOP" -> new Bishop(pawn.getPlayer(), pawn.getPosition(), pawn.board);
            default -> null;
        };
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
