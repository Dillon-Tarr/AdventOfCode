package day17.puzzle1;

import day17.CardinalDirection;

import java.util.ArrayList;

class Block {
    int y, x, cost, heatLossDistanceFromStart;
    ArrayList<Visit> visits = new ArrayList<>();

    Block(int y, int x, int cost) {
        this.y = y;
        this.x = x;
        this.cost = cost;
        if (y == 0 && x == 0) heatLossDistanceFromStart = 0;
        else heatLossDistanceFromStart = 2000000000;
    }

    boolean visitAndReturnWhetherAlreadyVisited(CardinalDirection fromDirection, int consecutiveStepsInOneDirection) {
        Visit visit = new Visit(fromDirection, consecutiveStepsInOneDirection);
        if (visits.stream().anyMatch(visit::equals)) return true;
        visits.add(visit);
        return false;
    }
}
