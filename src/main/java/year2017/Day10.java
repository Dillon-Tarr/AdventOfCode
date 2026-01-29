package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static shared.BitwiseOperations.xor;

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
        initializeList();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
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

    private static void solvePart2() {
        if (listLength != 256) { System.out.println("\nPart 2 solve only works with list length of 256. listLength: "+listLength); return; }
        int[] rangeLengths = getPart2RangeLengths(), readValues;
        int currentPosition = 0, skipSize = 0;
        for (int round = 1; round <= 64; round++) for (int rangeLength : rangeLengths) {
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
            if (skipSize >= listLength) skipSize -= listLength;
            while (currentPosition >= listLength) currentPosition -= listLength;
        }
        int[] denseHash = new int[16]; char xorResult;
        for (int d = 0; d < 16; d++) {
            xorResult = 0;
            for (int i = 0; i < 16; i++) xorResult = xor(xorResult, (char) list[(d*16)+i]);
            denseHash[d] = xorResult;
        }
        System.out.print("\nKnot Hash (part 2 answer): ");
        for (int i = 0; i < denseHash.length; i++) System.out.printf("%02x", denseHash[i]);
        System.out.println();
    }

    private static int[] getPart2RangeLengths() {
        int inputLength = inputString.length();
        int[] rangeLengths = new int[inputLength+5];
        for (int i = 0; i < inputLength; i++) {
            int asciiValue = inputString.charAt(i);
            if (asciiValue > 255) throw new RuntimeException("There is a non-digit, non-comma character in input string: "+inputString.charAt(i));
            rangeLengths[i] = asciiValue;
        }
        rangeLengths[inputLength] = 17;
        rangeLengths[inputLength+1] = 31;
        rangeLengths[inputLength+2] = 73;
        rangeLengths[inputLength+3] = 47;
        rangeLengths[inputLength+4] = 23;
        return rangeLengths;
    }

}
