package year2023.day17;

import shared.CardinalDirection;

import java.util.ArrayList;

class QueuedStep implements Comparable<QueuedStep> {
    int y, x, heatLost;
    ArrayList<CardinalDirection> pastDirections = new ArrayList<>();

    QueuedStep(int y, int x, int heatLost) {
        this.y = y;
        this.x = x;
        this.heatLost = heatLost;
    }

    QueuedStep(int y, int x, int heatLost, CardinalDirection newDirection, int stepsInNewDirection, ArrayList<CardinalDirection> pastPastDirections) {
        this.y = y;
        this.x = x;
        this.heatLost = heatLost;
        pastDirections.addAll(pastPastDirections);
        while (stepsInNewDirection > 0){
            pastDirections.add(newDirection);
            stepsInNewDirection--;
        }
    }

    @Override
    public int compareTo(QueuedStep otherStep) {
        if (heatLost < otherStep.heatLost) return -1;
        else if (heatLost > otherStep.heatLost) return 1;
        else return Integer.compare(y+x, otherStep.y+otherStep.x);
    }
}
