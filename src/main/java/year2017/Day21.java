package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

class Day21 {
    static private final int DAY = 21;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<EnhancementRule> size2Rules = new ArrayList<>();
    static private final ArrayList<EnhancementRule> size3Rules = new ArrayList<>();
    static private final int iterations = 18;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        initializeAndSolve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            EnhancementRule rule;
            String s = br.readLine();
            while (s != null) {
                rule = new EnhancementRule(s);
                if (rule.size == 2) size2Rules.add(rule);
                else size3Rules.add(rule);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void initializeAndSolve() {
        boolean[][] grid = new boolean[3][], newGrid, oldSquare, newSquare;
        grid[0] = new boolean[]{false, true, false};
        grid[1] = new boolean[]{false, false, true};
        grid[2] = new boolean[]{true, true, true};
        int oldRowSize, newRowSize, oldGridSize, newGridSize = 3, subY, subX;
        ArrayDeque<boolean[][]> newSquares = new ArrayDeque<>();
        for (int iterationsCompleted = 0; iterationsCompleted < iterations; iterationsCompleted++) {
            if (iterationsCompleted == 5) System.out.println("\nNumber of \"on\" pixels after 5 iterations (part 1 answer): "+countTrues(grid));
            oldGridSize = newGridSize;
            if (oldGridSize % 2 == 0) {
                oldRowSize = 2;
                newRowSize = 3;
            } else {
                oldRowSize = 3;
                newRowSize = 4;
            }
            newGridSize = (oldGridSize/oldRowSize)*newRowSize;
            newSquares.clear();
            for (int squareY = 0; squareY < oldGridSize; squareY += oldRowSize) {
                xSquareLoop: for (int squareX = 0; squareX < oldGridSize; squareX += oldRowSize) {
                    oldSquare = new boolean[oldRowSize][oldRowSize];
                    subY = -1;
                    for (int y = squareY; y < squareY+oldRowSize; y++) {
                        subY++; subX = 0;
                        for (int x = squareX; x < squareX+oldRowSize; x++) oldSquare[subY][subX++] = grid[y][x];
                    }
                    for (EnhancementRule rule : oldRowSize == 2 ? size2Rules : size3Rules) {
                        if (rule.checkForMatch(oldSquare)) {
                            newSquares.add(rule.output);
                            continue xSquareLoop;
                        }
                    }
                    throw new RuntimeException("This means a matching rule was not found! Iterations completed: "+iterationsCompleted);
                }
            }
            newGrid = new boolean[newGridSize][newGridSize];
            for (int squareY = 0; squareY < newGridSize; squareY += newRowSize) {
                for (int squareX = 0; squareX < newGridSize; squareX += newRowSize) {
                    newSquare = newSquares.remove();
                    subY = -1;
                    for (int y = squareY; y < squareY+newRowSize; y++) {
                        subY++; subX = 0;
                        for (int x = squareX; x < squareX+newRowSize; x++) {
                            newGrid[y][x] = newSquare[subY][subX++];
                        }
                    }
                }
            }
            grid = newGrid;
        }
        if (iterations == 18) System.out.println("\nNumber of \"on\" pixels after 18 iterations (part 2 answer): "+countTrues(grid));
        else System.out.println("\nNumber of \"on\" pixels after "+iterations+" iterations (change to 18 if wanting puzzle answer): "+countTrues(grid));
    }

    private static int countTrues(boolean[][] grid) {
        int count = 0;
        for (int y = 0; y < grid.length; y++) for (int x = 0; x < grid.length; x++) if (grid[y][x]) count++;
        return count;
    }

    private static class EnhancementRule {
        int size;
        boolean[][][] matchingInputs = new boolean[8][][];
        int uniqueInputCount = 1;
        boolean[][] output;

        EnhancementRule(String s) {
            size = s.indexOf('/');
            String[] inStringRows = s.substring(0, s.indexOf(' ')).split("/");
            boolean[][] originalInput = new boolean[size][size]; // Original pattern:
            int inverseY, inverseX;
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) originalInput[y][x] = inStringRows[y].charAt(x) == '#';
            }
            matchingInputs[0] = originalInput;
            boolean[][] alteredInput = new boolean[size][size]; // xFlip:
            for (int y = 0; y < size; y++) {
                inverseX = size;
                for (int x = 0; x < size; x++) alteredInput[y][x] = originalInput[y][--inverseX];
            }
            if (!this.checkForMatch(alteredInput)) matchingInputs[uniqueInputCount++] = alteredInput;
            alteredInput = new boolean[size][size]; //yFlip:
            for (int x = 0; x < size; x++) {
                inverseY = size;
                for (int y = 0; y < size; y++) alteredInput[y][x] = originalInput[--inverseY][x];
            }
            if (!this.checkForMatch(alteredInput)) matchingInputs[uniqueInputCount++] = alteredInput;
            alteredInput = new boolean[size][size]; // bottomLeftToTopRightAxisFlip:
            inverseY = size;
            for (int y = 0; y < size; y++) {
                inverseY--; inverseX = size;
                for (int x = 0; x < size; x++) alteredInput[--inverseX][inverseY] = originalInput[y][x];
            }
            if (!this.checkForMatch(alteredInput)) matchingInputs[uniqueInputCount++] = alteredInput;
            alteredInput = new boolean[size][size]; // topLeftToBottomRightAxisFlip:
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    alteredInput[x][y] = originalInput[y][x];
                }
            }
            if (!this.checkForMatch(alteredInput)) matchingInputs[uniqueInputCount++] = alteredInput;
            alteredInput = new boolean[size][size]; // rotateRight1:
            inverseY = size;
            for (int y = 0; y < size; y++) {
                inverseY--;
                for (int x = 0; x < size; x++) {
                    alteredInput[x][inverseY] = originalInput[y][x];
                }
            }
            if (!this.checkForMatch(alteredInput)) matchingInputs[uniqueInputCount++] = alteredInput;
            alteredInput = new boolean[size][size]; // rotateRight2:
            inverseY = size;
            for (int y = 0; y < size; y++) {
                inverseY--; inverseX = size;
                for (int x = 0; x < size; x++) {
                    inverseX--;
                    alteredInput[inverseY][inverseX] = originalInput[y][x];
                }
            }
            if (!this.checkForMatch(alteredInput)) matchingInputs[uniqueInputCount++] = alteredInput;
            alteredInput = new boolean[size][size]; // rotateRight3 (rotateLeft1):
            for (int y = 0; y < size; y++) {
                inverseX = size;
                for (int x = 0; x < size; x++) {
                    alteredInput[--inverseX][y] = originalInput[y][x];
                }
            }
            if (!this.checkForMatch(alteredInput)) matchingInputs[uniqueInputCount++] = alteredInput;

            String[] outStringRows = s.substring(s.lastIndexOf(' ')+1).split("/");
            int outputSize = size+1;
            output = new boolean[outputSize][outputSize];
            for (int y = 0; y < outputSize; y++) for (int x = 0; x < outputSize; x++) output[y][x] = outStringRows[y].charAt(x) == '#';
        }

        boolean checkForMatch(boolean[][] input) {
            for (int i = 0; i < uniqueInputCount; i++) {
                if (!Arrays.deepEquals(input, matchingInputs[i])) continue;
                return true;
            }
            return false;
        }

    }

}
