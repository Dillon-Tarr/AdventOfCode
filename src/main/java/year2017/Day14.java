package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static shared.BitwiseOperations.xor;
import static shared.StringMethods.hexToBinary;

class Day14 {
    static private final int DAY = 14;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private String inputString;
    static private final boolean[][] needGroupedBits = new boolean[128][];

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        countUsedSquares();
        countSetBitCoordinatesGroups();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void countUsedSquares() {
        int usedSquareCount = 0;
        for (int i = 0; i < 128; i++) {
            boolean[] row = new boolean[128]; needGroupedBits[i] = row;
            String binaryString = hexToBinary(getKnotHash(inputString+'-'+i));
            for (int j = 0; j < 128; j++) if (binaryString.charAt(j) == '1') {
                usedSquareCount++;
                row[j] = true;
            }
        }
        System.out.println("\nUsed square count (part 1 answer): "+usedSquareCount);
    }

    private static String getKnotHash(String input) {
        final int listLength = 256;
        final int[] list = new int[listLength];
        for (int i = 0; i < listLength; i++) list[i] = i;
        int[] rangeLengths = getRangeLengths(input), readValues;
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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < denseHash.length; i++) sb.append(String.format("%02x", denseHash[i]));
        return sb.toString();
    }

    private static int[] getRangeLengths(String s) {
        int inputLength = s.length();
        int[] rangeLengths = new int[inputLength+5];
        for (int i = 0; i < inputLength; i++) {
            int asciiValue = s.charAt(i);
            if (asciiValue > 255) throw new RuntimeException("There is an out-of-range (0-255) character in input string: "+s.charAt(i));
            rangeLengths[i] = asciiValue;
        }
        rangeLengths[inputLength] = 17;
        rangeLengths[inputLength+1] = 31;
        rangeLengths[inputLength+2] = 73;
        rangeLengths[inputLength+3] = 47;
        rangeLengths[inputLength+4] = 23;
        return rangeLengths;
    }

    private static void countSetBitCoordinatesGroups() {
        int usedSquareRegionCount = 0;
        for (int y = 0; y < 128; y++) {
            boolean[] row = needGroupedBits[y];
            for (int x = 0; x < 128; x++) {
                if (row[x]) {
                    usedSquareRegionCount++;
                    groupContiguousBits(y, x);
                }
            }
        }
        System.out.println("\nNumber of used-square contiguous regions (part 2 answer): "+ usedSquareRegionCount);
    }

    private static void groupContiguousBits(int y, int x) {
        needGroupedBits[y][x] = false;
        int otherY = y-1, otherX = x-1;
        if (y > 0 && needGroupedBits[otherY][x]) groupContiguousBits(otherY, x);
        if (y < 127 && needGroupedBits[otherY+=2][x]) groupContiguousBits(otherY, x);
        if (x > 0 && needGroupedBits[y][otherX]) groupContiguousBits(y, otherX);
        if (x < 127 && needGroupedBits[y][otherX+=2]) groupContiguousBits(y, otherX);
    }

}
