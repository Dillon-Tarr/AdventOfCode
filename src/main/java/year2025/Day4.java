package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day4 {
    static private final int DAY = 4;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private char[][] inputChars;
    static private int[][] adjacentRollCounts;
    static private int width, height;

    static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        solve(args.length > 0); // Enter any arg for printMode.

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        ArrayList<String> inputStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            width = s.length();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        height = inputStrings.size();
        inputChars = new char[height][];
        for (int i = 0; i < height; i++) inputChars[i] = inputStrings.get(i).toCharArray();
        adjacentRollCounts = new int[height][width];
    }

    private static void solve(boolean printMode) {
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
            if (inputChars[y][x] == '@') incrementAdjacentCounts(y, x);
        }
        int count = 0;
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
            if (inputChars[y][x] == '@' && adjacentRollCounts[y][x] < 4) {
                inputChars[y][x] = 'x'; count++;
            }
        }
        int part1Count = count;
        if (printMode) {
            System.out.println("\nFound all rolls which could initially be removed.");
            printMap();
            System.out.println("\nRemoving the initially counted "+count+" rolls...");
        }
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
            if (inputChars[y][x] == 'x') { inputChars[y][x] = '.'; decrementAdjacentCounts(y, x); }
        }
        boolean foundOne = true;
        while (foundOne) {
            foundOne = false;
            if (printMode) {
                printMap();
                System.out.println("\nTrying to find and remove more rolls. Current count: "+count);
            }
            for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
                if (inputChars[y][x] == '@' && adjacentRollCounts[y][x] < 4) {
                    inputChars[y][x] = '.'; decrementAdjacentCounts(y, x); count++; foundOne = true;
                }
            }
        }
        if (printMode) System.out.println("There are no more to remove.");
        System.out.println("\nNumber of rolls initially adjacent to fewer than 4 rolls (part 1 answer): "+part1Count);
        System.out.println("\nIteratively removed all reachable paper rolls. Total count (part 2 answer): "+count);
    }

    private static void incrementAdjacentCounts(int y, int x) {
        boolean yMin = y == 0, yMax = y >= height-1, xMin = x == 0, xMax = x >= width-1;
        if (!yMin) {
            ++adjacentRollCounts[y-1][x];
            if (!xMin) ++adjacentRollCounts[y-1][x-1];
            if (!xMax) ++adjacentRollCounts[y-1][x+1];
        }
        if (!yMax) {
            ++adjacentRollCounts[y+1][x];
            if (!xMin) ++adjacentRollCounts[y+1][x-1];
            if (!xMax) ++adjacentRollCounts[y+1][x+1];
        }
        if (!xMin) ++adjacentRollCounts[y][x-1];
        if (!xMax) ++adjacentRollCounts[y][x+1];
    }

    private static void decrementAdjacentCounts(int y, int x) {
        boolean yMin = y == 0, yMax = y >= height-1, xMin = x == 0, xMax = x >= width-1;
        if (!yMin) {
            --adjacentRollCounts[y-1][x];
            if (!xMin) --adjacentRollCounts[y-1][x-1];
            if (!xMax) --adjacentRollCounts[y-1][x+1];
        }
        if (!yMax) {
            --adjacentRollCounts[y+1][x];
            if (!xMin) --adjacentRollCounts[y+1][x-1];
            if (!xMax) --adjacentRollCounts[y+1][x+1];
        }
        if (!xMin) --adjacentRollCounts[y][x-1];
        if (!xMax) --adjacentRollCounts[y][x+1];
    }

    private static void printMap() {
        var sb = new StringBuilder();
        System.out.println("Map:");
        for (int y = 0; y < height; y++) {
            sb.setLength(0);
            for (int x = 0; x < width; x++) {
                sb.append(inputChars[y][x]);
            }
            System.out.println(sb);
        }
    }

}
