package day17;

import java.util.ArrayList;

class Block {
    char cost;
    int heatLossDistanceFromStart;
    ArrayList<Visit> visits = new ArrayList<>();

    Block(int y, int x, char cost) {
        this.cost = cost;
        if (y == 0 && x == 0) heatLossDistanceFromStart = 0;
        else heatLossDistanceFromStart = 2000000000;
    }

    boolean visitAndReturnWhetherStepWasAlreadyTaken(int newHeatLost, Visit visit) {
        if (visits.stream().anyMatch(visit::equals)) return true;
        visits.add(visit);
        if (newHeatLost < heatLossDistanceFromStart) heatLossDistanceFromStart = newHeatLost;
        return false;
    }

    @Override
    public String toString() {
        return ""+cost;
    }
}
