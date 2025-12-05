package year2025;

import shared.LongInclusiveNumberRange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day5 {
    static private final int DAY = 5;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final ArrayList<LongInclusiveNumberRange> freshRanges = new ArrayList<>();
    static private final ArrayList<Long> ids = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        ArrayList<String> rangeStrings = new ArrayList<>();
        ArrayList<String> idStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                if (s.isEmpty()) break;
                rangeStrings.add(s);
                s = br.readLine();
            }
            s = br.readLine();
            while (s != null) {
                idStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (var s : rangeStrings) {
            var split = s.split("-");
            freshRanges.add(new LongInclusiveNumberRange(Long.parseLong(split[0]), Long.parseLong(split[1])));
        }
        LongInclusiveNumberRange.mergeOverlappingRanges(freshRanges);
        for (var s : idStrings) ids.add(Long.parseLong(s));
    }

    private static void solve() {
        int count = 0;
        for (var id : ids) for (var range : freshRanges) if (range.inRange(id)) { count++; break; }
        System.out.println("\nNumber of available fresh ingredients (part 1 answer): "+count);
        long combinedSizeOfRanges = 0;
        for (var range : freshRanges) combinedSizeOfRanges += range.size;
        System.out.println("\nCount of all IDs considered fresh (part 2 answer): "+combinedSizeOfRanges);
    }

}
