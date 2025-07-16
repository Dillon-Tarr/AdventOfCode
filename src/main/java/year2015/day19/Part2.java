package year2015.day19;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Part2 { // This program finds the solution for my input, but is not necessarily a general solution.
    static private final int DAY = 19;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private String medicineMoleculeString;
    static private final List<ReplacementPair> reversedReplacementPairs = new ArrayList<>();
    static private final Queue<QueuedReplacement> queue = new PriorityQueue<>();
    static private final Set<String> visitedStrings = new HashSet<>();
    static private int fewestStepsToReachE = Integer.MAX_VALUE;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        queueReplacements(medicineMoleculeString, 0);
        processReplacementsUntilE();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine(), original, replacement;
            int firstSpaceIndex, lastSpaceIndex;
            while (!inputString.isEmpty()) {
                firstSpaceIndex = inputString.indexOf(' ');
                replacement = inputString.substring(0, firstSpaceIndex);
                lastSpaceIndex = inputString.lastIndexOf(' ');
                original = inputString.substring(lastSpaceIndex+1);
                reversedReplacementPairs.add(new ReplacementPair(original, replacement));

                inputString = br.readLine();
            }
            medicineMoleculeString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void queueReplacements(String s, int completedReplacements) {
        String original, replacement;
        int nextInstanceIndex, replacementEndIndex;
        for (ReplacementPair pair : reversedReplacementPairs) {
            original = pair.original();
            replacement = pair.replacement();
            nextInstanceIndex = s.indexOf(original);
            while (nextInstanceIndex != -1) {
                replacementEndIndex = nextInstanceIndex+original.length()-1;
                queue.add(new QueuedReplacement(s, replacement, nextInstanceIndex, replacementEndIndex, completedReplacements));
                if (nextInstanceIndex == s.length()-1) break;
                nextInstanceIndex = s.indexOf(original, nextInstanceIndex+1);
            }
        }
    }

    private static void processReplacementsUntilE(){
        QueuedReplacement queuedReplacement;
        String startingString, replacementString, newString;
        int replacementStartIndex, replacementEndIndex, completedReplacements;
        while (!queue.isEmpty()) {
            queuedReplacement = queue.poll();
            startingString = queuedReplacement.startingString();
            replacementString = queuedReplacement.replacementString();
            replacementStartIndex = queuedReplacement.replacementStartIndex();
            replacementEndIndex = queuedReplacement.replacementEndIndex();
            completedReplacements = 1+queuedReplacement.completedReplacements();
            newString = replaceSubstring(startingString, replacementString, replacementStartIndex, replacementEndIndex);
            if (newString.equals("e") && completedReplacements < fewestStepsToReachE) {
                fewestStepsToReachE = completedReplacements;
                continue;
            }
            if (!visitedStrings.add(newString)) continue;
            if (visitedStrings.size() >= 20000000 && fewestStepsToReachE != Integer.MAX_VALUE) {
                System.out.println("\nFewest steps encountered to go from \"e\" to medicine molecule (or vice-versa) upon encountering 20 million unique strings: "+fewestStepsToReachE);
                System.out.println("In the case of my input, this answer was the correct answer. More memory or a different method would be required to obtain");
                System.out.println("certainty of this answer being correct without checking by entering the answer at \"https://adventofcode.com/2015/day/19\".");
                System.out.println("The answer happens to be the same if not even checking visited strings, in my case. That also executes much faster...");
                return;
            }
            queueReplacements(newString, completedReplacements);
        }
        System.out.println("\nFewest possible steps to go from \"e\" to medicine molecule (or vice-versa): "+fewestStepsToReachE);
    }

    private static String replaceSubstring(String startingString, String replacementString, int replacementStartIndex, int replacementEndIndex){
        if (replacementEndIndex == startingString.length()-1) {
            String pre = startingString.substring(0, replacementStartIndex);
            return pre+replacementString;
        } else {
            String post = startingString.substring(replacementEndIndex+1);
            if (replacementStartIndex == 0) return replacementString+post;
            String pre = startingString.substring(0, replacementStartIndex);
            return pre+replacementString+post;
        }
    }

}
