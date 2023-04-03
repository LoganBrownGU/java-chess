package players;

import main.Coordinate;

public class HumanPlayer extends Player {
    @Override
    public Coordinate getMove() {
        return null;
    }

    public HumanPlayer(char representation) {
        super(representation);
    }
}
