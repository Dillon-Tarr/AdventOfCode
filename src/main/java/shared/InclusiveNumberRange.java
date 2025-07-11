package shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public record InclusiveNumberRange(int rangeStart, int inclusiveRangeEnd) {

    public static void mergeOverlappingRanges(ArrayList<InclusiveNumberRange> ranges) {
        sortRangesByRangeStartLowToHigh(ranges);
        for (int i = 0; i < ranges.size() - 1; i++) {
            InclusiveNumberRange lowerRange = ranges.get(i);
            InclusiveNumberRange higherRange = ranges.get(i + 1);
            if (lowerRange.inclusiveRangeEnd >= higherRange.rangeStart - 1) {
                ranges.set(i + 1, new InclusiveNumberRange(lowerRange.rangeStart,
                        Math.max(higherRange.inclusiveRangeEnd(), lowerRange.inclusiveRangeEnd())));
                ranges.set(i, null);
            }
        }
        ranges.removeAll(Collections.singleton(null));
    }

    public static void sortRangesByRangeStartLowToHigh(ArrayList<InclusiveNumberRange> ranges) {
        ranges.sort(Comparator.comparing(InclusiveNumberRange::rangeStart));
    }

    @Override
    public String toString() {
        return "\nrangeStart: " + rangeStart + "\ninclusiveRangeEnd: " + inclusiveRangeEnd;
    }

}
