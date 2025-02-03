package day5.part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public final class InclusiveNumberRange {
    private final long rangeStart, inclusiveRangeEnd;

    InclusiveNumberRange(long rangeStart, long inclusiveRangeEnd) {
        this.rangeStart = rangeStart;
        this.inclusiveRangeEnd = inclusiveRangeEnd;
    }

    public static void mergeOverlappingRanges(ArrayList<InclusiveNumberRange> ranges) {
        sortRangesByRangeStartLowToHigh(ranges);
        for (int i = 0; i < ranges.size()-1; i++) {
            InclusiveNumberRange lowerRange = ranges.get(i);
            InclusiveNumberRange higherRange = ranges.get(i+1);
            if (lowerRange.inclusiveRangeEnd >= higherRange.rangeStart-1){
                ranges.set(i+1, new InclusiveNumberRange(lowerRange.rangeStart,
                        Math.max(higherRange.getInclusiveRangeEnd(), lowerRange.getInclusiveRangeEnd())));
                ranges.set(i, null);
            }
        }
        ranges.removeAll(Collections.singleton(null));
    }

    public long getRangeStart() {return rangeStart;}
    public long getInclusiveRangeEnd() {return inclusiveRangeEnd;}

    public static void sortRangesByRangeStartLowToHigh(ArrayList<InclusiveNumberRange> ranges) {
        ranges.sort(Comparator.comparing(InclusiveNumberRange::getRangeStart));
    }

    @Override
    public String toString(){
        return "\nrangeStart: " +rangeStart+"\ninclusiveRangeEnd: "+inclusiveRangeEnd;
    }

}
