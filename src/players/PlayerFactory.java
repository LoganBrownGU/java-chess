package players;

import board.Board;

public class PlayerFactory {

    public static Player playerFromString(String str, char representation, Board board) {
        Player player;

        switch (str) {
            case "HUMAN" -> player = new HumanPlayer(representation, board);
            case "BAD_AI" -> player = new BadAIPlayer(representation, board);
            default -> throw new RuntimeException("player type " + str + " not recognised");
        }

        return player;
    }
}
