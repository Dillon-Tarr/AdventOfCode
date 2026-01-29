package year2016;

import shared.LongInclusiveNumberRange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day20 {
    static private final int DAY = 20;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final ArrayList<LongInclusiveNumberRange> ranges = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        createMergeAndSortRanges();
        getLowestValidIPAddress();
        countAllowedIPAddresses();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
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

    private static void createMergeAndSortRanges() {
        int hyphenIndex;
        for (String s : inputStrings) {
            hyphenIndex = s.indexOf('-');
            ranges.add(new LongInclusiveNumberRange(Long.parseLong(s.substring(0, hyphenIndex)), Long.parseLong(s.substring(1+hyphenIndex))));
        }
        System.out.println("\nNumber of ranges before merging: "+ranges.size());
        LongInclusiveNumberRange.mergeOverlappingRanges(ranges);
        System.out.println("\nRanges after sorting and merging ("+ranges.size()+"):"+ranges);
    }

    private static void getLowestValidIPAddress() {
        long address;
        LongInclusiveNumberRange range = ranges.getFirst();
        if (range.rangeStart == 0) address = range.inclusiveRangeEnd+1;
        else address = 0;
        System.out.println("\nLowest-valued allowed IP address (part 1 answer): "+ address);
    }

    private static void countAllowedIPAddresses() {
        long count = 0;
        LongInclusiveNumberRange range1, range2 = ranges.getFirst();
        count += range2.rangeStart;
        for (int i = 1; i < ranges.size(); i++) {
            range1 = range2;
            range2 = ranges.get(i);
            count += range2.rangeStart-range1.inclusiveRangeEnd-1;
        }
        count += 4294967295L-range2.inclusiveRangeEnd;
        System.out.println("\nNumber of IP addresses allowed by the blacklist (part 2 answer): "+count);
    }

}
