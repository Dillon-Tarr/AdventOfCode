package year2015.day12;

import shared.InclusiveNumberRange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Part2 {
    static private final int DAY = 12;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private String inputString;
    static private final ArrayList<InclusiveNumberRange> redObjectRanges = new ArrayList<>();
    static private boolean[] charIsRed;

    static private int sum = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputString();
        identifyRedObjects();
        markRedObjects();
        findAndSumNumbers();

        System.out.println("\nSum of all non-red numbers in the document: "+sum);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputString() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void identifyRedObjects() {
        int startCursor, redPropertyValueIndex, endCursor = 0, precedingOpeningCurlyCount, precedingClosingCurlyCount,
            followingOpeningCurlyCount, followingClosingCurlyCount;
        while (true) {
            redPropertyValueIndex = inputString.indexOf(":\"red\"", endCursor);
            if (redPropertyValueIndex == -1) break;

            startCursor = redPropertyValueIndex;
            precedingOpeningCurlyCount = 0; precedingClosingCurlyCount = 0;
            endCursor = redPropertyValueIndex;
            followingClosingCurlyCount = 0; followingOpeningCurlyCount = 0;
            while (precedingOpeningCurlyCount <= precedingClosingCurlyCount) {
                startCursor--;
                switch (inputString.charAt(startCursor)) {
                    case '{' -> precedingOpeningCurlyCount++;
                    case '}' -> precedingClosingCurlyCount++;
                }
            }
            while (followingClosingCurlyCount <= followingOpeningCurlyCount) {
                endCursor++;
                switch (inputString.charAt(endCursor)) {
                    case '{' -> followingOpeningCurlyCount++;
                    case '}' -> followingClosingCurlyCount++;
                }
            }
            redObjectRanges.add(new InclusiveNumberRange(startCursor, endCursor));
        }
        InclusiveNumberRange.mergeOverlappingRanges(redObjectRanges);
    }

    private static void markRedObjects() {
        charIsRed = new boolean[inputString.length()];
        int i;
        for (InclusiveNumberRange range : redObjectRanges) {
            i = range.rangeStart();
            while (i <= range.inclusiveRangeEnd()) charIsRed[i++] = true;
        }
    }

    private static void findAndSumNumbers() {
        boolean numberMode = false;
        int currentNumberStartIndex = Integer.MIN_VALUE;
        for (int i = 0; i < inputString.length(); i++) {
            if (charIsRed[i]) continue;
            if (numberMode) {
                switch (inputString.charAt(i)) {
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {}
                    default -> {
                        numberMode = false;
                        sum += Integer.parseInt(inputString.substring(currentNumberStartIndex, i));
                    }
                }
            } else {
                switch (inputString.charAt(i)) {
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                        numberMode = true;
                        currentNumberStartIndex = inputString.charAt(i-1) == '-' ? i-1 : i;
                    }
                }
            }
        }
    }

}
