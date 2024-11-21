package day17.puzzle1;

import java.util.ArrayList;

class Block {
    int cost, heatLossDistanceFromStart;
    ArrayList<Visit> visits = new ArrayList<>();

    Block(int y, int x, int cost) {
        this.cost = cost;
        if (y == 0 && x == 0) heatLossDistanceFromStart = 0;
        else heatLossDistanceFromStart = 2000000000;
    }

    boolean stepWasAlreadyTaken(Visit visit) {
        if (visits.stream().anyMatch(visit::equals)) return true;
        visits.add(visit);
        return false;
    }

}
