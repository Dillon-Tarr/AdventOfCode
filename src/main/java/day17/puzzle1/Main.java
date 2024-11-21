package day17.puzzle1;

import day17.CardinalDirection;

import java.io.*;
import java.util.PriorityQueue;

public class Main {
    static private final File INPUT_FILE = new File("input-files/day17input.txt");
    static private Block[][] blocks;
    static private int gridLength, gridWidth;
    static private final PriorityQueue<QueuedStep> queue = new PriorityQueue<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        findBestPath();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
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
                    blocks[y][x] = new Block(y, x, Character.getNumericValue(line.charAt(x)));
                }
                line = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void findBestPath() {
        queue.add(new QueuedStep(0, 0, 0, CardinalDirection.EAST));
        queue.add(new QueuedStep(0, 0, 0, CardinalDirection.SOUTH));
        int heatLostOnBestPath = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            QueuedStep step = queue.poll(); assert step != null;
            int y = step.y(), x = step.x(), heatLost = step.heatLost();
            if (y == gridLength-1 && x == gridWidth-1 && heatLost < heatLostOnBestPath) { heatLostOnBestPath = heatLost; break; }

            int newHeatLost = heatLost;
            CardinalDirection newDirection;
            switch (step.fromDirection()) {
                case WEST, EAST -> {
                    int newY;
                    newDirection = CardinalDirection.NORTH;
                    for (int i = 1; i <= 3; i++) {
                        newY = y-i;
                        if (newY < 0) break;
                        Block block = blocks[newY][x];
                        newHeatLost += block.cost;
                        if (block.stepWasAlreadyTaken(new Visit(newDirection, i))) break;
                        queue.offer(new QueuedStep(newY, x, newHeatLost, newDirection));
                    }
                    newHeatLost = heatLost;
                    newDirection = CardinalDirection.SOUTH;
                    for (int i = 1; i <= 3; i++) {
                        newY = y+i;
                        if (newY >= gridLength) break;
                        Block block = blocks[newY][x];
                        newHeatLost += block.cost;
                        if (block.stepWasAlreadyTaken(new Visit(newDirection, i))) break;
                        queue.offer(new QueuedStep(newY, x, newHeatLost, newDirection));
                    }
                }
                case NORTH, SOUTH -> {
                    newDirection = CardinalDirection.WEST;
                    int newX;
                    for (int i = 1; i <= 3; i++) {
                        newX = x-i;
                        if (newX < 0) break;
                        Block block = blocks[y][newX];
                        newHeatLost += block.cost;
                        if (block.stepWasAlreadyTaken(new Visit(newDirection, i))) break;
                        queue.offer(new QueuedStep(y, newX, newHeatLost, newDirection));
                    }
                    newHeatLost = heatLost;
                    newDirection = CardinalDirection.EAST;
                    for (int i = 1; i <= 3; i++) {
                        newX = x+i;
                        if (newX >= gridWidth) break;
                        Block block = blocks[y][newX];
                        newHeatLost += block.cost;
                        if (block.stepWasAlreadyTaken(new Visit(newDirection, i))) break;
                        queue.offer(new QueuedStep(y, newX, newHeatLost, newDirection));
                    }
                }
            }
        }
        System.out.println("\nHeat lost on best path:\n\n"+heatLostOnBestPath);
    }

}
