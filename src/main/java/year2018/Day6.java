package year2018;

import shared.Coordinates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Day6 {
    static private final int DAY = 6;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<Coordinates> locations = new ArrayList<>();
    static private int lowX = Integer.MAX_VALUE, lowY = Integer.MAX_VALUE,
    highX = Integer.MIN_VALUE, highY = Integer.MIN_VALUE;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine(); String[] pair;
            int x, y;
            while (s != null) {
                pair = s.split(", ");
                x = Integer.parseInt(pair[0]);
                y = Integer.parseInt(pair[1]);
                locations.add(new Coordinates(y, x));
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        int x, y;
        for (var location : locations) {
            x = location.x; if (x < lowX) lowX = x; if (x > highX) highX = x;
            y = location.y; if (y < lowY) lowY = y; if (y > highY) highY = y;
        }
    }

    private static void solvePart1() {
        var owningLocations = new Coordinates[highY+1][];
        for (int y = lowY; y <= highY; y++) {
            owningLocations[y] = new Coordinates[highX+1];
            for (int x = lowX; x <= highX; x++) {
                owningLocations[y][x] = findNearestLocation(new Coordinates(y, x));
            }
        }
        HashSet<Coordinates> boundaryLocations = new HashSet<>();
        Coordinates location;
        for (int y = lowY; y <= highY; y++) {
            location = owningLocations[y][lowX]; if (location != null) boundaryLocations.add(location);
            location = owningLocations[y][highX]; if (location != null) boundaryLocations.add(location);
        }
        for (int x = lowX+1; x <= highX-1; x++) {
            location = owningLocations[lowY][x]; if (location != null) boundaryLocations.add(location);
            location = owningLocations[highY][x]; if (location != null) boundaryLocations.add(location);
        }
        ArrayList<Coordinates> nonInfiniteAreaLocations = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            location = locations.get(i);
            if (!boundaryLocations.contains(location)) nonInfiniteAreaLocations.add(location);
        }
        var locationToOwnedCoordinatesCountMap = new HashMap<Coordinates, Integer>();
        for (int y = lowY; y <= highY; y++) for (int x = lowX; x <= highX; x++) {
            location = owningLocations[y][x];
            if (!nonInfiniteAreaLocations.contains(location)) continue;
            int existingCount = locationToOwnedCoordinatesCountMap.getOrDefault(location, 0);
            locationToOwnedCoordinatesCountMap.put(location, existingCount+1);
        }
        int highestCount = Integer.MIN_VALUE;
        for (int i = 0; i < nonInfiniteAreaLocations.size(); i++) {
            location = nonInfiniteAreaLocations.get(i);
            int count = locationToOwnedCoordinatesCountMap.get(location);
            if (count > highestCount) highestCount = count;
        }
        System.out.println("\nLargest non-infinite area (part 1 answer): "+highestCount);
    }

    private static Coordinates findNearestLocation(Coordinates coordinates) {
        var locationToDistanceMap = new HashMap<Coordinates, Integer>();
        int distance, closestDistance = Integer.MAX_VALUE; Coordinates closestLocation = null;
        for (var location : locations) {
            distance = coordinates.getManhattanDistance(location);
            locationToDistanceMap.put(location, distance);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestLocation = location;
            }
        }
        for (var location : locations) if (location != closestLocation
                && locationToDistanceMap.get(location) == closestDistance) return null;
        return closestLocation;
    }

    private static void solvePart2() {
        int sum, count = 0;
        Coordinates coordinates;
        for (int y = lowY; y <= highY; y++) {
            xLoop: for (int x = lowX; x <= highX; x++) {
                coordinates = new Coordinates(y, x);
                sum = 0;
                for (var location : locations) {
                    sum += coordinates.getManhattanDistance(location);
                    if (sum >= 10000) continue xLoop;
                }
                count++;
            }
        }
        System.out.println("\nCount of locations with combined distance of 9999 or less..."+
                "\n...from all provided coordinates (part 2 answer): "+count);
    }

}
