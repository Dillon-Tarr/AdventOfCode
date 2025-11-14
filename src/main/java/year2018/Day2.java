package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Day2 {
    static private final int DAY = 2;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solvePart1() {
        int twoCount = 0, threeCount = 0;
        boolean foundATwo, foundAThree;
        HashMap<Character, Integer> counts;
        for (String s : inputStrings) {
            counts = new HashMap<>();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                counts.put(c, counts.getOrDefault(c, 0) + 1);
            }
            foundATwo = false;
            foundAThree = false;
            for (int count : counts.values()) {
                switch (count) {
                    case 2 -> foundATwo = true;
                    case 3 -> foundAThree = true;
                }
            }
            if (foundATwo) twoCount++;
            if (foundAThree) threeCount++;
        }
        System.out.println("\nChecksum (part 1 answer): " + twoCount * threeCount);

    }

    private static void solvePart2() {
        String iString = "", jString = ""; boolean mismatchFound, answerFound = false; int length = inputStrings.getFirst().length();
        iLoop: for (int i = 0; i < inputStrings.size()-1; i++) { iString = inputStrings.get(i);
            jLoop: for (int j = i+1; j < inputStrings.size(); j++) { jString = inputStrings.get(j);
                mismatchFound = false;
                for (int c = 0; c < length; c++) {
                    if (iString.charAt(c) != jString.charAt(c)) {
                        if (mismatchFound) continue jLoop;
                        mismatchFound = true;
                    }
                }
                if (mismatchFound) {
                    answerFound = true;
                    break iLoop;
                }
            }
        }
        if (!answerFound) throw new RuntimeException("Something quite wrong happened.");
        String answerString = null;
        for (int i = 0; i < length; i++) {
            if (iString.charAt(i) != jString.charAt(i)) {
                if (i == length-1) answerString = iString.substring(0, i);
                else answerString = iString.substring(0, i)+iString.substring(i+1);
            }
        }
        if (answerString == null) throw new RuntimeException("Something quite wrong happened.");
        System.out.println("\nCommon letters: "+answerString);
    }

}
