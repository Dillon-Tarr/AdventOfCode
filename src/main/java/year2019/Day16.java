package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day16 {
    static private final int DAY = 16;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private String inputString;
    static private int length;
    static private final int[] BASE_PATTERN = new int[]{0, 1, 0, -1};
    static private int[][] patterns;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        createPatterns();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
        length = inputString.length();
    }

    private static void createPatterns() {
        patterns = new int[length][length]; // 1 int[] pattern for each output index along the length
        for (int o = 0; o < length; o++) { // o: outputIndex
            int r = o+1; // r: # of times to repeat each BASE_PATTERN value before moving onto the next one
            int c = 0; // s: # of completed repeats on current BASE_PATTERN index (effectively starts at 1 to skip first value)
            int b = 0; // b: BASE_PATTERN index
            for (int i = 0; i < length; i++) { // i: inputIndex
                if (++c >= r) {
                    c = 0;
                    if (++b > 3) b = 0;
                }
                patterns[o][i] = BASE_PATTERN[b];
            }
        }
    }

    private static void solve() {
        String s = inputString;
        for (int i = 0; i < 100; i++) s = runPhaseOfFFT(s);
        System.out.println("First 8 digits of s after 100 phases of FFT (part 1 answer): "+s.substring(0, 8));
    }

    private static String runPhaseOfFFT(String input) {
        int[] inVals = new int[length];
        for (int i = 0; i < length; i++) inVals[i] = Character.getNumericValue(input.charAt(i));
        var output = new StringBuilder();
        for (int o = 0; o < length; o++) {
            int[] pattern = patterns[o];
            int n = 0;
            for (int i = 0; i < length; i++) n += pattern[i] * inVals[i];
            var nString = String.valueOf(n);
            output.append(nString.charAt(nString.length()-1));
        }
        return output.toString();
    }

}
