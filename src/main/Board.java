package main;

import pieces.King;
import pieces.Piece;
import pieces.PieceType;
import players.Player;
import userlayers.UserLayer;

import java.util.ArrayList;

public class Board {
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

    public void update() {
        updateUserLayer();
    }

    public King getKing(Player player) {
        for (Piece p: pieces)
            if (p.getType() == PieceType.KING && p.getPlayer() == player) return (King) p;

        return null;
    }

    public void updateUserLayer() {
        if (userLayer != null) userLayer.update();
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

        if (userLayer != null) userLayer.update();
    }

    public void removePiece(Piece p) {
        pieces.remove(p);

        userLayer.update();
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
