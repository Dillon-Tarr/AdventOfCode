package day23.part2;

import shared.Coordinates;

import java.util.ArrayList;

class Step {
    int stepCount;
    Coordinates currentCoordinates;
    ArrayList<Coordinates> pastCoordinates = new ArrayList<>();

    Step(int y, int x, int stepCount) {
        this.stepCount = stepCount;
        currentCoordinates = new Coordinates(y, x);
    }

    Step(int y, int x, int stepCount, ArrayList<Coordinates> pastPastCoordinates) {
        this.stepCount = stepCount;
        currentCoordinates = new Coordinates(y, x);
        pastCoordinates.addAll(pastPastCoordinates);
        pastCoordinates.add(new Coordinates(y, x));
    }

}
