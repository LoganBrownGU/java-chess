package userlayers;

import main.Board;
import main.Coordinate;
import observer.Subject;
import pieces.Piece;

import java.util.Scanner;

public class CommandLineUserLayer implements UserLayer {

    private Scanner sc = new Scanner(System.in);
    private Board board;
    private static final String representations[] = {
            "rok", "knt", "bsp", "kng", "qun", "pwn"
    };

    @Override
    public void update(Subject s) {
        String boardRep[][] = new String[board.maxX][board.maxY];
        for (Piece p: board.getPieces())
            boardRep[p.getPosition().x][p.getPosition().y] = representations[p.getType().ordinal()];

        for (String[] line: boardRep) {
            for (String str: line)
                System.out.print("|" + str);
            System.out.println("|");
        }
    }

    @Override
    public Coordinate getMove() {

        Coordinate piecePosition = null;
        Coordinate movePosition = null;
        Piece piece;

        while ((piece = board.pieceAt(piecePosition)) == null && piecePosition == null) {
            System.out.print("Enter position of piece to move: ");
            String input = sc.nextLine();
            piecePosition = Coordinate.chessCoordToCoordinate(input);
        }

        while (movePosition == null && piece.move(movePosition)) {
            System.out.print("Enter position to move piece to: ");
            String input = sc.nextLine();
            movePosition = Coordinate.chessCoordToCoordinate(input);
        }

        return movePosition;
    }
}
