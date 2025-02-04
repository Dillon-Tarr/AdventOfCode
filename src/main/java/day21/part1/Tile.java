package day21.part1;

import java.util.LinkedList;

class Tile {
    boolean isGardenPlot;
    private boolean hasBeenReached;
    private Integer stepCountAtFirstReach = null;
    private Integer minimumDistanceFromStart = null;
    boolean isOddNumberOfStepsFromStart = false;
    private Tile northAdjacentGardenPlot = null;
    private Tile southAdjacentGardenPlot = null;
    private Tile eastAdjacentGardenPlot = null;
    private Tile westAdjacentGardenPlot = null;
    private LinkedList<Tile> adjacentGardenPlots = new LinkedList<>();

    Tile(char ch) {
        switch (ch) {
            case '.' -> {
                isGardenPlot = true;
                hasBeenReached = false;
            }
            case '#' -> {
                isGardenPlot = false;
                hasBeenReached = false;
            }
            case 'S' -> {
                isGardenPlot = true;
                hasBeenReached = true;
                stepCountAtFirstReach = 0;
            }
            default -> throw new IllegalArgumentException();
        }
    }

    void establishSouthTile(Tile southernTile) {
        southAdjacentGardenPlot = southernTile;
        southernTile.northAdjacentGardenPlot = this;
    }

    void establishEastTile(Tile easternTile){
        eastAdjacentGardenPlot = easternTile;
        easternTile.westAdjacentGardenPlot = this;
    }

    void performPreWalkCalculations(int startY, int startX, int y, int x) {
        if (northAdjacentGardenPlot != null) adjacentGardenPlots.add(northAdjacentGardenPlot);
        if (southAdjacentGardenPlot != null) adjacentGardenPlots.add(southAdjacentGardenPlot);
        if (eastAdjacentGardenPlot != null) adjacentGardenPlots.add(eastAdjacentGardenPlot);
        if (westAdjacentGardenPlot != null) adjacentGardenPlots.add(westAdjacentGardenPlot);
        minimumDistanceFromStart = Math.abs(startY-y)+Math.abs(startX-x);
        isOddNumberOfStepsFromStart = minimumDistanceFromStart%2!=0;
    }

    LinkedList<Tile> getAdjacentGardenPlots() {
        return adjacentGardenPlots;
    }

    public void recordFirstReach(int stepCount) {
        hasBeenReached = true;
        stepCountAtFirstReach = stepCount;
    }
}
