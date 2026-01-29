package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day15 {
    static private final int DAY = 15;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private long initialA, initialB;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        countMatchingLowestSixteenBits();
        countWithNewRules();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            initialA = Long.parseLong(s.substring(s.lastIndexOf(' ')+1));
            s = br.readLine();
            initialB = Long.parseLong(s.substring(s.lastIndexOf(' ')+1));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void countMatchingLowestSixteenBits() {
        int matchCount = 0;
        long a = initialA, b = initialB;
        for (int i = 0; i < 40_000_000; i++) {
            a = (a*16807) % 2147483647;
            b = (b*48271) % 2147483647;
            if ((char) a == (char) b) matchCount++; // char cast conveniently keeps the lowest 16 bits in unsigned form
        }
        System.out.println("\nCount of lowest 16 bit matches in 40,000,000 value pairs (part 1 answer): "+matchCount);
    }

    private static void countWithNewRules() {
        long a = initialA, b = initialB;
        long[] aVals = new long[5_000_000], bVals = new long[5_000_000];
        int acceptableValueCount = 0;
        while (acceptableValueCount < 5_000_000) {
            a = (a*16807) % 2147483647;
            if (a % 4 == 0) aVals[acceptableValueCount++] = a;
        }
        acceptableValueCount = 0;
        while (acceptableValueCount < 5_000_000) {
            b = (b*48271) % 2147483647;
            if (b % 8 == 0) bVals[acceptableValueCount++] = b;
        }
        int matchCount = 0;
        for (int i = 0; i < 5_000_000; i++) if ((char) aVals[i] == (char) bVals[i]) matchCount++;
        System.out.println("\nNumber of matches in 5,000,000 pairs given new rules (part 2 answer): "+matchCount);
    }

}
