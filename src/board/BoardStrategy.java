package board;

import players.Player;

public interface BoardStrategy {
    void play();
    Player check(Player player);
    Player checkWinner();
}
