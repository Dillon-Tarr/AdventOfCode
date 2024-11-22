package day17;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Puzzle1 {
    static private final File INPUT_FILE = new File("input-files/day17input.txt");
    static private Block[][] blocks;
    static private int gridLength, gridWidth;
    static private final PriorityQueue<QueuedStep> queue = new PriorityQueue<>();
    static private int heatLostOnBestPath = Integer.MAX_VALUE;
    static private ArrayList<CardinalDirection> directionsToEnd;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        findBestPath();

        long timeAfterFindingBestPath = System.nanoTime();

        drawPath();
        printPath();

        long endTime = System.nanoTime();

        System.out.println("\nTime spent pathfinding, in seconds (includes time spent recording previous step directions for every path): "+
                ((double) (timeAfterFindingBestPath-startTime)/1000000000));
        System.out.println("Time spent drawing and printing the best path, in seconds: "+((double) (endTime-timeAfterFindingBestPath)/1000000000));
        System.out.println("Total execution time, in seconds: "+((double) (endTime-startTime)/1000000000));
    }

    private static void getInputData() {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(INPUT_FILE))) {
            lnr.skip(Long.MAX_VALUE);
            gridLength = lnr.getLineNumber();
            blocks = new Block[gridLength][];
        } catch (IOException e) {throw new RuntimeException();}
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line = br.readLine();
            gridWidth = line.length();
            for (int y = 0; y < blocks.length; y++) {
                blocks[y] = new Block[gridWidth];
                for (int x = 0; x < gridWidth; x++) {
                    blocks[y][x] = new Block(y, x, line.charAt(x));
                }
                line = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void findBestPath() {
        queue.add(new QueuedStep(0, 0, 0));
        while (!queue.isEmpty()) {
            QueuedStep step = queue.poll(); assert step != null;
            int y = step.y, x = step.x, heatLost = step.heatLost;
            ArrayList<CardinalDirection> pastDirections = step.pastDirections;
            if (y == gridLength-1 && x == gridWidth-1 && heatLost < heatLostOnBestPath) {
                heatLostOnBestPath = heatLost;
                directionsToEnd = step.pastDirections;
                break;
            }

            CardinalDirection lastDirection;
            if (pastDirections.isEmpty()) lastDirection = CardinalDirection.NONE;
            else lastDirection = pastDirections.get(pastDirections.size()-1);

            int newHeatLost = heatLost;
            CardinalDirection newDirection;
            if (lastDirection == CardinalDirection.EAST || lastDirection == CardinalDirection.WEST || lastDirection == CardinalDirection.NONE) {
                int newY;
                newDirection = CardinalDirection.NORTH;
                for (int i = 1; i <= 3; i++) {
                    newY = y-i;
                    if (newY < 0) break;
                    Block block = blocks[newY][x];
                    newHeatLost += Character.getNumericValue(block.cost);
                    if (block.visitAndReturnWhetherStepWasAlreadyTaken(newHeatLost, new Visit(newDirection, i))) break;
                    queue.offer(new QueuedStep(newY, x, newHeatLost, newDirection, i, pastDirections));
                }
                newHeatLost = heatLost;
                newDirection = CardinalDirection.SOUTH;
                for (int i = 1; i <= 3; i++) {
                    newY = y+i;
                    if (newY >= gridLength) break;
                    Block block = blocks[newY][x];
                    newHeatLost += Character.getNumericValue(block.cost);
                    if (block.visitAndReturnWhetherStepWasAlreadyTaken(newHeatLost, new Visit(newDirection, i))) break;
                    queue.offer(new QueuedStep(newY, x, newHeatLost, newDirection, i, pastDirections));
                }
            }
            if (lastDirection == CardinalDirection.NORTH || lastDirection == CardinalDirection.SOUTH || lastDirection == CardinalDirection.NONE) {
                newDirection = CardinalDirection.WEST;
                int newX;
                for (int i = 1; i <= 3; i++) {
                    newX = x-i;
                    if (newX < 0) break;
                    Block block = blocks[y][newX];
                    newHeatLost += Character.getNumericValue(block.cost);
                    if (block.visitAndReturnWhetherStepWasAlreadyTaken(newHeatLost, new Visit(newDirection, i))) break;
                    queue.offer(new QueuedStep(y, newX, newHeatLost, newDirection, i, pastDirections));
                }
                newHeatLost = heatLost;
                newDirection = CardinalDirection.EAST;
                for (int i = 1; i <= 3; i++) {
                    newX = x+i;
                    if (newX >= gridWidth) break;
                    Block block = blocks[y][newX];
                    newHeatLost += Character.getNumericValue(block.cost);
                    if (block.visitAndReturnWhetherStepWasAlreadyTaken(newHeatLost, new Visit(newDirection, i))) break;
                    queue.offer(new QueuedStep(y, newX, newHeatLost, newDirection, i, pastDirections));
                }
            }
        }
    }

    private static void drawPath() {
        int y = gridLength-1;
        int x = gridWidth-1;
        blocks[0][0].cost = 'S';
        blocks[y][x].cost = 'E';
        while (directionsToEnd.size() > 1) {
            int lastElementIndex = directionsToEnd.size()-1;
            switch (directionsToEnd.get(lastElementIndex)) {
                case NORTH -> {y++; blocks[y][x].cost = '^';} // Could replace these
                case SOUTH -> {y--; blocks[y][x].cost = 'v';} // characters with dots
                case WEST -> {x++; blocks[y][x].cost = '<';}  // to more easily
                case EAST -> {x--; blocks[y][x].cost = '>';}  // see the path.
            }
            directionsToEnd.remove(lastElementIndex);
        }
    }

    private static void printPath() {
        System.out.println("\nPath taken:");
        for (int y = 0; y < gridLength; y++) {
            System.out.println();
            for (int x = 0; x < gridWidth; x++) {
                System.out.print(blocks[y][x].cost);
            }
        }
        System.out.println("\n\nHeat lost on best path:\n\n"+heatLostOnBestPath);
    }

}
