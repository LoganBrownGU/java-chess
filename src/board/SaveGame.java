package board;

import main.Coordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pieces.*;
import players.Player;
import toolbox.FileHandler;
import userlayers.UserLayer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

public class SaveGame {
    private static void addMovesToDOM(ArrayList<Coordinate> moves, Element element) {
        for (Coordinate coord : moves) {
            Element move = element.getOwnerDocument().createElement("move");
            move.setTextContent(coord.x + "," + coord.y);
            element.appendChild(move);
        }
    }

    private static void addPiecesToDom(ArrayList<Piece> pieces, Element element) {
        for (Piece piece : pieces) {
            Element pieceElement = element.getOwnerDocument().createElement(piece.getType().toString().toLowerCase());

            Element pieceAttr = element.getOwnerDocument().createElement("position");
            pieceAttr.setTextContent(piece.getPosition().x  + "," + piece.getPosition().y);
            pieceElement.appendChild(pieceAttr);

            pieceAttr = element.getOwnerDocument().createElement("player");
            pieceAttr.setTextContent(String.valueOf(piece.getPlayer().representation));
            pieceElement.appendChild(pieceAttr);

            if (piece instanceof Pawn pawn) {
                pieceAttr = element.getOwnerDocument().createElement("direction");
                pieceAttr.setTextContent(String.valueOf(pawn.direction));
                pieceElement.appendChild(pieceAttr);

                pieceAttr = element.getOwnerDocument().createElement("promotionRank");
                pieceAttr.setTextContent(String.valueOf(pawn.promotionRank));
                pieceElement.appendChild(pieceAttr);
            } else if (piece instanceof Sovereign sovereign) {
                pieceAttr = element.getOwnerDocument().createElement("sovereign");
                pieceElement.appendChild(pieceAttr);
            }

            Element moves = element.getOwnerDocument().createElement("moves");
            addMovesToDOM(piece.getPreviousMoves(), moves);
            pieceElement.appendChild(moves);

            element.appendChild(pieceElement);
        }
    }

    private static void addPlayersToDOM(ArrayList<Player> players, Element element) {
        for (Player player : players) {
            Element playerElement = element.getOwnerDocument().createElement("player");

            Element playerAttr = element.getOwnerDocument().createElement("representation");
            playerAttr.setTextContent(String.valueOf(player.representation));
            playerElement.appendChild(playerAttr);

            playerAttr = element.getOwnerDocument().createElement("type");
            playerAttr.setTextContent(String.valueOf(player.type));
            playerElement.appendChild(playerAttr);

            element.appendChild(playerElement);
        }
    }

    public static void saveGame(String path, Board board) {
        Document doc;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        Element root = doc.createElement("board");
        doc.appendChild(root);

        Element pieces = doc.createElement("pieces");
        root.appendChild(pieces);
        Element players = doc.createElement("players");
        root.appendChild(players);

        addPiecesToDom(board.getPieces(), pieces);
        addPlayersToDOM(board.getPlayers(), players);

        Element max = doc.createElement("max_x");
        max.setTextContent(String.valueOf(board.maxX));
        root.appendChild(max);
        max = doc.createElement("max_y");
        max.setTextContent(String.valueOf(board.maxY));
        root.appendChild(max);

        Element lastMove = doc.createElement("last_move");
        lastMove.setTextContent(board.getLastMove().x + "," + board.getLastMove().y);
        root.appendChild(lastMove);

        FileHandler.writeXML(path, doc);
    }

    private static ArrayList<Piece> addPiecesFromDOM(Element root, ArrayList<Player> players) {
        ArrayList<Piece> pieces = new ArrayList<>();

        ArrayList<Element> elements = FileHandler.getChildren(root);
        for (Element element : elements) {
            if (element.getTagName().equals("king"))
                King piece = new King()
        }

        return pieces;
    }

    public static Board loadGame(String path, UserLayer userLayer) {
        Document doc = FileHandler.readXML(path);
        Element root = doc.getDocumentElement();

        ArrayList<Piece> pieces = new ArrayList<>();

        NodeList nodeList = root.getChildNodes();



        return null;
    }
}
