package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day11 {
    static private final int DAY = 11;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private int serialNumber;
    static private final int[][] powerLevels = new int[301][];

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        calculatePowerLevels();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            serialNumber = Integer.parseInt(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void calculatePowerLevels() {
        for (int y = 1; y <= 300; y++) {
            powerLevels[y] = new int[301];
            for (int x = 1; x <= 300; x++) {
                int rackID = x+10;
                int value = rackID*y;
                value += serialNumber;
                value *= rackID;
                if (value < 100) value = -5;
                else {
                    String v = String.valueOf(value);
                    value = Character.getNumericValue(v.charAt(v.length()-3));
                    value -= 5;
                }
                powerLevels[y][x] = value;
            }
        }
    }

    private static void solvePart1() {
        int highestSum = Integer.MIN_VALUE;
        String highestXYCoordinates = "???";
        for (int y = 1; y <= 298; y++) {
            for (int x = 1; x <= 298; x++) {
                int sum = powerLevels[y][x] + powerLevels[y][x+1] + powerLevels[y][x+2] +
                        powerLevels[y+1][x] + powerLevels[y+1][x+1] + powerLevels[y+1][x+2] +
                        powerLevels[y+2][x] + powerLevels[y+2][x+1] + powerLevels[y+2][x+2];
                if (sum > highestSum) {
                    highestSum = sum;
                    highestXYCoordinates = x+","+y;
                }
            }
        }
        System.out.println("\nx,y coordinates of highest 3x3-sum area (part 1 answer): "+highestXYCoordinates);
    }

    private static void solvePart2() {
        // TODO: Map previously found sums for reuse.
        int highestSum = Integer.MIN_VALUE;
        String highestXYSizeInfo = "???";
        for (int y = 1; y <= 300; y++) {
            for (int x = 1; x <= 300; x++) {
                for (int s = 1; s <= 300; s++) {
                    int farY = y+s-1; if (farY > 300) continue;
                    int farX = x+s-1; if (farX > 300) continue;
                    int sum = 0;
                    for (int innerY = y; innerY <= farY; innerY++) {
                        for (int innerX = x; innerX <= farX; innerX++) {
                            sum += powerLevels[innerY][innerX];
                        }
                    }
                    if (sum > highestSum) {
                        highestSum = sum;
                        highestXYSizeInfo = x+","+y+","+s;
                    }
                }
            }
        }
        System.out.println("\nx,y,size info of highest sum area (part 2 answer): "+ highestXYSizeInfo);
    }

}
