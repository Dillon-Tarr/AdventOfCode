package shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public record LongInclusiveNumberRange(long rangeStart, long inclusiveRangeEnd) {

    public static void mergeOverlappingRanges(ArrayList<LongInclusiveNumberRange> ranges) {
        sortRangesByRangeStartLowToHigh(ranges);
        for (int i = 0; i < ranges.size() - 1; i++) {
            LongInclusiveNumberRange lowerRange = ranges.get(i);
            LongInclusiveNumberRange higherRange = ranges.get(i + 1);
            if (lowerRange.inclusiveRangeEnd >= higherRange.rangeStart - 1) {
                ranges.set(i + 1, new LongInclusiveNumberRange(lowerRange.rangeStart,
                        Math.max(higherRange.inclusiveRangeEnd(), lowerRange.inclusiveRangeEnd())));
                ranges.set(i, null);
            }
        }
        ranges.removeAll(Collections.singleton(null));
    }

    public static void sortRangesByRangeStartLowToHigh(ArrayList<LongInclusiveNumberRange> ranges) {
        ranges.sort(Comparator.comparing(LongInclusiveNumberRange::rangeStart));
    }

    @Override
    public String toString() {
        return "\nrangeStart: " + rangeStart + "\ninclusiveRangeEnd: " + inclusiveRangeEnd;
    }

}
