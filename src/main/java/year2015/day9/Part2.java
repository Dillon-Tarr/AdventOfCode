package year2015.day9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

class Part2 {
    static private final int DAY = 9;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final HashMap<String, Integer> locationsMap = new HashMap<>();
    static private final HashMap<Integer, HashMap<Integer, Integer>> edgeToDistanceMap = new HashMap<>();
    static private int longestEncounteredPathDistance = Integer.MIN_VALUE;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getLocations();
        mapEdgesToDistances();
        getMirrorlessPossiblePaths();

        System.out.println("Shortest path's distance: "+ longestEncounteredPathDistance);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getLocations() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            int spaceIndex, equalsIndex;
            String inputString = br.readLine();
            while (inputString != null) {
                spaceIndex = inputString.indexOf(' ');
                equalsIndex = inputString.lastIndexOf('=');
                locationsMap.putIfAbsent(inputString.substring(0, spaceIndex), locationsMap.size());
                locationsMap.putIfAbsent(inputString.substring(spaceIndex+4, equalsIndex-1), locationsMap.size());
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void mapEdgesToDistances() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            int spaceIndex, equalsIndex, loc1, loc2, distance;
            String inputString = br.readLine();
            while (inputString != null) {
                spaceIndex = inputString.indexOf(' ');
                equalsIndex = inputString.lastIndexOf('=');
                loc1 = locationsMap.get(inputString.substring(0, spaceIndex));
                loc2 = locationsMap.get(inputString.substring(spaceIndex+4, equalsIndex-1));
                distance = Integer.parseInt(inputString.substring(equalsIndex+2));
                if (!edgeToDistanceMap.containsKey(loc1)) edgeToDistanceMap.put(loc1, new HashMap<>());
                if (!edgeToDistanceMap.containsKey(loc2)) edgeToDistanceMap.put(loc2, new HashMap<>());
                edgeToDistanceMap.get(loc1).putIfAbsent(loc2, distance);
                edgeToDistanceMap.get(loc2).putIfAbsent(loc1, distance);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void getMirrorlessPossiblePaths() {
        HashSet<String> possibilities = PossibilityChecker.getMirrorlessPossibilities(locationsMap.size());
        for (String s : possibilities) {
            int distance = 0;
            char c1 , c2 = s.charAt(0);
            for (int i = 1; i < s.length(); i++) {
                c1 = c2;
                c2 = s.charAt(i);
                distance += edgeToDistanceMap.get(c1-'0').get(c2-'0');
            }
            if (distance > longestEncounteredPathDistance) longestEncounteredPathDistance = distance;
        }
    }

}
