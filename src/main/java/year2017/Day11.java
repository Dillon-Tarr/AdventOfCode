package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day11 {
    static private final int DAY = 11;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private String inputString;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        countStepsToChildProcess();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString =br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void countStepsToChildProcess() {
        int x = 0, y = 0, z = 0; // qolumn, row, sideways (corresponding to cube grid)
        int absoluteValueSum = 0, highestReached = 0;
        for (String substring : inputString.split(",")) {
            switch (substring) {
                case "ne" -> { z++; x--; }
                case "nw" -> { x++; y--; }
                case "s" -> { y++; z--; }
                case "n" -> { y--; z++; }
                case "se" -> { x--; y++; }
                case "sw" -> { z--; x++; }
                default -> throw new IllegalArgumentException("Invalid direction string: "+ substring);
            }
            absoluteValueSum = Math.abs(x)+Math.abs(y)+Math.abs(z);
            if (absoluteValueSum > highestReached) highestReached = absoluteValueSum;
        }
        System.out.println("\nFinal x: "+ x +"; y: "+ y +"; z: "+ z);
        System.out.println("What Manhattan distance would be if these were cubes: "+absoluteValueSum);
        System.out.println("\nFewest steps needed to reach child process on hexagonal grid (part 1 answer): "+absoluteValueSum/2);
        System.out.println("Furthest Manhattan distance reached during travels (part 2 answer): "+highestReached/2);
    }

}
