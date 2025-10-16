package year2016;

import shared.CardinalDirection;
import shared.Coordinates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

import static year2016.Day13.TileDatum.*;
import static shared.CardinalDirection.*;

class Day13 {
    static private final int DAY = 13;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private int officeDesignersFavoriteNumber;
    static private TileDatum[][] tileData; // Referenced by [y][x]; Looking for 31,39 (x,y) or [39][31];
    static private final int gridSize = 52;
    static private final PriorityQueue<QueuedStep> queuedSteps = new PriorityQueue<>();

    static void main(String[] args) {
        long startTime = System.nanoTime();

        getOfficeDesignersFavoriteNumber();
        initializeTileData();
        queueInitialState();
        if (args.length == 0) findFewestStepsToGoalCoordinates(); // Part 1
        else countReachableTilesInAtMostFiftySteps(); // Part 2

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getOfficeDesignersFavoriteNumber() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            officeDesignersFavoriteNumber = Integer.parseInt(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void initializeTileData() {
        tileData = new TileDatum[gridSize][];
        for (int y = 0; y < tileData.length; y++) {
            tileData[y] = new TileDatum[gridSize];
            for (int x = 0; x < gridSize; x++) tileData[y][x] = UNEVALUATED;
        }
        tileData[1][1] = OPEN;
    }

    private static void queueInitialState() {
        queuedSteps.add(new QueuedStep(new Coordinates(1, 1), new ArrayList<>(), NONE, 0));
    }

    private static void findFewestStepsToGoalCoordinates() {
        Coordinates goalCoordinates = new Coordinates(39, 31);

        QueuedStep step;
        Coordinates coordinates, newCoordinates;
        ArrayList<Coordinates> visitedCoordinates = new ArrayList<>();
        int y, x, newY, newX, stepsTaken = 0, newStepsTaken;
        CardinalDirection direction;

        while (!queuedSteps.isEmpty()) {
            step = queuedSteps.remove(); coordinates = step.coordinates;
            stepsTaken = step.stepsTaken; visitedCoordinates = step.visitedCoordinates;
            if (coordinates.equals(goalCoordinates)) break;
            y = coordinates.y; x = coordinates.x;
            direction = step.direction; newStepsTaken = stepsTaken+1;
            if (direction != SOUTH && y > 0) {
                newY = y-1;
                if (tileData[newY][x] == UNEVALUATED) tileData[newY][x] = tileData[newY][x].evaluateWhetherOpen(newY, x) ? OPEN : WALL;
                if (tileData[newY][x] == OPEN) {
                    newCoordinates = new Coordinates(newY, x);
                    if (!visitedCoordinates.contains(newCoordinates)) queuedSteps.add(new QueuedStep(newCoordinates, visitedCoordinates, NORTH, newStepsTaken));
                }
            }
            if (direction != NORTH && y < gridSize-1) {
                newY = y+1;
                if (tileData[newY][x] == UNEVALUATED) tileData[newY][x] = tileData[newY][x].evaluateWhetherOpen(newY, x) ? OPEN : WALL;
                if (tileData[newY][x] == OPEN) {
                    newCoordinates = new Coordinates(newY, x);
                    if (!visitedCoordinates.contains(newCoordinates)) queuedSteps.add(new QueuedStep(newCoordinates, visitedCoordinates, SOUTH, newStepsTaken));
                }
            }
            if (direction != EAST && x > 0) {
                newX = x-1;
                if (tileData[y][newX] == UNEVALUATED) tileData[y][newX] = tileData[y][newX].evaluateWhetherOpen(y, newX) ? OPEN : WALL;
                if (tileData[y][newX] == OPEN) {
                    newCoordinates = new Coordinates(y, newX);
                    if (!visitedCoordinates.contains(newCoordinates)) queuedSteps.add(new QueuedStep(newCoordinates, visitedCoordinates, WEST, newStepsTaken));
                }
            }
            if (direction != WEST && x < gridSize-1) {
                newX = x+1;
                if (tileData[y][newX] == UNEVALUATED) tileData[y][newX] = tileData[y][newX].evaluateWhetherOpen(y, newX) ? OPEN : WALL;
                if (tileData[y][newX] == OPEN) {
                    newCoordinates = new Coordinates(y, newX);
                    if (!visitedCoordinates.contains(newCoordinates)) queuedSteps.add(new QueuedStep(newCoordinates, visitedCoordinates, EAST, newStepsTaken));
                }
            }
        }

        // Answer found. Printing findings:
        System.out.print("\nGraph of tiles ('O' - visited on optimal path; '.' - open; '#' - wall; '?' - unknown):");
        for (int y2 = 0; y2 < gridSize; y2++) {
            System.out.println();
            for (int x2 = 0; x2 < gridSize; x2++) {
                if (visitedCoordinates.contains(new Coordinates(y2, x2))) System.out.print('O');
                else System.out.print(switch (tileData[y2][x2]){
                    case OPEN -> '.';
                    case WALL -> '#';
                    case UNEVALUATED -> '?';
                });
            }
        }
        System.out.println("\n\nCoordinates visited in order: ");
        for (int i = 0; i < visitedCoordinates.size(); i++) System.out.println("After "+i+" steps: "+visitedCoordinates.get(i));
        System.out.println("\nFewest steps needed to reach goal coordinates: "+stepsTaken);

    }

    private static void countReachableTilesInAtMostFiftySteps() {
        QueuedStep step;
        Coordinates coordinates, newCoordinates;
        ArrayList<Coordinates> visitedCoordinates;
        int y, x, newY, newX, stepsTaken, newStepsTaken;
        CardinalDirection direction;

        while (!queuedSteps.isEmpty()) {
            step = queuedSteps.remove(); stepsTaken = step.stepsTaken;
            if (stepsTaken == 50) break;
            coordinates = step.coordinates; y = coordinates.y; x = coordinates.x;
            direction = step.direction; newStepsTaken = stepsTaken+1; visitedCoordinates = step.visitedCoordinates;
            if (direction != SOUTH && y > 0) {
                newY = y-1;
                if (tileData[newY][x] == UNEVALUATED) tileData[newY][x] = tileData[newY][x].evaluateWhetherOpen(newY, x) ? OPEN : WALL;
                if (tileData[newY][x] == OPEN) {
                    newCoordinates = new Coordinates(newY, x);
                    if (!visitedCoordinates.contains(newCoordinates)) queuedSteps.add(new QueuedStep(newCoordinates, visitedCoordinates, NORTH, newStepsTaken));
                }
            }
            if (direction != NORTH && y < gridSize-1) {
                newY = y+1;
                if (tileData[newY][x] == UNEVALUATED) tileData[newY][x] = tileData[newY][x].evaluateWhetherOpen(newY, x) ? OPEN : WALL;
                if (tileData[newY][x] == OPEN) {
                    newCoordinates = new Coordinates(newY, x);
                    if (!visitedCoordinates.contains(newCoordinates)) queuedSteps.add(new QueuedStep(newCoordinates, visitedCoordinates, SOUTH, newStepsTaken));
                }
            }
            if (direction != EAST && x > 0) {
                newX = x-1;
                if (tileData[y][newX] == UNEVALUATED) tileData[y][newX] = tileData[y][newX].evaluateWhetherOpen(y, newX) ? OPEN : WALL;
                if (tileData[y][newX] == OPEN) {
                    newCoordinates = new Coordinates(y, newX);
                    if (!visitedCoordinates.contains(newCoordinates)) queuedSteps.add(new QueuedStep(newCoordinates, visitedCoordinates, WEST, newStepsTaken));
                }
            }
            if (direction != WEST && x < gridSize-1) {
                newX = x+1;
                if (tileData[y][newX] == UNEVALUATED) tileData[y][newX] = tileData[y][newX].evaluateWhetherOpen(y, newX) ? OPEN : WALL;
                if (tileData[y][newX] == OPEN) {
                    newCoordinates = new Coordinates(y, newX);
                    if (!visitedCoordinates.contains(newCoordinates)) queuedSteps.add(new QueuedStep(newCoordinates, visitedCoordinates, EAST, newStepsTaken));
                }
            }
        }
        int count = 0;
        System.out.print("\nGraph of tiles ('.' - visitable/open; '#' - wall; '?' - unknown):");
        for (int y2 = 0; y2 < gridSize; y2++) {
            System.out.println();
            for (int x2 = 0; x2 < gridSize; x2++) {
                switch (tileData[y2][x2]) {
                    case OPEN -> {
                        System.out.print('.');
                        count++;
                    }
                    case WALL -> System.out.print('#');
                    case UNEVALUATED -> System.out.print('?');
                }
            }
        }
        System.out.println("\n\nReachable tiles in at most fifty steps: "+count);
    }

    private static class QueuedStep implements Comparable<QueuedStep> {
        Coordinates coordinates;
        ArrayList<Coordinates> visitedCoordinates;
        CardinalDirection direction;
        int stepsTaken;

        QueuedStep(Coordinates coordinates, ArrayList<Coordinates> visitedCoordinates, CardinalDirection direction, int stepsTaken) {
            this.coordinates = coordinates;
            this.visitedCoordinates = new ArrayList<>(visitedCoordinates);
            this.visitedCoordinates.add(coordinates);
            this.direction = direction;
            this.stepsTaken = stepsTaken;
        }

        @Override
        public int compareTo(QueuedStep o) { return Integer.compare(stepsTaken, o.stepsTaken); }
    }
    
    enum TileDatum {
        OPEN,
        WALL,
        UNEVALUATED;

        boolean evaluateWhetherOpen(int y, int x) {
            int n = x*x + 3*x + 2*x*y + y + y*y + officeDesignersFavoriteNumber;
            String binary = Integer.toBinaryString(n);
            boolean evenOneCount = true; // If so after counting 1s, open space. Else wall.
            for (int i = 0; i < binary.length(); i++) if (binary.charAt(i) == '1') evenOneCount = !evenOneCount;
            return evenOneCount;
        }
    }

}
