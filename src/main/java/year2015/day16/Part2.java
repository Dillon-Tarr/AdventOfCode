package year2015.day16;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Part2 {
    static private final int DAY = 16;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final List<HashMap<String, Integer>> sues = new ArrayList<>(500);
    static private int giftGiverSueNumber = -1;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        identifyGiftGiverSue();

        System.out.println("Real number of gift-giver Sue: "+ giftGiverSueNumber);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        String inputString;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
            while (inputString != null) {
                processSueData(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processSueData(String inputString) {
        String key1, key2, key3;
        int val1, val2, val3, spaceIndex, commaIndex, firstColonIndex;

        spaceIndex = inputString.lastIndexOf(' ');
        val3 = Integer.parseInt(inputString.substring(spaceIndex+1));
        commaIndex = inputString.lastIndexOf(',');
        key3 = inputString.substring(commaIndex+2, spaceIndex-1);
        spaceIndex = inputString.lastIndexOf(' ', commaIndex);
        val2 = Integer.parseInt(inputString.substring(spaceIndex+1, commaIndex));
        commaIndex = inputString.lastIndexOf(',', spaceIndex);
        key2 = inputString.substring(commaIndex+2, spaceIndex-1);
        spaceIndex = inputString.lastIndexOf(' ', commaIndex);
        val1 = Integer.parseInt(inputString.substring(spaceIndex+1, commaIndex));
        firstColonIndex = inputString.indexOf(':');
        key1 = inputString.substring(firstColonIndex+2, spaceIndex-1);

        HashMap<String, Integer> sue = new HashMap<>();
        sue.put(key1, val1);
        sue.put(key2, val2);
        sue.put(key3, val3);
        sues.add(sue);
    }

    private static void identifyGiftGiverSue() {
        HashMap<String, Integer> sue;
        for (int i = 0; i < 500; i++) {
            sue = sues.get(i);
            Integer children = sue.get("children");
            if (children != null && children != 3) continue;
            Integer cats = sue.get("cats");
            if (cats != null && cats <= 7) continue;
            Integer samoyeds = sue.get("samoyeds");
            if (samoyeds != null && samoyeds != 2) continue;
            Integer pomeranians = sue.get("pomeranians");
            if (pomeranians != null && pomeranians >= 3) continue;
            Integer akitas = sue.get("akitas");
            if (akitas != null && akitas != 0) continue;
            Integer vizslas = sue.get("vizslas");
            if (vizslas != null && vizslas != 0) continue;
            Integer goldfish = sue.get("goldfish");
            if (goldfish != null && goldfish >= 5) continue;
            Integer trees = sue.get("trees");
            if (trees != null && trees <= 3) continue;
            Integer cars = sue.get("cars");
            if (cars != null && cars != 2) continue;
            Integer perfumes = sue.get("perfumes");
            if (perfumes != null && perfumes != 1) continue;
            giftGiverSueNumber = i+1;
            break;
        }
    }

}
