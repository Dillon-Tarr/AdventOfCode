package year2023.day21.part2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import static year2023.day21.part2.Main.gridWidth;

public class Grid {
    static private final ArrayList<Grid> allGrids = new ArrayList<>();

    private final String gridTypeDescription;
    private final long gridCount;
    private int reachedTileCount = 0;
    private long totalPlotsReachedFromThisGridType = 0;
    private int stepsActuallyTaken = 0;
    private final Tile[][] tiles = new Tile[gridWidth][gridWidth];
    private final ArrayList<Tile> unreachedGardenPlots = new ArrayList<>();
    private final ArrayList<Tile> reachedGardenPlots = new ArrayList<>();
    private final int startY;
    private final int startX;
    private final Tile startPlot;
    private final int stepsAvailable;

    Grid(String gridTypeDescription, long gridCount, char[][] inputCharacters, int startY, int startX, int stepsAvailable) {
        allGrids.add(this);
        this.gridTypeDescription = gridTypeDescription;
        this.gridCount = gridCount;
        Tile tile;
        for (int y = 0; y < gridWidth; y++) for (int x = 0; x < gridWidth; x++) {
            tile = new Tile(inputCharacters[y][x]);
            tiles[y][x] = tile;
            if (tile.isGardenPlot) unreachedGardenPlots.add(tile);
        }
        this.startY = startY;
        this.startX = startX;
        startPlot = tiles[startY][startX];
        this.stepsAvailable = stepsAvailable;
        unreachedGardenPlots.remove(startPlot);
        reachedGardenPlots.add(startPlot);

        setOtherTileDetails();
        walk();
        countReachablePlotsGivenStepsAvailable();
    }

    private void setOtherTileDetails() {
        Tile southernTile, easternTile;
        for (int y = 0; y < gridWidth-1; y++) {
            for (int x = 0; x < gridWidth; x++) {
                southernTile = tiles[y+1][x];
                if (tiles[y][x].isGardenPlot && southernTile.isGardenPlot) tiles[y][x].establishSouthTile(southernTile);
            }
        }
        for (int x = 0; x < gridWidth-1; x++) {
            for (int y = 0; y < gridWidth; y++) {
                easternTile = tiles[y][x+1];
                if (tiles[y][x].isGardenPlot && easternTile.isGardenPlot) tiles[y][x].establishEastTile(easternTile);
            }
        }
        for (int y = 0; y < gridWidth; y++) for (int x = 0; x < gridWidth; x++)
            tiles[y][x].performPreWalkCalculations(startY, startX, y, x);
    }

    private void walk() {
        LinkedList<Tile> stepGroup = new LinkedList<>();
        LinkedList<Tile> adjacentGardenPlots = new LinkedList<>();
        HashSet<Tile> nextTiles = new HashSet<>();
        Tile tile, adjacentGardenPlot;

        stepGroup.add(startPlot);
        while (stepsActuallyTaken < stepsAvailable) {
            stepsActuallyTaken++;
            while(!stepGroup.isEmpty()) {
                tile = stepGroup.pop();
                adjacentGardenPlots.addAll(tile.getAdjacentGardenPlots());
                while (!adjacentGardenPlots.isEmpty()) {
                    adjacentGardenPlot = adjacentGardenPlots.pop();
                    if (unreachedGardenPlots.contains(adjacentGardenPlot)) {
                        nextTiles.add(adjacentGardenPlot);
                        unreachedGardenPlots.remove(adjacentGardenPlot);
                        reachedGardenPlots.add(adjacentGardenPlot);
                    }
                }
            }
            if (nextTiles.isEmpty()) break;
            stepGroup.addAll(nextTiles);
            nextTiles.clear();
        }
    }

    private void countReachablePlotsGivenStepsAvailable() {
        if (stepsAvailable%2==0) {
            for (Tile plot : reachedGardenPlots) if (!plot.isOddNumberOfStepsFromStart) reachedTileCount++;
        } else for (Tile plot : reachedGardenPlots) if (plot.isOddNumberOfStepsFromStart) reachedTileCount++;

        totalPlotsReachedFromThisGridType = gridCount*reachedTileCount;

        System.out.println("\nGrid type description: "+gridTypeDescription);
        System.out.println("    Number of steps taken to evaluate this grid type: "+stepsActuallyTaken);
        System.out.println("    Number of garden plots reachable in exactly "+stepsAvailable+" steps: "+ reachedTileCount);
        System.out.println("    Number of this type of grid: "+gridCount);
        System.out.println("    Total number of reachable garden plots across grids of this type: "+ totalPlotsReachedFromThisGridType);
    }

    static long getSumOfPlotsReachedOnAllGrids() {
        long sum = 0;
        for (Grid grid : allGrids) {
            sum += grid.totalPlotsReachedFromThisGridType;
        }
        return sum;
    }

}
