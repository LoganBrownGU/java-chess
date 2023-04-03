package pieces;

import main.Coordinate;
import observer.Observer;
import observer.Subject;

import java.util.ArrayList;

public abstract class Piece implements PieceStrategy, Subject {
    private ArrayList<Coordinate> previousMoves;
    private ArrayList<Observer> observers;
    private final boolean player; // false = black, true = white
    private PieceType type;
    private Coordinate position;

    public Coordinate getLastMove() {
        return previousMoves.get(0);
    }

    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void deregister(Observer o) {
        observers.remove(o);
    }

    @Override
    public void updateObservers() {
        for (Observer o: observers)
            o.update(this);
    }

    public Piece(boolean player, Coordinate position, PieceType type) {
        this.previousMoves = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.player = player;
        this.position = position;
        this.type = type;
    }

    public boolean getPlayer() {
        return player;
    }

    public PieceType getType() {
        return type;
    }

    public Coordinate getPosition() {
        return position;
    }
}
