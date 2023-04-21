package board;

import main.Coordinate;
import pieces.King;
import pieces.Piece;
import pieces.PieceType;
import players.Player;
import userlayers.UserLayer;

import java.util.ArrayList;

public abstract class Board implements BoardStrategy {
    private ArrayList<Piece> pieces;
    private ArrayList<Player> players;
    private UserLayer userLayer = null;
    public final int maxX, maxY;

    public boolean onBoard(Coordinate coord) {
        return coord.x >= 0 && coord.y >= 0 && coord.x < maxX && coord.y < maxY;
    }

    public Piece pieceAt(Coordinate coords) {
        if (coords == null) return null;

        for (Piece p: pieces) {
            if (p.getPosition().equals(coords))
                return p;
        }

        return null;
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
        if (userLayer != null) userLayer.update();
    }

    public void removePiece(Piece p) {
        if (!pieces.remove(p)) throw new RuntimeException("Piece " + p + " does not exist");

        userLayer.update();
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
}
