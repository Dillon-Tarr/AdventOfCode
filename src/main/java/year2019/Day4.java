package year2019;

import java.io.*;

class Day4 {
    static private final int DAY = 4;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private int rangeStart, rangeEnd;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var ss = br.readLine().split("-");
            rangeStart = Integer.parseInt(ss[0]); rangeEnd = Integer.parseInt(ss[1]);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solve() {
        int count1 = 0, count2 = 0;
        int n = rangeStart;
        nLoop: while (n <= rangeEnd) {
            boolean foundPair = false, lastCheckWasPair = false, foundNonTrioPair = false;
            var ca = String.valueOf(n).toCharArray(); char c1, c2 = ca[0];
            for (int i = 1; i < 6; i++) {
                c1 = c2; c2 = ca[i];
                if (c1 > c2) { n += (c1-c2)*Math.powExact(10, -(i-5)); continue nLoop; }
                if (c1 == c2) {
                    if (!foundNonTrioPair && !lastCheckWasPair && (i == 5 || c2 != ca[i+1])) foundNonTrioPair = true;
                    lastCheckWasPair = foundPair = true;
                } else lastCheckWasPair = false;
            }
            if (foundPair) {
                count1++;
                if (foundNonTrioPair) count2++;
            }
            n++;
        }
        System.out.println("\nCount 1: " + count1+"\n\nCount 2: " + count2);
    }

}
