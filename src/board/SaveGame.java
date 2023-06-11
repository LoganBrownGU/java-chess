package board;

import main.Coordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pieces.*;
import players.HumanPlayer;
import players.Player;
import players.PlayerFactory;
import toolbox.FileHandler;
import userlayers.UserLayer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Vector;

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
        root.setAttribute("type", board.type.toString().toLowerCase());
        doc.appendChild(root);

        Element pieces = doc.createElement("pieces");
        root.appendChild(pieces);
        Element players = doc.createElement("players");
        root.appendChild(players);

        addPiecesToDom(board.getPieces(), pieces);
        addPlayersToDOM(board.getPlayers(), players);

        if (board.getLastMove() != null) {
            Element lastMove = doc.createElement("last_move");
            lastMove.setTextContent(board.getLastMove().x + "," + board.getLastMove().y);
            root.appendChild(lastMove);
        }

        FileHandler.writeXML(path, doc);
    }

    private static ArrayList<Player> addPlayersFromDOM(Element root, Board board) {
        ArrayList<Player> players = new ArrayList<>();

        ArrayList<Element> elements = FileHandler.getChildren(root);
        for (Element element : elements) {
            char representation = element.getElementsByTagName("representation").item(0).getTextContent().charAt(0);
            players.add(PlayerFactory.playerFromString(element.getElementsByTagName("type").item(0).getTextContent().toUpperCase(), representation, board));
        }

        return players;
    }

    private static ArrayList<Piece> addPiecesFromDOM(Element root, Board board) {
        ArrayList<Piece> pieces = new ArrayList<>();

        ArrayList<Element> elements = FileHandler.getChildren(root);
        for (Element element : elements) {
            Piece piece;
            String[] posText = element.getElementsByTagName("position").item(0).getTextContent().split(",");
            Coordinate position = new Coordinate(Integer.parseInt(posText[0]), Integer.parseInt(posText[1]));
            Player player = board.playerWithRep(element.getElementsByTagName("player").item(0).getTextContent().charAt(0));


            switch (element.getTagName()) {
                case "king" -> {
                    piece = new King(player, position, board);
                    player.setSovereign((Sovereign) piece);
                }
                case "pawn" -> {
                    int direction = Integer.parseInt(element.getElementsByTagName("direction").item(0).getTextContent());
                    piece = new Pawn(player, position, direction, board);
                }
                default -> piece = PieceFactory.pieceFromString(element.getTagName().toUpperCase(), player, position, board);
            }

            if (element.getElementsByTagName("sovereign").getLength() > 0)
                player.setSovereign((Sovereign) piece);
        }

        return pieces;
    }

    public static Board loadGame(String path, UserLayer userLayer) {
        Document doc = FileHandler.readXML(path);
        Element root = doc.getDocumentElement();

        Board board = BoardFactory.boardFromString(root.getAttribute("type").toUpperCase(), userLayer);
        addPlayersFromDOM((Element) root.getElementsByTagName("players").item(0), board);

        for (Piece piece : addPiecesFromDOM((Element) root.getElementsByTagName("pieces").item(0), board))
            board.addPiece(piece);

        if (root.getElementsByTagName("last_move").getLength() > 0) {
            String[] lastMoveText = root.getElementsByTagName("last_move").item(0).getTextContent().split(",");
            board.setLastMove(new Coordinate(Integer.parseInt(lastMoveText[0]), Integer.parseInt(lastMoveText[1])));
        }

        board.setUserLayerActive(true);

        return board;
    }
}
