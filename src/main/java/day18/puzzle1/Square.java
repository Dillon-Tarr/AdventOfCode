package day18.puzzle1;

import shared.Coordinates;

public class Square {
    Coordinates coordinates;
    boolean hasBeenDugOut = false;

    Square(int y, int x) {
        coordinates = new Coordinates(y, x);
    }

    void dig() {
        hasBeenDugOut = true;
    }

    @Override
    public String toString() {
        if (hasBeenDugOut) return "#";
        else return ".";
    }
}
