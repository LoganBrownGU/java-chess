package players;

import board.Board;
import main.Coordinate;
import pieces.Piece;

import java.util.ArrayList;
import java.util.Random;

public class BadAIPlayer extends Player {

    Random rd = new Random();
    @Override
    public Piece getPiece() {
        ArrayList<Piece> pieces = new ArrayList<>();
        for (Piece piece: board.getPieces())
            if (piece.getPlayer() == this) pieces.add(piece);

        Piece pieceToMove = null;
        while (pieceToMove == null || pieceToMove.possibleMoves().size() == 0)
            pieceToMove = pieces.get(rd.nextInt(pieces.size()));

        return pieceToMove;
    }

    @Override
    public Coordinate getMove(Piece pieceToMove) {
        return pieceToMove.possibleMoves().get(rd.nextInt(pieceToMove.possibleMoves().size()));
    }

    public BadAIPlayer(char representation, Board board) {
        super(representation, board);
    }

}
