package main;

import observer.Observer;
import observer.Subject;
import pieces.Piece;
import pieces.Rook;
import userlayers.CommandLineUserLayer;

import java.util.ArrayList;

public class Board implements Observer, Subject {
    private ArrayList<Piece> pieces;
    private Observer observer = null;
    public final int maxX, maxY;

    public Piece pieceAt(Coordinate coords) {
        if (coords == null) return null;

        for (Piece p: pieces)
            if (p.getPosition().equals(coords)) return p;

        return null;
    }

    @Override
    public void update(Subject s) {
        // todo do something with s
    }

    @Override
    public void register(Observer o) {
        observer = o;
    }

    @Override
    public void deregister(Observer o) {
        observer = null;
    }

    @Override
    public void updateObservers() {
        if (observer != null) observer.update(this);
        else throw new RuntimeException("OBSERVER IN BOARD HAS NOT BEEN INITIALISED");
    }

    public Board(int maxX, int maxY) {
        this.pieces = new ArrayList<>();
        observer = new CommandLineUserLayer(this);

        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void addPiece(Piece p) {
        pieces.add(p);
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }
}
