package observer;

public interface Subject {

    void register(Observer o);
    void deregister(Observer o);

    void updateObservers();
}
