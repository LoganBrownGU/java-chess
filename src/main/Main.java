package main;

public class Main {
    public static void main(String[] args) {

        Board b = BoardFactory.standardBoard();
        b.updateObservers();
    }
}