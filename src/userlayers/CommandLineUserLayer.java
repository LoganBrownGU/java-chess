package userlayers;

import board.Board;
import main.Coordinate;
import pieces.Piece;
import players.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CommandLineUserLayer implements UserLayer {

    // todo make getPiece and getMove independent of userlayer.

    private final String clear = "\n".repeat(50);

    private Scanner sc = new Scanner(System.in);
    private Board board;
    private static final String representations[] = {
            "rk", "kt", "bp", "kg", "qn", "pn"
    };

    public void update() {
        System.out.println(clear);

        String boardRep[][] = new String[board.maxY][board.maxX];
        for (String[] strings : boardRep) Arrays.fill(strings, "   ");

        for (Piece p: board.getPieces()) {
            if (p != null)
                boardRep[p.getPosition().y][p.getPosition().x] = representations[p.getType().ordinal()] + p.getPlayer().representation;
        }

        StringBuilder sb = new StringBuilder();

        for (char c = 'a'; c < 'a' + 8; c++)
            sb.append("| ").append(c).append(" ");
        sb.append("|\n");

        int i = 0;
        for (String[] line: boardRep) {
            for (String str: line)
                sb.append("|").append(str);
            sb.append("| ").append(i++).append("\n");
        }

        System.out.println(sb.toString());
    }

    @Override
    public Piece getPiece(Player p) {
        Coordinate piecePosition = null;
        Piece piece;

        while ((piece = board.pieceAt(piecePosition)) == null || piecePosition == null || piece.getPlayer() != p) {

            System.out.print(p.representation + ": Enter position of piece to move: ");
            String input = sc.nextLine();
            piecePosition = Coordinate.chessCoordToCoordinate(input);
        }

        return piece;
    }

    @Override
    public Coordinate getMove(Piece pieceToMove) {

        Coordinate movePosition = null;
        ArrayList<Coordinate> moves = pieceToMove.possibleMoves();

        while (movePosition == null || !moves.contains(movePosition)) {
            System.out.print("Enter position to move piece to: ");
            String input = sc.nextLine();
            movePosition = Coordinate.chessCoordToCoordinate(input);
        }

        return movePosition;
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

}
