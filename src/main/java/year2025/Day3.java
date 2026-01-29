package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day3 {
    static private final int DAY = 3;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final ArrayList<char[]> inputChars = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputChars.add(s.toCharArray());
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solve() {
        System.out.println("\nPart 1 joltage: "+getTotalJoltage(2)+
                "\n\nPart 2 joltage: "+getTotalJoltage(12));
    }

    private static long getTotalJoltage(int neededDigitCount) {
        int length = inputChars.getFirst().length;
        if (neededDigitCount > length) throw new IllegalArgumentException("Requested digit count exceeds input string length.");
        long totalJoltage = 0;
        for (var bank : inputChars) {
            int lowestPossibleNextDigitIndex = 0, highestPossibleNextDigitIndex = length-neededDigitCount;
            var sb = new StringBuilder();
            iterationLoop: for (int iteration = 1; iteration <= neededDigitCount; iteration++) {
                for (char n = '9'; n > '0'; n--) {
                    for (int i = lowestPossibleNextDigitIndex; i <= highestPossibleNextDigitIndex; i++) {
                        if (bank[i] == n) {
                            sb.append(n);
                            lowestPossibleNextDigitIndex = i+1;
                            highestPossibleNextDigitIndex++;
                            continue iterationLoop;
                        }
                    }
                }
            }
            totalJoltage += Long.parseLong(sb.toString());
        }
        return totalJoltage;
    }

//    private static void solvePart1() { // The functional but much less nice way I originally solved part 1 (with full input strings):
//        int totalJoltage = 0, joltage = -1, lastIndex = inputStrings.getFirst().length()-1;
//        for (var s : inputStrings) {
//            int i1 = -1, i2 = -1; char n1 = 'x';
//            for (char n = '9'; n > '0'; n--) {
//                i1 = s.indexOf(n);
//                if (i1 != -1) { n1 = n; break; }
//            }
//            if (i1 == -1) throw new RuntimeException("This should never happen.");
//            else if (n1 == '1') joltage = 11;
//            else if (i1 == lastIndex) {
//                for (char n = (char)(n1-1); n > '0'; n--) {
//                    i2 = s.indexOf(n);
//                    if (i2 != -1) { joltage = Integer.parseInt(n+""+n1); break; }
//                }
//            } else {
//                i2 = s.lastIndexOf(n1);
//                if (i2 != i1) joltage = Integer.parseInt(n1+""+n1);
//                else for (char n = (char)(n1-1); n > '0'; n--) {
//                    i2 = s.indexOf(n, i1+1);
//                    if (i2 != -1) { joltage = Integer.parseInt(n1+""+n); break; }
//                }
//            }
//            if (i2 == -1) throw new RuntimeException("This should never happen.");
//            totalJoltage += joltage;
//        }
//        System.out.println("\nPart 1 joltage: "+totalJoltage);
//    }

}
