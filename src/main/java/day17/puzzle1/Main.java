package day17.puzzle1;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Main {
    static private final File INPUT_FILE = new File("input-files/day17input.txt");
    static private final char[] directions = {'s', 'e', 'w', 'n'};
    static private Block[][] blocks;
    static private int gridLength, gridWidth, mostPossibleEfficientHeatLoss;
    static private int heatLostOnBestPath = Integer.MAX_VALUE;
    static private final PriorityQueue<QueuedStep> queue = new PriorityQueue<>();
    static private final ArrayList<HeatLossIgnoringStep> pastVisits = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        getInputData();
        findBestPath();
        long endTime = System.currentTimeMillis();
        System.out.println("\nHeat lost on best path:\n\n"+heatLostOnBestPath);
        long executionDuration = endTime-startTime;
        System.out.println("\nExecution time in seconds: "+((double) executionDuration/1000));
    }

    private static void findBestPath() {
        queue.add(new QueuedStep(0, 0, 0, 0, 'x'));
        while (!queue.isEmpty()) {
            QueuedStep step = queue.poll();
            assert step != null;
            int y = step.y(), x = step.x(), heatLost = step.heatLost(), consecutiveStepsInOneDirection = step.consecutiveStepsInOneDirection();
            char lastDir = step.lastStepDirection();

            if (heatLost+(gridLength+gridWidth-y-x-2)*9 > mostPossibleEfficientHeatLoss) continue;
            if (heatLost > blocks[y][x].heatLossDistanceFromStart+18) continue;

            if (y == gridLength-1 && x == gridWidth-1 && heatLost < heatLostOnBestPath) {
                heatLostOnBestPath = heatLost;
                break;
            }

            HeatLossIgnoringStep visitRecordStep = new HeatLossIgnoringStep(step);
            boolean stepAlreadyHappened = false;
            for (HeatLossIgnoringStep historicalStep : pastVisits) {
                if (visitRecordStep.equals(historicalStep)) {stepAlreadyHappened = true; break;}
            }
            if (stepAlreadyHappened) continue;
            else pastVisits.add(visitRecordStep);

            for (char dir : directions){
                int newY = y, newX = x, newConsecutiveStepsInOneDirection = consecutiveStepsInOneDirection;
                if (lastDir == dir) {
                    if (consecutiveStepsInOneDirection >=3) continue;
                    else newConsecutiveStepsInOneDirection++;
                } else newConsecutiveStepsInOneDirection = 1;

                switch (dir) {
                    case 'w' -> {
                        if (lastDir == 'e' || x == 0) continue;
                        newX = x-1;}
                    case 'e' -> {
                        if (lastDir == 'w' || x == gridWidth-1) continue;
                        newX = x+1;}
                    case 'n' -> {
                        if (lastDir == 's' || y == 0) continue;
                        newY = y-1;}
                    case 's' -> {
                        if (lastDir == 'n' || y == gridLength-1) continue;
                        newY = y+1;}
                }
                int newHeatLost = heatLost+ blocks[newY][newX].cost;
                if (newHeatLost < blocks[newY][newX].heatLossDistanceFromStart) blocks[newY][newX].heatLossDistanceFromStart = newHeatLost;

                queue.offer(new QueuedStep(newY, newX, newHeatLost, newConsecutiveStepsInOneDirection, dir));
            }
        }
    }

    private static void getInputData() {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(INPUT_FILE))) {
            lnr.skip(Long.MAX_VALUE);
            gridLength = lnr.getLineNumber();
            blocks = new Block[gridLength][];
        } catch (IOException e) {throw new RuntimeException();}
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            for (int y = 0; y < blocks.length; y++) {
                String line = br.readLine();
                blocks[y] = new Block[line.length()];
                for (int x = 0; x < line.length(); x++) {
                    blocks[y][x] = new Block(y, x, Character.getNumericValue(line.charAt(x)));
                }
            }
            gridWidth = blocks[0].length;
            mostPossibleEfficientHeatLoss = (gridLength+gridWidth-2)*9;
        } catch (IOException e) {throw new RuntimeException(e);}
    }

}
