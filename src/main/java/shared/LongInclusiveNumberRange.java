package shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LongInclusiveNumberRange {
    public long rangeStart, inclusiveRangeEnd, size;

    public LongInclusiveNumberRange(long n1, long n2) {
        if (n1 < n2) { rangeStart = n1; inclusiveRangeEnd = n2; }
        else { rangeStart = n2; inclusiveRangeEnd = n1; }
        size = 1+inclusiveRangeEnd-rangeStart;
    }

    public static void mergeOverlappingRanges(ArrayList<LongInclusiveNumberRange> ranges) {
        sortRangesByRangeStartLowToHigh(ranges);
        for (int i = 0; i < ranges.size() - 1; i++) {
            var lowerRange = ranges.get(i);
            var higherRange = ranges.get(i + 1);
            if (lowerRange.inclusiveRangeEnd >= higherRange.rangeStart - 1) {
                ranges.set(i + 1, new LongInclusiveNumberRange(lowerRange.rangeStart,
                        Math.max(higherRange.inclusiveRangeEnd, lowerRange.inclusiveRangeEnd)));
                ranges.set(i, null);
            }
        }
        ranges.removeAll(Collections.singleton(null));
    }

    public static void sortRangesByRangeStartLowToHigh(ArrayList<LongInclusiveNumberRange> ranges) {
        ranges.sort(Comparator.comparing(range -> range.rangeStart));
    }

    public boolean inRange(long n) { return n >= rangeStart && n <= inclusiveRangeEnd; }

    @Override
    public String toString() {
        return "\nrangeStart: " + rangeStart + "\ninclusiveRangeEnd: " + inclusiveRangeEnd;
    }

}
