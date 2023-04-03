package main;

/*
* All the functions will return true if the pieces are at the same position
*/

import java.util.Objects;

public class Coordinate {

    public int x, y;

    public static Coordinate chessCoordToCoordinate(String chessCoord) {
        String positions[] = chessCoord.split(" ");
        int x = positions[0].charAt(0) - 'a';
        int y = Integer.parseInt(positions[1]);

        return new Coordinate(x, y);
    }

    public boolean sameRank(Coordinate other) {
        return this.x == other.x;
    }

    public boolean sameFile(Coordinate other) {
        return this.y == other.y;
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
