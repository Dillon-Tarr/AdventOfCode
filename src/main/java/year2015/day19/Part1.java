package year2015.day19;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Part1 {
    static private final int DAY = 19;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private String medicineMoleculeString;
    static private final List<ReplacementPair> replacementPairs = new ArrayList<>();
    static private final Set<String> newDistinctMolecules = new HashSet<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        calibrate();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine(), original, replacement;
            int firstSpaceIndex, lastSpaceIndex;
            while (!inputString.isEmpty()) {
                firstSpaceIndex = inputString.indexOf(' ');
                original = inputString.substring(0, firstSpaceIndex);
                lastSpaceIndex = inputString.lastIndexOf(' ');
                replacement = inputString.substring(lastSpaceIndex+1);
                replacementPairs.add(new ReplacementPair(original, replacement));

                inputString = br.readLine();
            }
            medicineMoleculeString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void calibrate(){
        String pre, original, replacement, post;
        int originalLength, nextInstanceIndex;
        for (ReplacementPair pair : replacementPairs) {
            original = pair.original();
            replacement = pair.replacement();
            originalLength = original.length();
            nextInstanceIndex = medicineMoleculeString.indexOf(original);
            while (nextInstanceIndex != -1) {
                if (nextInstanceIndex == medicineMoleculeString.length()- originalLength) {
                    pre = medicineMoleculeString.substring(0, nextInstanceIndex);
                    newDistinctMolecules.add(pre+replacement);
                    break;
                }
                if (nextInstanceIndex == 0)  {
                    post = medicineMoleculeString.substring(originalLength);
                    newDistinctMolecules.add(replacement+post);
                } else {
                    pre = medicineMoleculeString.substring(0, nextInstanceIndex);
                    post = medicineMoleculeString.substring(nextInstanceIndex+ originalLength);
                    newDistinctMolecules.add(pre+replacement+post);
                }
                nextInstanceIndex = medicineMoleculeString.indexOf(original, nextInstanceIndex+1);
            }
        }
        System.out.println("\nNumber of distinct molecules that can be made with exactly one replacement: "+newDistinctMolecules.size());
    }

}
