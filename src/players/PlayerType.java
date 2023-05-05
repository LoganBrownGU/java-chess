package players;

import board.Board;

public enum PlayerType {
    HUMAN,
    BAD_AI;

    public Player toPlayer(char rep, Board board) {
        return switch (this) {
            case HUMAN -> new HumanPlayer(rep, board);
            case BAD_AI -> new BadAIPlayer(rep, board);
        };
    }
}
