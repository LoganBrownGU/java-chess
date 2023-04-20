package board;

import players.Player;

public interface BoardStrategy {
    void play();
    boolean check(Player player);
    Player checkWinner();
}
