package day5.part1;

import java.io.*;
import java.util.ArrayList;

public class Main {
    static private final String INPUT_FILE_PATH = "input-files/day5input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);

    static private final ArrayList<Long> seeds = new ArrayList<>();
    static private final ArrayList<Long> locationNumbers = new ArrayList<>();

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
            for (String string : seedStrings) seeds.add(Long.parseLong(string));

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

        System.out.println("\nAll seed and map data obtained from input file.");
    }

    private static void mapSeedsToLocationNumbers() {
        for (long seed : seeds) {
            locationNumbers.add(humidityToLocationMapper
                    .mapValue(temperatureToHumidityMapper
                            .mapValue(lightToTemperatureMapper
                                    .mapValue(waterToLightMapper
                                            .mapValue(fertilizerToWaterMapper
                                                    .mapValue(soilToFertilizerMapper
                                                            .mapValue(seedToSoilMapper.mapValue(seed))))))));
        }
        System.out.println("\nMapped each seed to soil to fertilizer to water to light to temperature to humidity to location.");
    }

    private static void getLowestLocationNumber() {
        long lowestLocationNumber = Long.MAX_VALUE;
        for (long locationNumber : locationNumbers)
            if (locationNumber < lowestLocationNumber)
                lowestLocationNumber = locationNumber;

        System.out.println("\nLOWEST LOCATION NUMBER:\n\n"+lowestLocationNumber);
    }

}