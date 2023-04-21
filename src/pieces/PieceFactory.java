package pieces;

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
}
