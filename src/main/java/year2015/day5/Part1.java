package year2015.day5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part1 {
    static private final int DAY = 5;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");

    static private final ArrayList<String> strings = new ArrayList<>();
    static private int niceStringCount = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        testStrings();

        System.out.println("Nice string count: "+ niceStringCount);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
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
            if (checkIfFreeFromNaughtyStrings(s) && checkIfContainsThreeOrMoreVowelLetters(s) && checkIfContainsDoubleLetters(s)) {
                niceStringCount++;
            }
        }
    }

    private static boolean checkIfFreeFromNaughtyStrings(String s) {
        return !s.contains("ab") && !s.contains("cd") && !s.contains("pq") && !s.contains("xy");
    }

    private static boolean checkIfContainsThreeOrMoreVowelLetters(String s) {
        int vowelCounter = 0;
        for (int i = 0; i < s.length(); i++) {
            switch(s.charAt(i)) {
                case 'a', 'e', 'i', 'o', 'u' -> vowelCounter++;
            }
            if (vowelCounter == 3) return true;
        }
        return false;
    }

    private static boolean checkIfContainsDoubleLetters(String s) {
        char char1;
        char char2 = s.charAt(0);
        for (int i = 1; i < s.length(); i++) {
            char1 = char2;
            char2 = s.charAt(i);
            if (char1 == char2) return true;
        }
        return false;
    }

}
