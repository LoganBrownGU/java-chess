package main;

import observer.Observer;
import observer.Subject;
import pieces.Piece;
import players.Player;
import userlayers.CommandLineUserLayer;
import userlayers.UserLayer;

import java.util.ArrayList;

public class Board implements Observer, Subject {
    private ArrayList<Piece> pieces;
    private ArrayList<Player> players;
    private UserLayer userLayer = null;
    public final int maxX, maxY;

    public Piece pieceAt(Coordinate coords) {
        if (coords == null) return null;

        for (Piece p: pieces) {
            if (p.getPosition().equals(coords))
                return p;
        }

        return null;
    }

    @Override
    public void update(Subject s) {
        // todo do something with s
        updateObservers();
    }

    @Override
    public void register(Observer o) {
        return;
    }

    @Override
    public void deregister(Observer o) {
        return;
    }

    @Override
    public void updateObservers() {
        if (userLayer != null) userLayer.update(this);
        else throw new RuntimeException("USERLAYER IN BOARD HAS NOT BEEN INITIALISED");
    }

    public Board(int maxX, int maxY) {
        this.pieces = new ArrayList<>();
        this.players = new ArrayList<>();

        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void addPiece(Piece p) {
        pieces.add(p);

        if (userLayer != null) userLayer.update(this);
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public UserLayer getUserLayer() {
        return userLayer;
    }

    public void setUserLayer(UserLayer userLayer) {
        this.userLayer = userLayer;
    }
}
