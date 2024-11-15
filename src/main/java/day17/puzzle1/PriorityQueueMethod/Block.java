package day17.puzzle1.PriorityQueueMethod;

class Block {
    int y, x, cost, heatLossDistanceFromStart;

    Block(int y, int x, int cost) {
        this.y = y;
        this.x = x;
        this.cost = cost;
        if (y == 0 && x == 0) heatLossDistanceFromStart = 0;
        else heatLossDistanceFromStart = 2000000000;
    }
}
