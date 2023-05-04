package board;

import main.Coordinate;
import pieces.King;
import pieces.Piece;
import pieces.PieceType;
import players.Player;
import userlayers.UserLayer;

import java.util.ArrayList;

public abstract class Board implements BoardStrategy {
    // todo add check to make sure that two players don't have the same rep
    private ArrayList<Piece> pieces;
    private ArrayList<Player> players;
    private UserLayer userLayer = null;
    private boolean userLayerActive = false;
    public final int maxX, maxY;
    private Coordinate lastMove = null;

    public boolean onBoard(Coordinate coord) {
        return coord.x >= 0 && coord.y >= 0 && coord.x < maxX && coord.y < maxY;
    }

    public Player playerWithRep(char representation) {
        for (Player player : this.getPlayers())
            if (player.representation == representation) return player;

        return null;
    }

    public ArrayList<Piece> getPiecesBelongingTo(Player player) {
        ArrayList<Piece> pieces = new ArrayList<>();
        for (Piece piece: this.getPieces())
            if (piece.getPlayer() == player) pieces.add(piece);

        return pieces;
    }

    public Piece pieceAt(Coordinate coords) {
        if (coords == null) return null;

        for (Piece p: pieces) {
            if (p.getPosition().equals(coords))
                return p;
        }

        return null;
    }

    public void updateUserLayer() {
        if (!userLayerActive) return;
        if (userLayer != null) userLayer.update();
        else throw new RuntimeException("USERLAYER IN BOARD HAS NOT BEEN INITIALISED");
    }

    public Board(int maxX, int maxY, UserLayer userLayer) {
        this.pieces = new ArrayList<>();
        this.players = new ArrayList<>();

        this.maxX = maxX;
        this.maxY = maxY;

        this.userLayer = userLayer;
        userLayer.setBoard(this);
    }

    public void addPiece(Piece p) {
        pieces.add(p);

        // todo can probably remove the not null check
        if (userLayer != null) updateUserLayer();
    }

    public void removePiece(Piece p) {
        if (!pieces.remove(p)) throw new RuntimeException("Piece " + p + " does not exist");

        updateUserLayer();
    }

    public boolean hasPieceAt(Coordinate coord) {
        return this.pieceAt(coord) != null;
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

    public Coordinate getLastMove() {
        return lastMove;
    }

    public void setLastMove(Coordinate lastMove) {
        this.lastMove = lastMove;
    }

    public void setUserLayerActive(boolean userLayerActive) {
        this.userLayerActive = userLayerActive;
    }
}
