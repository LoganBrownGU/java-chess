package userlayers;

import main.Board;
import main.Coordinate;
import observer.Subject;
import pieces.Piece;

import java.util.Arrays;
import java.util.Scanner;

public class CommandLineUserLayer implements UserLayer {

    private Scanner sc = new Scanner(System.in);
    private Board board;
    private static final String representations[] = {
            "rk", "kt", "bp", "kg", "qn", "pn"
    };

    public void update() {
        String boardRep[][] = new String[board.maxY][board.maxX];
        for (String[] strings : boardRep) Arrays.fill(strings, "   ");

        for (Piece p: board.getPieces()) {
            if (p != null)
                boardRep[p.getPosition().y][p.getPosition().x] = representations[p.getType().ordinal()] + p.getPlayer().representation;
        }

        for (String[] line: boardRep) {
            for (String str: line)
                System.out.print("|" + str);
            System.out.println("|");
        }
    }

    @Override
    public Piece getPiece() {
        Coordinate piecePosition = null;
        Piece piece;

        while ((piece = board.pieceAt(piecePosition)) == null && piecePosition == null) {
            System.out.print("Enter position of piece to move: ");
            String input = sc.nextLine();
            piecePosition = Coordinate.chessCoordToCoordinate(input);
        }

        return piece;
    }

    @Override
    public Coordinate getMove(Piece pieceToMove) {

        Coordinate movePosition = null;

        while (movePosition == null || !pieceToMove.move(movePosition)) {
            System.out.print("Enter position to move piece to: ");
            String input = sc.nextLine();
            movePosition = Coordinate.chessCoordToCoordinate(input);
        }

        return movePosition;
    }

    public CommandLineUserLayer(Board board) {
        this.board = board;
    }
}
