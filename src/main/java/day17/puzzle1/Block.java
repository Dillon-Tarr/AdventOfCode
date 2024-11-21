package day17.puzzle1;

import java.util.ArrayList;

class Block {
    int cost, heatLossDistanceFromStart, worstPossibleEffectiveHeatLossFromBeginningToBlock;
    ArrayList<Visit> visits = new ArrayList<>();

    Block(int y, int x, int cost, int worstPossibleEffectiveHeatLossFromBeginningToBlock) {
        this.cost = cost;
        this.worstPossibleEffectiveHeatLossFromBeginningToBlock = worstPossibleEffectiveHeatLossFromBeginningToBlock;
        if (y == 0 && x == 0) heatLossDistanceFromStart = 0;
        else heatLossDistanceFromStart = 2000000000;
    }

    boolean stepIsOldOrCold(Visit visit, int heatLost) {
        if (visits.stream().anyMatch(visit::equals)) return true;
        visits.add(visit);
        if (heatLost < heatLossDistanceFromStart) heatLossDistanceFromStart = heatLost;
        return heatLost > worstPossibleEffectiveHeatLossFromBeginningToBlock;
    }

}
