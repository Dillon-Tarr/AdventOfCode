package day14.puzzle1;

import java.io.*;

public class Main {
    static private final File INPUT_FILE = new File("input-files/day14input.txt");
    static private char[][] platform;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        tiltPlatformSoRoundedRocksRollNorth();
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

    private static void measureLoadOnNorthSupportBeams() {
        int load = 0;
        int currentLoadPerRock = platform.length+1;
        for (char[] row : platform) {
            currentLoadPerRock--;
            for (int x = 0; x < platform[0].length; x++) if (row[x] == 'O') load += currentLoadPerRock;
        }
        System.out.println("\nTotal load on north support beams after rolling rocks north:\n\n"+load);
    }

    private static void tiltPlatformSoRoundedRocksRollNorth() {
        for (int y = 1; y < platform.length; y++)
            for (int x = 0; x < platform[0].length; x++)
                if (platform[y][x] == 'O') rollRockNorth(y, x);
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

}
