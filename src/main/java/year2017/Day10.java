package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day10 {
    static private final int DAY = 10;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private String inputString;
    static private final int listLength = 256;
    static private final int[] list = new int[listLength];

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        initializeList();
        solvePart1();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void initializeList() {
        for (int i = 0; i < list.length; i++) list[i] = i;
    }

    private static void solvePart1() {
        int currentPosition = 0, skipSize = 0, rangeLength;
        int[] readValues;
        for (String lengthString : inputString.split(",")) {
            rangeLength = Integer.parseInt(lengthString);
            if (rangeLength > listLength) continue;
            readValues = new int[rangeLength];
            for (int i = 0; i < rangeLength; i++) { // march forward reading values
                readValues[i] = list[currentPosition];
                if (++currentPosition >= listLength) currentPosition -= listLength;
            }
            for (int i = 0; i < rangeLength; i++) { // march backward writing the values in the same order they were read
                if (--currentPosition < 0) currentPosition += listLength;
                list[currentPosition] = readValues[i];
            }
            currentPosition += rangeLength+(skipSize++);
            while (currentPosition >= listLength) currentPosition -= listLength;
        }
        System.out.println("\nProduct of first 2 values in list after a normal run of the hash function (part 1 answer): "+(list[0]*list[1]));
    }

}
