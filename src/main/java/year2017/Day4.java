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

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        countValidPassphrases();

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

    private static void countValidPassphrases() {
        int count = 0;
        System.out.println();
        mainLoop: for (String s : inputStrings) {
            System.out.println("Checking string: "+s);
            String[] subs = s.split(" ");
            HashSet<String> seenSubs = new HashSet<>();
            for (String sub : subs) {
                if (!seenSubs.add(sub)) {
                    System.out.println("NOT valid. Repeating \"word\": " + sub);
                    continue mainLoop;
                }
            }
            System.out.println("Valid! Count: "+(++count));
        }
        System.out.println("\nFinal count of valid passphrases: "+count);
    }

}
