package year2025;

import shared.LongInclusiveNumberRange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

class Day2 {
    static private final int DAY = 2;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final ArrayList<LongInclusiveNumberRange> ranges = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        String[] rangeStrings;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            rangeStrings = br.readLine().split(",");
        } catch (IOException e) {throw new RuntimeException(e);}
        for (var pair : rangeStrings) {
            String[] splitPair = pair.split("-");
            ranges.add(new LongInclusiveNumberRange(Long.parseLong(splitPair[0]), Long.parseLong(splitPair[1])));
        }
        LongInclusiveNumberRange.mergeOverlappingRanges(ranges);
    }

    private static void solve() {
        long sum1 = 0, sum2 = 0;
        Pattern part2Pattern = Pattern.compile("^(.+)\\1+$");
        for (var range : ranges) {
            for (long i = range.rangeStart; i <= range.inclusiveRangeEnd; i++) {
                String s = ""+i; int length = s.length();
                if (part2Pattern.matcher(s).matches()) {
                    sum2 += i; // Faster/simpler than regex for part 1 sum, and only possible if part 2 pattern is true:
                    if (length % 2 == 0 && s.substring(0, length/2).equals(s.substring(length/2))) sum1 += i;
                }
            }
        }
        System.out.println("\nPart 1 sum (of numbers matching \"^(.+)\\1$\"): "+sum1);
        System.out.println("\nPart 2 sum (of numbers matching \"^(.+)\\1+$\"): "+sum2);
    }

}
