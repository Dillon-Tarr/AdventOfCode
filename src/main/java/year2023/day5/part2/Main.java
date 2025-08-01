package year2023.day5.part2;

import shared.LongInclusiveNumberRange;

import java.io.*;
import java.util.ArrayList;

class Main {
    static private final int DAY = 5;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");

    static private final ArrayList<LongInclusiveNumberRange> seedRanges = new ArrayList<>();
    static private final ArrayList<LongInclusiveNumberRange> locationNumberRanges = new ArrayList<>();

    static private final ArrayList<long[]> seedToSoilMap = new ArrayList<>();
    static private final ArrayList<long[]> soilToFertilizerMap = new ArrayList<>();
    static private final ArrayList<long[]> fertilizerToWaterMap = new ArrayList<>();
    static private final ArrayList<long[]> waterToLightMap = new ArrayList<>();
    static private final ArrayList<long[]> lightToTemperatureMap = new ArrayList<>();
    static private final ArrayList<long[]> temperatureToHumidityMap = new ArrayList<>();
    static private final ArrayList<long[]> humidityToLocationMap = new ArrayList<>();

    static private Mapper seedToSoilMapper;
    static private Mapper soilToFertilizerMapper;
    static private Mapper fertilizerToWaterMapper;
    static private Mapper waterToLightMapper;
    static private Mapper lightToTemperatureMapper;
    static private Mapper temperatureToHumidityMapper;
    static private Mapper humidityToLocationMapper;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputDataAndMaps();

        seedToSoilMapper = new Mapper(seedToSoilMap);
        soilToFertilizerMapper = new Mapper(soilToFertilizerMap);
        fertilizerToWaterMapper = new Mapper(fertilizerToWaterMap);
        waterToLightMapper = new Mapper(waterToLightMap);
        lightToTemperatureMapper = new Mapper(lightToTemperatureMap);
        temperatureToHumidityMapper = new Mapper(temperatureToHumidityMap);
        humidityToLocationMapper = new Mapper(humidityToLocationMap);
        System.out.println("\nMappers created.");

        mapSeedsToLocationNumbers();
        getLowestLocationNumber();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputDataAndMaps() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            String stringOfSeeds = currentLine.substring(currentLine.indexOf(":")+2);
            String[] seedStrings = stringOfSeeds.split(" ");
            for (int i = 0; i < seedStrings.length; i+=2){
                long seedRangeStart = Long.parseLong(seedStrings[i]);
                long seedRangeLength = Long.parseLong(seedStrings[i+1]);
                long inclusiveSeedRangeEnd = seedRangeStart + seedRangeLength - 1;
                seedRanges.add(new LongInclusiveNumberRange(seedRangeStart, inclusiveSeedRangeEnd));
            }
            LongInclusiveNumberRange.mergeOverlappingRanges(seedRanges);
            System.out.println(seedRanges);

            currentLine = br.readLine();

            ArrayList<long[]> map = seedToSoilMap;
            while (currentLine != null) {
                if (currentLine.contains(":")) {
                    switch (currentLine) {
                        case "seed-to-soil map:" -> map = seedToSoilMap;
                        case "soil-to-fertilizer map:" -> map = soilToFertilizerMap;
                        case "fertilizer-to-water map:" -> map = fertilizerToWaterMap;
                        case "water-to-light map:" -> map = waterToLightMap;
                        case "light-to-temperature map:" -> map = lightToTemperatureMap;
                        case "temperature-to-humidity map:" -> map = temperatureToHumidityMap;
                        case "humidity-to-location map:" -> map = humidityToLocationMap;
                    }
                } else if (!currentLine.isBlank()) {
                    String[] mapLineValues = currentLine.split(" ");
                    map.add(new long[]{Long.parseLong(mapLineValues[0]),
                            Long.parseLong(mapLineValues[1]), Long.parseLong(mapLineValues[2])});
                }
                currentLine = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\nAll seed and map data obtained from input file.\n\nAll seed number ranges identified.");
    }

    private static void mapSeedsToLocationNumbers() {
        ArrayList<LongInclusiveNumberRange> ranges = new ArrayList<>(seedRanges.size());
        for (LongInclusiveNumberRange seedRange : seedRanges) ranges.add(new LongInclusiveNumberRange(seedRange.rangeStart(), seedRange.inclusiveRangeEnd()));
        System.out.println("Seed ranges:\n"+ranges);
        seedToSoilMapper.transformRanges(ranges); System.out.println("\nSoil ranges:\n"+ranges);
        soilToFertilizerMapper.transformRanges(ranges); System.out.println("\nFertilizer ranges:\n"+ranges);
        fertilizerToWaterMapper.transformRanges(ranges); System.out.println("\nWater ranges:\n"+ranges);
        waterToLightMapper.transformRanges(ranges); System.out.println("\nLight ranges:\n"+ranges);
        lightToTemperatureMapper.transformRanges(ranges); System.out.println("\nTemperature ranges:\n"+ranges);
        temperatureToHumidityMapper.transformRanges(ranges); System.out.println("\nHumidity ranges:\n"+ranges);
        humidityToLocationMapper.transformRanges(ranges); System.out.println("\nLocation ranges:\n"+ranges);
        locationNumberRanges.addAll(ranges);

        System.out.println("\nMapped each seed to soil to fertilizer to water to light to temperature to humidity to location.");
    }

    private static void getLowestLocationNumber() {
        long lowestLocationNumber = Long.MAX_VALUE;
            for (LongInclusiveNumberRange range : locationNumberRanges) {
                long rangeStart = range.rangeStart();
                if (rangeStart < lowestLocationNumber)
                    lowestLocationNumber = rangeStart;
            }
        System.out.println("\nLOWEST LOCATION NUMBER:\n\n"+lowestLocationNumber);
    }

}