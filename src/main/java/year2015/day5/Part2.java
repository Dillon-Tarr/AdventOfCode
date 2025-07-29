package year2015.day5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part2 {
    static private final int DAY = 5;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");

    static private final ArrayList<String> strings = new ArrayList<>();
    static private int niceStringCount = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        testStrings();

        System.out.println("Nice string count: "+ niceStringCount);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                strings.add(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void testStrings() {
        String s;
        for (int i = 0; i < strings.size(); i++) {
            s = strings.get(i);
            if (checkIfContainsDoubleLettersWithOneCharGap(s) && checkIfContainsRepeatingPairWithTwoIndexDistance(s)) {
                niceStringCount++;
            }
        }
    }

    private static boolean checkIfContainsDoubleLettersWithOneCharGap(String s) {
        char char1;
        char char2 = s.charAt(0);
        char char3 = s.charAt(1);
        for (int i = 2; i < s.length(); i++) {
            char1 = char2;
            char2 = char3;
            char3 = s.charAt(i);
            if (char1 == char3) return true;
        }
        return false;
    }

    private static boolean checkIfContainsRepeatingPairWithTwoIndexDistance(String s) {
        for (int i = 0; i < s.length()-3; i++) {
            if (s.lastIndexOf(s.substring(i, i+2)) >= i+2) return true;
        }
        return false;
    }

}
