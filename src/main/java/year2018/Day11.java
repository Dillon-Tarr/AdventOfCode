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
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
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

    private static void solve() {
        int highestSum = Integer.MIN_VALUE, part1HighestSum = Integer.MIN_VALUE, size, sum, farY, farX; int[] row;
        String highestXYSizeInfo = "???", highestPart1XYCoordinates = "???";
        for (int topLeftY = 1; topLeftY <= 300; topLeftY++) {
            for (int topLeftX = 1; topLeftX <= 300; topLeftX++) {
                size = 1; sum = powerLevels[topLeftY][topLeftX];
                farY = topLeftY; farX = topLeftX;
                while (++farY <= 300 && ++farX <= 300) {
                    row = powerLevels[farY]; for (int subX = topLeftX; subX <= farX; subX++) sum += row[subX];
                    for (int subY = topLeftY; subY < farY; subY++) sum += powerLevels[subY][farX];
                    if (++size == 3 && sum > part1HighestSum) { part1HighestSum = sum; highestPart1XYCoordinates = topLeftX+","+topLeftY; }
                    if (sum > highestSum) { highestSum = sum; highestXYSizeInfo = topLeftX+","+topLeftY+","+size; }
                }
            }
        }
        System.out.println("\nx,y coordinates of highest 3x3-sum area (part 1 answer): "+highestPart1XYCoordinates);
        System.out.println("\nx,y,size info of highest square-area sum (part 2 answer): "+highestXYSizeInfo);
    }

}
