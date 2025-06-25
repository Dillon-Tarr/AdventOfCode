package year2023.day5.part1;

import java.util.ArrayList;

public final class Mapper {
    MappingDataContainer[] mappingDataContainers;

    Mapper(ArrayList<long[]> map){
        mappingDataContainers = new MappingDataContainer[map.size()];
        for (int i = 0; i < map.size(); i++) {
            long[] originalMapValues = map.get(i);
            long destinationRangeStart = originalMapValues[0];
            long sourceRangeStart = originalMapValues[1];
            long valueToAdd = destinationRangeStart - sourceRangeStart;
            long rangeLength = originalMapValues[2];
            long inclusiveSourceRangeEnd = sourceRangeStart+rangeLength -1;
            mappingDataContainers[i] = new MappingDataContainer
                    (sourceRangeStart, inclusiveSourceRangeEnd, valueToAdd);
        }
    }

    public long mapValue(long value) {
        for (MappingDataContainer mappingDataContainer : mappingDataContainers)
            if (value >= mappingDataContainer.rangeStart && value <= mappingDataContainer.rangeEndInclusive)
                return value + mappingDataContainer.valueToAdd;
        return value;
    }

    private final class MappingDataContainer {
        long rangeStart, rangeEndInclusive, valueToAdd;

        MappingDataContainer(long rangeStart, long rangeEndInclusive, long valueToAdd) {
            this.rangeStart = rangeStart;
            this.rangeEndInclusive = rangeEndInclusive;
            this.valueToAdd = valueToAdd;
        }
    }

}
