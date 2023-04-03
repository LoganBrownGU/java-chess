package userlayers;

import main.Coordinate;
import observer.Observer;

public interface UserLayer extends Observer {
    Coordinate getMove();
}
