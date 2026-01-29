package year2023.day21.part1;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

class Main {
    static private final int DAY = 21;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private Tile[][] tiles;
    static private int gridLength, gridWidth;
    static private int startY;
    static private int startX;
    static private Tile startPlot;
    static private final int neededStepCount = 64;
    static private int stepsActuallyTaken = 0;
    static private final ArrayList<Tile> unreachedGardenPlots = new ArrayList<>();
    static private final ArrayList<Tile> reachedGardenPlots = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        setOtherTileDetails();
        walk();
        countReachablePlotsForNeededStepCount();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void processInputData() {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(INPUT_FILE))) {
            lnr.skip(Long.MAX_VALUE);
            gridLength = lnr.getLineNumber();
            tiles = new Tile[gridLength][];
        } catch (IOException e) {throw new RuntimeException();}
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line = br.readLine();
            gridWidth = line.length();
            for (int y = 0; y < tiles.length; y++) {
                tiles[y] = new Tile[gridWidth];
                for (int x = 0; x < gridWidth; x++) {
                    char character = line.charAt(x);
                    tiles[y][x] = new Tile(character);
                    if (character == '.') unreachedGardenPlots.add(tiles[y][x]);
                    else if (character == 'S') {
                        startY = y;
                        startX = x;
                        startPlot = tiles[y][x];
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void setOtherTileDetails() {
        Tile southernTile, easternTile;
        for (int y = 0; y < gridLength-1; y++) {
            for (int x = 0; x < gridWidth; x++) {
                southernTile = tiles[y+1][x];
                if (tiles[y][x].isGardenPlot && southernTile.isGardenPlot) tiles[y][x].establishSouthTile(southernTile);
            }
        }
        for (int x = 0; x < gridWidth-1; x++) {
            for (int y = 0; y < gridLength; y++) {
                easternTile = tiles[y][x+1];
                if (tiles[y][x].isGardenPlot && easternTile.isGardenPlot) tiles[y][x].establishEastTile(easternTile);
            }
        }
        for (int y = 0; y < gridLength; y++) for (int x = 0; x < gridWidth; x++)
                tiles[y][x].performPreWalkCalculations(startY, startX, y, x);
    }

    private static void walk() {
        LinkedList<Tile> stepGroup = new LinkedList<>();
        LinkedList<Tile> adjacentGardenPlots = new LinkedList<>();
        HashSet<Tile> nextTiles = new HashSet<>();
        Tile tile, adjacentGardenPlot;

        reachedGardenPlots.add(startPlot);
        stepGroup.add(startPlot);

        while (stepsActuallyTaken < neededStepCount) {
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

    private static void countReachablePlotsForNeededStepCount() {
        int count = 0;
        for (Tile plot : reachedGardenPlots) {
            if (!plot.isOddNumberOfStepsFromStart) count++;
        } // ^ If neededStepCount were odd, count++ would instead need to happen when plot.isOddNumberOfStepsFromStart is true.
        System.out.println("Number of garden tiles reachable in exactly "+neededStepCount+" steps: "+count);
        System.out.println("Number of steps actually taken to determine this: "+stepsActuallyTaken);
    }

}
