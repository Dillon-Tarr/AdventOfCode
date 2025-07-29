package year2023.day22;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.TreeSet;

class Part2 {
    static private final int DAY = 22;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private final ArrayList<Brick> allBricks = new ArrayList<>();
    static private Brick[][][] occupyingBrick;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        createSpace();
        fillSpace();
        dropBricks();
        establishSupportStructure();
        getChainReactionsSum();

        System.out.println("\nExecution time in seconds: " + ((double) (System.nanoTime() - startTime) / 1000000000));
    }

    private static void processInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line = br.readLine();
            while (line != null) {
                allBricks.add(new Brick(line));
                line = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void createSpace() {
        int highestX = Integer.MIN_VALUE;
        int highestY = Integer.MIN_VALUE;
        int highestZ = Integer.MIN_VALUE;
        for (Brick brick : allBricks) {
            if (brick.highX > highestX) highestX = brick.highX;
            if (brick.highY > highestY) highestY = brick.highY;
            if (brick.highZ > highestZ) highestZ = brick.highZ;
        }
        occupyingBrick = new Brick[highestX+1][highestY+1][highestZ+2];
    }

    private static void fillSpace() {
        Brick theFloor = new Brick("0,0,0~"+ occupyingBrick.length+","+ occupyingBrick[0].length+",0");
        for (int x = 0; x < occupyingBrick.length; x++) for (int y = 0; y < occupyingBrick[0].length; y++)
            occupyingBrick[x][y][0] = theFloor;
        for (Brick brick : allBricks) for (XyzCoordinates coordinates : brick.occupiedCoordinates)
            occupyingBrick[coordinates.x][coordinates.y][coordinates.z] = brick;
    }

    private static void dropBricks() {
        final PriorityQueue<Brick> droppingBricks = new PriorityQueue<>(allBricks);
        Brick brick;
        boolean foundRestingSpot;
        while (!droppingBricks.isEmpty()) {
            brick = droppingBricks.poll();
            foundRestingSpot = false;
            while (!foundRestingSpot) {
                for(XyzCoordinates coordinates : brick.occupiedCoordinates) {
                    if (occupyingBrick[coordinates.x][coordinates.y][coordinates.z-1] != null
                            && occupyingBrick[coordinates.x][coordinates.y][coordinates.z-1] != brick) {
                        foundRestingSpot = true;
                        break;
                    }
                }
                if (!foundRestingSpot) {
                    for(XyzCoordinates coordinates : brick.occupiedCoordinates) {
                        occupyingBrick[coordinates.x][coordinates.y][coordinates.z] = null;
                        coordinates.setNewZ(coordinates.z-1);
                        occupyingBrick[coordinates.x][coordinates.y][coordinates.z] = brick;
                    }
                    brick.dropLowAndHighZ();
                }
            }
        }
    }

    private static void establishSupportStructure() {
        final HashSet<Brick> overBricks = new HashSet<>();
        final HashSet<Brick> underBricks = new HashSet<>();
        Brick overBrick, underBrick;
        for(Brick brick : allBricks) {
            for (XyzCoordinates coordinates : brick.occupiedCoordinates) {
                overBrick = occupyingBrick[coordinates.x][coordinates.y][coordinates.z+1];
                if (overBrick != null && overBrick != brick) overBricks.add(overBrick);
                underBrick = occupyingBrick[coordinates.x][coordinates.y][coordinates.z-1];
                if (underBrick != null && underBrick != brick) underBricks.add(underBrick);
            }
            brick.overBricks.addAll(overBricks);
            overBricks.clear();
            brick.underBricks.addAll(underBricks);
            underBricks.clear();
        }
    }

    private static void getChainReactionsSum() {
        int chainReactionSum = 0; //Sum of OTHER BRICKS that would fall for each brick, if disintegrated.
        final PriorityQueue<Brick> bricksForWhichToTestChainReactivity = new PriorityQueue<>(allBricks);
        HashSet<Brick> removedOrFallenBricks = new HashSet<>();
        TreeSet<Brick> overBricksToCheck = new TreeSet<>();
        Brick brick;
        boolean anUnderBrickRemains;

        while (!bricksForWhichToTestChainReactivity.isEmpty()) {
            brick = bricksForWhichToTestChainReactivity.poll();
            if (brick.overBricks.isEmpty()) continue;
            removedOrFallenBricks.add(brick);
            overBricksToCheck.addAll(brick.overBricks); //When doing this addAll, only one is being added.
            while (!overBricksToCheck.isEmpty()) {
                Brick overBrick = overBricksToCheck.pollFirst();
                assert overBrick != null;
                anUnderBrickRemains = false;
                for (Brick underBrick : overBrick.underBricks) {
                    if (!removedOrFallenBricks.contains(underBrick)) {
                        anUnderBrickRemains = true;
                        break; // Identified that Brick G has itself, and not Brick F, as its only underBrick.
                    }
                }
                if (!anUnderBrickRemains) {
                    removedOrFallenBricks.add(overBrick);
                    overBricksToCheck.addAll(overBrick.overBricks);
                }
            }
            chainReactionSum += removedOrFallenBricks.size()-1;
            removedOrFallenBricks.clear();
        }
        System.out.println("\nChain reaction sum: "+chainReactionSum);
    }
}
