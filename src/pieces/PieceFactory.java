package pieces;

import board.Board;
import players.Player;

public class PieceFactory {
    public static Piece promotePawn(String str, Pawn pawn) {
        Piece piece = null;

        switch (str) {
            case "ROOK":
                piece = new Rook(pawn.getPlayer(), pawn.getPosition(), pawn.board);
                break;
            case "QUEEN":
                piece = new Queen(pawn.getPlayer(), pawn.getPosition(), pawn.board);
                break;
            case "KNIGHT":
                piece = new Knight(pawn.getPlayer(), pawn.getPosition(), pawn.board);
                break;
            case "KING":
                piece = new King(pawn.getPlayer(), pawn.getPosition(), pawn.board);
                break;
            case "BISHOP":
                piece = new Bishop(pawn.getPlayer(), pawn.getPosition(), pawn.board);
                break;
        }

        return piece;
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
