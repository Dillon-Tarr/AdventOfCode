package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Day12 {
    static private final int DAY = 12;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final HashSet<Integer> plantPositions = new HashSet<>();
    static private final HashMap<String, Boolean> rules = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        ArrayList<String> inputStrings = new ArrayList<>();
        String s;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        s = inputStrings.getFirst().substring(15);
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == '#') plantPositions.add(i);
        for (int i = 2; i < inputStrings.size(); i++) {
            s = inputStrings.get(i);
            rules.put(s.substring(0, 5), s.charAt(9) == '#');
        }
    }

    private static void solve() {
        long fiftyBilSum = 0;
        int previousSum = 0, currentSum, part1Sum = 0, low, high,
                currentIncrease, previousIncrease = -1, sameIncreaseCount = 0;
        for (var position : plantPositions) previousSum += position;
        HashSet<Integer> newPlantPositions = new HashSet<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            currentSum = 0; for (var position : plantPositions) currentSum += position; currentIncrease = currentSum-previousSum;
            System.out.println("Sum after "+i+" generations: "+currentSum+"; Increase: "+(currentIncrease));
            if (previousIncrease == currentIncrease) {
                if (++sameIncreaseCount > 20) {
                    fiftyBilSum = currentSum+(currentIncrease*(50_000_000_000L-i));
                    break;
                }
            } else sameIncreaseCount = 0;
            if (i == 20) part1Sum = currentSum;
            previousSum = currentSum; previousIncrease = currentIncrease;
            low = Integer.MAX_VALUE; high = Integer.MIN_VALUE;
            for (Integer position : plantPositions) {
                if (position < low) low = position;
                if (position > high) high = position;
            }
            for (int j = low-2; j < high+2; j++) {
                var pattern = new StringBuilder();
                pattern.append(plantPositions.contains(j-2) ? '#' : '.');
                pattern.append(plantPositions.contains(j-1) ? '#' : '.');
                pattern.append(plantPositions.contains(j) ? '#' : '.');
                pattern.append(plantPositions.contains(j+1) ? '#' : '.');
                pattern.append(plantPositions.contains(j+2) ? '#' : '.');
                if (rules.getOrDefault(pattern.toString(), false)) newPlantPositions.add(j);
            }
            plantPositions.clear(); plantPositions.addAll(newPlantPositions); newPlantPositions.clear();
        }
        System.out.println("\nSum of plant position numbers after 20 generations (part 1 answer): "+part1Sum);
        System.out.println("\nSum of plant position numbers after 50 billion generations (part 2 answer): "+fiftyBilSum);
    }

}
