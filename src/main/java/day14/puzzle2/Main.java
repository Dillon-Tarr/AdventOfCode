package day14.puzzle2;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    static private final File INPUT_FILE = new File("input-files/day14input.txt");
    static private char[][] platform;
    static private int spinCycleCount = 0;
    static private final int cycleGoal = 1000000000;
    static private final ArrayList<String> historyStrings = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        setPlatformToPositionThatWouldResultFromOneBillionSpinCycles();
        measureLoadOnNorthSupportBeams();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(INPUT_FILE))) {
            lnr.skip(Long.MAX_VALUE);
            platform = new char[lnr.getLineNumber()][];
        } catch (IOException e) {throw new RuntimeException();}
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            for (int i = 0; i < platform.length; i++) platform[i] = br.readLine().toCharArray();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void setPlatformToPositionThatWouldResultFromOneBillionSpinCycles() {
        historyStrings.add(Arrays.deepToString(platform));
        int loopLength = -1;
        while (spinCycleCount < cycleGoal) {
            performSpinCycle();
            String currentString = Arrays.deepToString(platform);
            for (int i = 0; i < historyStrings.size(); i++) if (currentString.equals(historyStrings.get(i))) loopLength = spinCycleCount-i;
            if (loopLength != -1) break;
            historyStrings.add(currentString);
        }
        if (loopLength != -1) {
            System.out.println("\nLoop found after "+spinCycleCount+" cycles.\nLoop length: "+loopLength);
            System.out.println("\nAdded loop length to cycle count repeatedly until just before or at the goal. Continuing to goal.\n");
            while (spinCycleCount <= cycleGoal-loopLength) spinCycleCount += loopLength;
        }
        while (spinCycleCount < cycleGoal) performSpinCycle();
    }

    private static void performSpinCycle() {
        tiltPlatformNorth();
        tiltPlatformWest();
        tiltPlatformSouth();
        tiltPlatformEast();
        spinCycleCount++;
        System.out.println("Cycles completed: "+spinCycleCount);
    }

    private static void measureLoadOnNorthSupportBeams() {
        int load = 0;
        int currentLoadPerRock = platform.length+1;
        for (char[] row : platform) {
            currentLoadPerRock--;
            for (int x = 0; x < platform[0].length; x++) if (row[x] == 'O') load += currentLoadPerRock;
        }
        System.out.println("\nTotal load on north support beams after "+cycleGoal+" cycles: "+load);
    }

    private static void tiltPlatformNorth() {
        for (int y = 1; y < platform.length; y++)
            for (int x = 0; x < platform[0].length; x++)
                if (platform[y][x] == 'O') rollRockNorth(y, x);
    }
    private static void tiltPlatformWest() {
        for (int x = 1; x < platform[0].length; x++)
            for (int y = 0; y < platform.length; y++)
                if (platform[y][x] == 'O') rollRockWest(y, x);
    }
    private static void tiltPlatformSouth() {
        for (int y = platform.length-2; y >= 0; y--)
            for (int x = 0; x < platform[0].length; x++)
                if (platform[y][x] == 'O') rollRockSouth(y, x);
    }
    private static void tiltPlatformEast() {
        for (int x = platform[0].length-2; x >= 0; x--)
            for (int y = 0; y < platform.length; y++)
                if (platform[y][x] == 'O') rollRockEast(y, x);
    }
    private static void rollRockNorth(int startY, int x) {
        int y = startY;
        while (y > 0) {
            if (platform[y-1][x] == '.') y--;
            else break;
        }
        if (y < startY) {
            platform[y][x] = 'O';
            platform[startY][x] = '.';
        }
    }
    private static void rollRockWest(int y, int startX) {
        int x = startX;
        while (x > 0) {
            if (platform[y][x-1] == '.') x--;
            else break;
        }
        if (x < startX) {
            platform[y][x] = 'O';
            platform[y][startX] = '.';
        }
    }
    private static void rollRockSouth(int startY, int x) {
        int y = startY;
        while (y < platform.length-1) {
            if (platform[y+1][x] == '.') y++;
            else break;
        }
        if (y > startY) {
            platform[y][x] = 'O';
            platform[startY][x] = '.';
        }
    }
    private static void rollRockEast(int y, int startX) {
        int x = startX;
        while (x < platform[0].length-1) {
            if (platform[y][x+1] == '.') x++;
            else break;
        }
        if (x > startX) {
            platform[y][x] = 'O';
            platform[y][startX] = '.';
        }
    }

}
