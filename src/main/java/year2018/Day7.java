package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

class Day7 {
    static private final int DAY = 7;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final HashSet<Character> incompleteSteps = new HashSet<>();
    static private final PriorityQueue<Character> readyStepChars = new PriorityQueue<>();
    static private final HashMap<Character, ArrayList<Character>> gatedCharToKeyCharsMap = new HashMap<>();
    static private final HashMap<Character, ArrayList<Character>> keyCharToGatedCharsMap = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        processInputData();
        solve();

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

    private static void processInputData() {
        char keyChar, gatedChar;
        ArrayList<Character> gatedChars, keyChars;
        for (String s : inputStrings) {
            keyChar = s.charAt(5); incompleteSteps.add(keyChar);
            gatedChar = s.charAt(36); incompleteSteps.add(gatedChar);
            if (keyCharToGatedCharsMap.containsKey(keyChar)) {
                gatedChars = keyCharToGatedCharsMap.get(keyChar);
                gatedChars.add(gatedChar);
            } else {
                gatedChars = new ArrayList<>();
                gatedChars.add(gatedChar);
                keyCharToGatedCharsMap.put(keyChar, gatedChars);
            }
            if (gatedCharToKeyCharsMap.containsKey(gatedChar)) {
                keyChars = gatedCharToKeyCharsMap.get(gatedChar);
                keyChars.add(keyChar);
            } else {
                keyChars = new ArrayList<>();
                keyChars.add(keyChar);
                gatedCharToKeyCharsMap.put(gatedChar, keyChars);
            }
        }
        for (var ch : incompleteSteps) if (!gatedCharToKeyCharsMap.containsKey(ch)) readyStepChars.add(ch);
    }

    private static void solve() {
        var sb = new StringBuilder();
        Character currentStep;
        ArrayList<Character> gatedChars, keyChars;
        while (!incompleteSteps.isEmpty()) {
            currentStep = readyStepChars.poll();
            if (currentStep == null) throw new RuntimeException("Something went horribly wrong.");
            incompleteSteps.remove(currentStep);
            sb.append(currentStep);
            gatedChars = keyCharToGatedCharsMap.remove(currentStep);
            if (gatedChars == null) continue;
            for (var ch : gatedChars) {
                keyChars = gatedCharToKeyCharsMap.get(ch);
                keyChars.remove(currentStep);
                if (keyChars.isEmpty()) {
                    gatedCharToKeyCharsMap.remove(ch);
                    readyStepChars.add(ch);
                }
            }
        }
        System.out.println("\nProper order of steps (part 1 answer): "+sb);
    }

}
