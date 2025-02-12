package day23.part1;

import shared.Coordinates;

import java.io.*;
import java.util.ArrayList;

public class Main {
    static private final File INPUT_FILE = new File("input-files/day23input.txt");
    static private char[][] grid;
    static private int gridLength, gridWidth;
    static private final ArrayList<Step> steps = new ArrayList<>();
    static private int stepsOfLongestHike = -1;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        findLongestHike();

        System.out.println("\nExecution time in seconds: " + ((double) (System.nanoTime() - startTime) / 1000000000));
    }

    private static void processInputData() {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(INPUT_FILE))) {
            lnr.skip(Long.MAX_VALUE);
            gridLength = lnr.getLineNumber();
            grid = new char[gridLength][];
        } catch (IOException e) {throw new RuntimeException();}
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line = br.readLine();
            gridWidth = line.length();
            steps.add(new Step(0, line.indexOf("."), 0));
            for (int y = 0; y < gridLength; y++) {
                grid[y] = new char[gridWidth];
                for (int x = 0; x < gridWidth; x++) {
                    grid[y][x] = line.charAt(x);
                }
                line = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void findLongestHike() {
        int y, x, stepCount;
        ArrayList<Coordinates> pastCoordinates;
        Coordinates northCoordinates = new Coordinates(-1, -1),
                eastCoordinates = new Coordinates(-1, -1),
                southCoordinates = new Coordinates(-1, -1),
                westCoordinates = new Coordinates(-1, -1);
        while (!steps.isEmpty()) {
            Step step = steps.remove(0);
            y = step.currentCoordinates.y;
            x = step.currentCoordinates.x;
            stepCount = step.stepCount;
            pastCoordinates = step.pastCoordinates;

            if (y == gridLength-1) {
                if (stepCount > stepsOfLongestHike) stepsOfLongestHike = stepCount;
                continue;
            }

            northCoordinates.update(y-1, x);
            eastCoordinates.update(y, x+1);
            southCoordinates.update(y+1, x);
            westCoordinates.update(y, x-1);

            if (y > 0 && grid[y-1][x] == '.' && !pastCoordinates.contains(northCoordinates))
                steps.add(new Step(northCoordinates.y, northCoordinates.x, stepCount+1, pastCoordinates));
            if (x < gridWidth-1 && (grid[y][x+1] == '.' || grid[y][x+1] == '>') && !pastCoordinates.contains(eastCoordinates))
                steps.add(new Step(eastCoordinates.y, eastCoordinates.x, stepCount+1, pastCoordinates));
            if (y < gridLength-1 && (grid[y+1][x] == '.' || grid[y+1][x] == 'v') && !pastCoordinates.contains(southCoordinates))
                steps.add(new Step(southCoordinates.y, southCoordinates.x, stepCount+1, pastCoordinates));
            if (x > 0 && (grid[y][x-1] == '.' || grid[y][x-1] == '<') && !pastCoordinates.contains(westCoordinates))
                steps.add(new Step(westCoordinates.y, westCoordinates.x, stepCount+1, pastCoordinates));
        }

        System.out.println("Step count of longest hike: "+stepsOfLongestHike);
    }

}
