package main;

/*
* All the functions will return true if the pieces are at the same position
*/

import java.util.Objects;

public class Coordinate {

    public final int x, y;

    public static Coordinate chessCoordToCoordinate(String chessCoord) {
        String positions[] = chessCoord.split(" ");
        int x = positions[0].charAt(0) - 'a';
        int y = Integer.parseInt(positions[1]);

        return new Coordinate(x, y);
    }

    // checks if there are no pieces between coords. Note that if the coordinates are not on the same rank, file, or diagonal,
    // the function will return FALSE
    public boolean lineOfSight(Coordinate other, Board board) {
        if (sameRank(other)) {
            for (int i = Math.min(this.x, other.x) + 1; i < Math.max(this.x, other.x); i++)
                if (board.pieceAt(new Coordinate(i, this.y)) != null) return false;

            return true;
        }

        if (sameFile(other)) {
            for (int i = Math.min(this.y, other.y) + 1; i < Math.max(this.y, other.y); i++)
                if (board.pieceAt(new Coordinate(this.x, i)) != null) return false;

            return true;
        }

        // todo this DOES NOT work. There needs to be a check to see if there are pieces on either side of the line of travel
        if (Math.abs(this.x - other.x) == Math.abs(this.y - other.y)) {
            int i = Math.min(this.x, other.x) + 1;
            int j = Math.min(this.y, other.y) + 1;

            while (i < Math.max(this.x, other.x)) {
                if (board.pieceAt(new Coordinate(i, j)) != null) return false;

                i++;
                j++;
            }

            return true;
        }

        return false;
    }

    public boolean sameRank(Coordinate other) {
        return this.y == other.y;
    }

    public boolean sameFile(Coordinate other) {
        return this.x == other.x;
    }

    public boolean sameDiagonal(Coordinate other) {
        int x = Math.abs(this.x - other.x);
        int y = Math.abs(this.y - other.y);

        return x == y;
    }

    public boolean adjacent(Coordinate other) {
        int x = Math.abs(this.x - other.x);
        int y = Math.abs(this.y - other.y);

        return (x <= 1 && y == 0) || (y <= 1 && x == 0);
    }

    public boolean adjacentDiagonally(Coordinate other) {
        int x = Math.abs(this.x - other.x);
        int y = Math.abs(this.y - other.y);

        return x == 1 && y == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
