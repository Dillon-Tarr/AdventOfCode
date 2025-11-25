package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class Day18 {
    static private final int DAY = 18;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private char[][] grid;
    static private final ArrayList<char[][]> historicGrids = new ArrayList<>(500);
    static private int height, width;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        ArrayList<String> inputStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        height = inputStrings.size();
        grid = new char[height][];
        for (int y = 0; y < inputStrings.size(); y++) grid[y] = inputStrings.get(y).toCharArray();
        width = grid[0].length;
    }

    private static void solvePart1() {
        historicGrids.add(grid);
        System.out.println("\nInitial state: ");
        printMap();
        for (int i = 1; i <= 10; i++) {
            updateGrid();
            historicGrids.add(grid);
            System.out.println("\nAfter "+i+" minutes: ");
            printMap();
        }
        System.out.println("\nResource value (part 1 answer): "+getResourceValue());
    }

    private static void solvePart2() {
        int i = 10, startOfPeriod = -1, periodSize = -1;
        mainLoop: while (++i < 999999) {
            updateGrid();
            for (int j = 0; j < historicGrids.size(); j++) if (Arrays.deepEquals(grid, historicGrids.get(j))) {
                startOfPeriod = j;
                periodSize = i-j;
                break mainLoop;
            }
            historicGrids.add(grid);
        }
        while (i < 1_000_000_000L) i+= periodSize;
        i -= periodSize;
        int minutesLeft = (int)(1_000_000_000L-i);
        grid = historicGrids.get(startOfPeriod+minutesLeft);
        System.out.println("\nResource value after 1 billion minutes (part 2 answer): "+getResourceValue());
    }

    private static void printMap() {
        var sb = new StringBuilder();
        char[] row;
        for (int y = 0; y < height; y++) {
            sb.setLength(0); row = grid[y];
            for (int x = 0; x < width; x++) sb.append(row[x]);
            System.out.println(sb);
        }
    }

    private static void updateGrid() {
        char[][] newGrid = new char[height][];
        for (int y = 0; y < height; y++) {
            newGrid[y] = new char[width];
            for (int x = 0; x < width; x++) {
                newGrid[y][x] = switch (grid[y][x]) {
                    case '.' -> getAdjacentCharCount('|', y, x) >= 3 ? '|' : '.';
                    case '|' -> getAdjacentCharCount('#', y, x) >= 3 ? '#' : '|';
                    case '#' -> isBy('#', y, x) && isBy('|', y, x) ? '#' : '.';
                    default -> throw new IllegalStateException("Unexpected value: " + grid[y][x]);
                };
            }
        }
        grid = newGrid.clone();
    }

    private static int getAdjacentCharCount(char ch, int y, int x) {
        int charCount = 0;
        if (y > 0) {
            if (x > 0 && grid[y-1][x-1] == ch) charCount++;
            if (grid[y-1][x] == ch) charCount++;
            if (x < width-1 && grid[y-1][x+1] == ch) charCount++;
        }
        if (x > 0 && grid[y][x-1] == ch) charCount++;
        if (x < width-1 && grid[y][x+1] == ch) charCount++;
        if (y < height-1) {
            if (x > 0 && grid[y+1][x-1] == ch) charCount++;
            if (grid[y+1][x] == ch) charCount++;
            if (x < width-1 && grid[y+1][x+1] == ch) charCount++;
        }
        return charCount;
    }

    private static boolean isBy(char ch, int y, int x) {
        if (y > 0) {
            if (x > 0 && grid[y-1][x-1] == ch) return true;
            if (grid[y-1][x] == ch) return true;
            if (x < width-1 && grid[y-1][x+1] == ch) return true;
        }
        if (x > 0 && grid[y][x-1] == ch) return true;
        if (x < width-1 && grid[y][x+1] == ch) return true;
        if (y < height-1) {
            if (x > 0 && grid[y+1][x-1] == ch) return true;
            if (grid[y+1][x] == ch) return true;
            return x < width-1 && grid[y+1][x+1] == ch;
        }
        return false;
    }

    private static int getResourceValue() {
        int woodsCount = 0, lumberyardCount = 0;
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) switch (grid[y][x]) {
            case '|' -> woodsCount++;
            case '#' -> lumberyardCount++;
        }
        return woodsCount*lumberyardCount;
    }

}
