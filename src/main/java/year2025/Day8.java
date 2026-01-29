package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

class Day8 {
    static private final int DAY = 8;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final ArrayList<JBox> allJBoxes = new ArrayList<>();
    static private final ArrayList<JBoxPair> boxPairs = new ArrayList<>();
    static private final ArrayList<HashSet<JBox>> circuits = new ArrayList<>();
    static private final int part1Connections = 1000;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        getAndSortPairDistances();
        connectInitialBoxes();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        var inputStrings = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (var s : inputStrings) {
            var split = s.split(",");
            JBox box = new JBox(Long.parseLong(split[0]), Long.parseLong(split[1]), Long.parseLong(split[2]));
            allJBoxes.add(box);
            circuits.add(box.circuit);
        }
    }

    private static void getAndSortPairDistances() {
        for (int i = 0; i < allJBoxes.size()-1; i++) {
            JBox b1 = allJBoxes.get(i);
            for (int j = i+1; j < allJBoxes.size(); j++) {
                JBox b2 = allJBoxes.get(j);
                long xDiff = Math.abs(b1.x-b2.x), yDiff = Math.abs(b1.y-b2.y), zDiff = Math.abs(b1.z-b2.z);
                double distance = Math.sqrt((xDiff*xDiff)+(yDiff*yDiff)+(zDiff*zDiff));
                boxPairs.add(new JBoxPair(b1, b2, distance));
            }
        }
        boxPairs.sort(Comparator.comparingDouble(pair -> pair.distance));
    }

    private static void connectInitialBoxes() {
        JBoxPair pair; JBox b1, b2; HashSet<JBox> c1, c2;
        if (boxPairs.size() < part1Connections) throw new RuntimeException("Box pair count is less than requested connection count.");
        for (int i = 0; i < part1Connections; i++) {
            pair = boxPairs.get(i); b1 = pair.box1; b2 = pair.box2; c1 = b1.circuit; c2 = b2.circuit;
            if (c1 != c2) {
                if (c1.size() > c2.size()) { circuits.remove(c2); c1.addAll(c2); for (var box : c2) box.circuit = c1; }
                else { circuits.remove(c1); c2.addAll(c1); for (var box : c1) box.circuit = c2; }
            }
        }
    }

    private static void solvePart1() {
        circuits.sort(Comparator.comparing(HashSet::size));
        int i = circuits.size();
        int product = circuits.get(--i).size() * circuits.get(--i).size() * circuits.get(--i).size();
        System.out.println("\nProduct of sizes of the three largest circuits (part 1 answer): "+product);
    }

    private static void solvePart2() {
        JBoxPair pair = null; JBox b1, b2; HashSet<JBox> c1, c2; int circuitCount = circuits.size();
        for (int i = part1Connections; i < boxPairs.size(); i++) {
            pair = boxPairs.get(i); b1 = pair.box1; b2 = pair.box2; c1 = b1.circuit; c2 = b2.circuit;
            if (c1 != c2) {
                if (--circuitCount == 1) break;
                if (c1.size() > c2.size()) { circuits.remove(c2); c1.addAll(c2); for (var box : c2) box.circuit = c1; }
                else { circuits.remove(c1); c2.addAll(c1); for (var box : c1) box.circuit = c2; }
            }
        }
        assert pair != null;
        System.out.println("\nProduct of last two box x values (part 2 answer): "+(pair.box1.x*pair.box2.x));
    }

    private static class JBox {
        long x, y, z;
        HashSet<JBox> circuit = new HashSet<>();

        JBox(long x, long y, long z) {
            this.x = x; this.y = y; this.z = z;
            circuit.add(this);
        }

    }

    private record JBoxPair(JBox box1, JBox box2, double distance) {}

}
