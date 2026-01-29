package year2023.day10.part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class InitialSolution {
    static private final int DAY = 10;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private int gridWidth;
    static private int gridHeight;
    static private char[][] grid;
    static private int startY; // Coordinates referenced in y-x order since values are always obtained in line-character order.
    static private int startX;
    static private int loopLength;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputDataAndStartCoordinates();
        evaluateLoopLength();
        printStepsNeededToGetToFurthestPointInLoop();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void printStepsNeededToGetToFurthestPointInLoop() {
        System.out.println("\nSteps needed to get to the furthest point in the loop:\n\n"+(loopLength/2));
    }

    private static void evaluateLoopLength() {
        int stepsTaken = 0;
        char currentCharacter = 'S';
        int currentY = startY;
        int currentX = startX;
        int previousY = startY;
        int previousX = startX;
        do {
            stepsTaken++;
            if (currentY != 0 && previousY != currentY-1 && (currentCharacter == '|' || currentCharacter == 'L' || currentCharacter == 'J') && // Go up
                    (grid[currentY -1][currentX] == '7' || grid[currentY-1][currentX] == '|' || grid[currentY-1][currentX] == 'F' || grid[currentY-1][currentX] == 'S')) {
                previousX = currentX;
                previousY = currentY;
                currentY--;
                currentCharacter = grid[currentY][currentX];
                continue;
            }
            if (currentX != gridWidth-1 && previousX != currentX+1 && (currentCharacter == '-' || currentCharacter == 'L' || currentCharacter == 'F') && // Go right
                    (grid[currentY][currentX+1] == 'J' || grid[currentY][currentX +1] == '-' || grid[currentY][currentX+1] == '7' || grid[currentY][currentX+1] == 'S')) {
                previousX = currentX;
                previousY = currentY;
                currentX++;
                currentCharacter = grid[currentY][currentX];
                continue;
            }
            if (currentY != gridHeight-1 && previousY != currentY+1 && (currentCharacter == '|' || currentCharacter == 'F' || currentCharacter == '7') && // Go down
                    (grid[currentY+1][currentX] == 'L' || grid[currentY+1][currentX] == '|' || grid[currentY+1][currentX] == 'J' || grid[currentY+1][currentX] == 'S')) {
                previousX = currentX;
                previousY = currentY;
                currentY++;
                currentCharacter = grid[currentY][currentX];
                continue;
            } // Else go left:
                previousX = currentX;
                previousY = currentY;
                currentX--;
                currentCharacter = grid[currentY][currentX];
        } while (currentY != startY || currentX != startX);
        loopLength = stepsTaken;
    }

    private static void getInputDataAndStartCoordinates() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            ArrayList<char[]> inputLines = new ArrayList<>();
            while (currentLine != null) {
                inputLines.add(currentLine.toCharArray());
                currentLine = br.readLine();
            }
            gridWidth = inputLines.get(0).length;
            gridHeight = inputLines.size();
            grid = new char[gridHeight][];
            for (int y = 0; y < gridHeight; y++) {
                grid[y] = new char[gridWidth];
                    for (int x = 0; x < gridWidth; x++) {
                        char value = inputLines.get(y)[x];
                        grid[y][x] = value;
                        if (value == 'S') {
                            startY = y;
                            startX = x;
                        }
                    }
                }
            } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
