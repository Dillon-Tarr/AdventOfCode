package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day22 {
    static private final int DAY = 22;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private int DEPTH, TARGET_Y, TARGET_X, Y_BOUNDARY, X_BOUNDARY;
    static private int[][] regionTypes;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        prepare();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            DEPTH =  Integer.parseInt(s.substring(s.lastIndexOf(' ')+1));
            s = br.readLine();
            var ss = s.substring(s.lastIndexOf(' ')+1).split(",");
            TARGET_Y = Integer.parseInt(ss[1]);
            TARGET_X = Integer.parseInt(ss[0]);
        } catch (IOException e) {throw new RuntimeException(e);}
        Y_BOUNDARY = TARGET_Y + 999;
        X_BOUNDARY = TARGET_X + 999;
    }

    private static void prepare() {
        var erosionLevels = new int[Y_BOUNDARY + 1][X_BOUNDARY + 1];
        regionTypes = new int[Y_BOUNDARY+1][X_BOUNDARY+1];
        erosionLevels[0][0] = calculateErosionLevel(0);
        for (int x = 1; x <= X_BOUNDARY; x++) {
            int level = calculateErosionLevel(x * 16807);
            erosionLevels[0][x] = level; regionTypes[0][x] = level % 3;
        }
        for (int y = 1; y <= Y_BOUNDARY; y++) {
            int level = calculateErosionLevel(y * 48271);
            erosionLevels[y][0] = level; regionTypes[y][0] = level % 3;
        }
        for (int y = 1; y <= Y_BOUNDARY; y++) {
            int leftLevel = erosionLevels[y][0];
            if (y == TARGET_Y) {
                for (int x = 1; x < TARGET_X; x++) {
                    leftLevel = calculateErosionLevel(leftLevel * erosionLevels[y-1][x]);
                    erosionLevels[y][x] = leftLevel; regionTypes[y][x] = leftLevel % 3;
                }
                leftLevel = erosionLevels[0][0];
                for (int x = TARGET_X+1; x <= X_BOUNDARY; x++) {
                    leftLevel = calculateErosionLevel(leftLevel * erosionLevels[y-1][x]);
                    erosionLevels[y][x] = leftLevel; regionTypes[y][x] = leftLevel % 3;
                }
            } else {
                for (int x = 1; x <= X_BOUNDARY; x++) {
                    leftLevel = calculateErosionLevel(leftLevel * erosionLevels[y-1][x]);
                    erosionLevels[y][x] = leftLevel; regionTypes[y][x] = leftLevel % 3;
                }
            }
        }
    }

    private static int calculateErosionLevel(int geologicIndex) {
        return (geologicIndex + DEPTH) % 20183;
    }

    private static void solvePart1() {
        int riskLevelSum = 0;
        for (int y = 0; y <= TARGET_Y; y++) for (int x = 0; x <= TARGET_X; x++) riskLevelSum += regionTypes[y][x];
        System.out.println("\nRisk level sum (part 1 answer): "+riskLevelSum);
    }

    private static void solvePart2() {
//        region types: 0, 1, 2 (rocky, wet, narrow)
//        equipment types: 0, 1, 2 (none, torch, climbingGear)
//        allowed equipment types by region type (region > equipment):
//        0 > 1, 2
//        1 > 0, 2
//        2 > 0, 1

    }

}
