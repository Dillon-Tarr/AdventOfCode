package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

class Day4 {
    static private final int DAY = 4;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final ArrayList<String[]> substringArrays = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        countValidPassphrases();
        countNonAnagramPassphrases();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
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

    private static void countValidPassphrases() {
        int count = 0;
        mainLoop: for (int i = 0; i < inputStrings.size(); i++) {
            String s = inputStrings.get(i);
            String[] subs = s.split(" ");
            HashSet<String> seenSubs = new HashSet<>();
            for (String sub : subs) if (!seenSubs.add(sub)) {
                inputStrings.remove(i--);
                continue mainLoop;
            }
            count++;
            substringArrays.add(subs);
        }
        System.out.println("\nFinal count of valid passphrases for part 1: "+count);
    }

    private static void countNonAnagramPassphrases() {
        int count = 0, charIndex;
        String sub1, sub2;
        StringBuilder sub1SB, sub2SB;
        mainLoop: for (String[] subs : substringArrays) {
            for (int sub1Index = 0; sub1Index < subs.length-1; sub1Index++) { // :sub1Loop
                sub1 = subs[sub1Index];
                sub2Loop: for (int sub2Index = sub1Index+1; sub2Index < subs.length; sub2Index++) {
                    sub2 = subs[sub2Index];
                    if (sub1.length() != sub2.length()) continue;
                    sub1SB = new StringBuilder(sub1);
                    sub2SB = new StringBuilder(sub2);
                    while (!sub1SB.isEmpty()) { // :anagramCheckLoop
                        charIndex = sub2SB.indexOf(""+sub1SB.charAt(0));
                        if (charIndex == -1) continue sub2Loop;
                        sub1SB.deleteCharAt(0);
                        sub2SB.deleteCharAt(charIndex);
                    } // end of anagramCheckLoop
                    continue mainLoop; // because end of anagramCheckLoop means anagram found.
                } // end of sub2Loop
            } // end of sub1Loop
            count++; // reached if no anagrams found between any pair of substrings in passphrase
        } // end of mainLoop
        System.out.println("\nFinal count of valid passphrases for part 2: "+count);
    }

}
