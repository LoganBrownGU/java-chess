package players;

import main.Coordinate;

public abstract class Player {

    public final char representation;
    public abstract Coordinate getMove();

    public Player(char representation) {
        this.representation = representation;
    }
}
