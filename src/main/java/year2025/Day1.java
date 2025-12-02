package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day1 {
    static private final int DAY = 1;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
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
        int position = 50, zeroCount = 0;
        for (String s : inputStrings) {
            int n = Integer.parseInt(s.substring(1));
            if (s.charAt(0) == 'L') {
                position -= n;
                while (position < 0) position += 100;
            }
            else {
                position += n;
                while (position > 99) position -= 100;
            }
            if (position == 0) zeroCount++;
        }
        System.out.println("\nPart 1 count: "+zeroCount);

        position = 50; zeroCount = 0;
        for (String s : inputStrings) {
            int n = Integer.parseInt(s.substring(1));
            if (s.charAt(0) == 'L') {
                while (--n >= 0) {
                    if (--position == 0) zeroCount++;
                    else if (position < 0) position += 100;
                }
            }
            else {
                while (--n >= 0) {
                    if (++position > 99) {
                        position = 0;
                        zeroCount++;
                    }
                }
            }
        }
        System.out.println("\nPart 2 count: "+zeroCount);
    }

}
