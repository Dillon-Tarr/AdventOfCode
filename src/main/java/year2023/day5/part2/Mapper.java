package year2023.day5.part2;

import java.util.ArrayList;
import java.util.Collections;

public final class Mapper {
    MappingDataContainer[] mappingDataContainers;

    Mapper(ArrayList<long[]> map) {
        mappingDataContainers = new MappingDataContainer[map.size()];
        for (int i = 0; i < map.size(); i++) {
            long[] originalMapValues = map.get(i);
            long destinationRangeStart = originalMapValues[0];
            long sourceRangeStart = originalMapValues[1];
            long valueToAdd = destinationRangeStart - sourceRangeStart;
            long rangeLength = originalMapValues[2];
            long inclusiveSourceRangeEnd = sourceRangeStart + rangeLength - 1;
            mappingDataContainers[i] = new MappingDataContainer
                    (sourceRangeStart, inclusiveSourceRangeEnd, valueToAdd);
        }
    }

    public void transformRanges(ArrayList<InclusiveNumberRange> ranges) {
        ArrayList<InclusiveNumberRange> transformedRanges = new ArrayList<>();
        for (int i = 0; i < ranges.size(); i++) {
            InclusiveNumberRange range = ranges.get(i);
            long rangeStart = range.getRangeStart();
            long inclusiveRangeEnd = range.getInclusiveRangeEnd();
            for (MappingDataContainer mappingDataContainer : mappingDataContainers) {
                long mapRangeStart = mappingDataContainer.getRangeStart();
                long mapInclusiveRangeEnd = mappingDataContainer.getInclusiveRangeEnd();
                long mapValueToAdd = mappingDataContainer.getValueToAdd();
                boolean rangeStartsInMapRange = rangeStart >= mapRangeStart && rangeStart <= mapInclusiveRangeEnd;
                boolean rangeEndsInMapRange = inclusiveRangeEnd >= mapRangeStart && inclusiveRangeEnd <= mapInclusiveRangeEnd;
                if (rangeStartsInMapRange && rangeEndsInMapRange) {
                    transformedRanges.add(new InclusiveNumberRange(rangeStart+mapValueToAdd, inclusiveRangeEnd+mapValueToAdd));
                    ranges.set(i, null);
                    rangeStart = -1;
                    inclusiveRangeEnd = -1;
                } else if (rangeEndsInMapRange) {
                        transformedRanges.add(new InclusiveNumberRange(mapRangeStart+mapValueToAdd, inclusiveRangeEnd+mapValueToAdd));
                        ranges.set(i, new InclusiveNumberRange(rangeStart, mapRangeStart-1));
                    inclusiveRangeEnd = mapRangeStart-1;
                } else if (rangeStartsInMapRange) {
                    transformedRanges.add(new InclusiveNumberRange(rangeStart+mapValueToAdd, mapInclusiveRangeEnd+mapValueToAdd));
                    ranges.set(i, new InclusiveNumberRange(mapInclusiveRangeEnd+1, inclusiveRangeEnd));
                    rangeStart = mapInclusiveRangeEnd+1;
                }
            }
        }
        ranges.addAll(transformedRanges);
        ranges.removeAll(Collections.singleton(null));
        InclusiveNumberRange.mergeOverlappingRanges(ranges);
        InclusiveNumberRange.sortRangesByRangeStartLowToHigh(ranges);
    }

    private final class MappingDataContainer {
        private final long rangeStart, inclusiveRangeEnd, valueToAdd;

        MappingDataContainer(long rangeStart, long inclusiveRangeEnd, long valueToAdd) {
            this.rangeStart = rangeStart;
            this.inclusiveRangeEnd = inclusiveRangeEnd;
            this.valueToAdd = valueToAdd;
        }

        public long getRangeStart() {return rangeStart;}
        public long getInclusiveRangeEnd() {return inclusiveRangeEnd;}
        public long getValueToAdd() {return valueToAdd;}
    }

}
