package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

class Day1 {
    static private final int DAY = 1;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
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

    private static void solve() {
        var ints = new int[inputStrings.size()];
        for (int i = 0; i < inputStrings.size(); i++) ints[i] = Integer.parseInt(inputStrings.get(i));
        int frequency = 0;
        Integer firstReachedTwice = null;
        HashSet<Integer> reachedFrequencies = new HashSet<>();
        for (int n : ints) {
            frequency += n;
            if (firstReachedTwice == null && !reachedFrequencies.add(frequency)) firstReachedTwice = frequency;
        }
        System.out.println("\nFrequency after one set of frequency changes (part 1 answer): "+frequency);
        if (firstReachedTwice == null) mainLoop: while (true) {
            for (int n : ints) {
                frequency += n;
                if (!reachedFrequencies.add(frequency)) {
                    firstReachedTwice = frequency;
                    break mainLoop;
                }
            }
        }
        System.out.println("\nFirst frequency reached twice (part 2 answer): "+firstReachedTwice);
    }

}
