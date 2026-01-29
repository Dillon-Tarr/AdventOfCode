package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day9 {
    static private final int DAY = 9;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private StringBuilder stream;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        removeGarbage();
        scoreStream();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            stream = new StringBuilder(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void removeGarbage() {
        int i = 0, countOfNonCanceledCharsWithinGarbage = 0;
        boolean garbageMode = false;
        System.out.println("\nStream before garbage removal: "+stream);
        while (i < stream.length()) {
            if (garbageMode) {
                switch (stream.charAt(i)) {
                    case '>' -> {
                        stream.deleteCharAt(i);
                        garbageMode = false;
                    }
                    case '!' -> stream.delete(i, i+2);
                    default -> {
                        countOfNonCanceledCharsWithinGarbage++;
                        stream.deleteCharAt(i);
                    }
                }
            } else { // !garbageMode
                if (stream.charAt(i) == '<') {
                    stream.deleteCharAt(i);
                    garbageMode = true;
                } else i++;
            }
        }
        System.out.println("\nAfter garbage removal: "+stream);
        System.out.println("\nCount of non-canceled characters within garbage (part 2 answer): "+ countOfNonCanceledCharsWithinGarbage);
    }

    private static void scoreStream() {
        int depth = 0, totalScore = 0;
        for (int i = 0; i < stream.length(); i++) {
            switch (stream.charAt(i)) {
                case '{' -> totalScore+=(++depth);
                case '}' -> --depth;
            }
        }
        System.out.println("\nTotal score (part 1 answer): "+totalScore);
    }

}
