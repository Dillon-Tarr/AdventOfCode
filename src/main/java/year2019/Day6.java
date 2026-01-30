package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Day6 {
    static private final int DAY = 6;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solve() {
        ArrayList<String[]> orbitPairs = new ArrayList<>();
        for (var s : inputStrings) orbitPairs.add(s.split("\\)"));
        HashMap<String, OrbitalObject> objects = new HashMap<>();
        for (var ss : orbitPairs) for (var s : ss) if (!objects.containsKey(s)) objects.put(s, new OrbitalObject());
        for (var ss : orbitPairs) objects.get(ss[0]).addOrbiter(objects.get(ss[1]));
        int orbitCount = 0;
        for (var object :  objects.values()) {
            var orbitedObject = object.orbitedObject;
            while (orbitedObject != null) { orbitCount++; orbitedObject = orbitedObject.orbitedObject; }
        }
        System.out.println("\nDirect and indirect orbit count (part 1 answer): "+ orbitCount);

        var startObject = objects.get("YOU").orbitedObject; var goalObject = objects.get("SAN").orbitedObject;
        ArrayList<OrbitalObject> currentObjects = new ArrayList<>(), nextObjects = new ArrayList<>();
        HashSet<OrbitalObject> seenObjects = new HashSet<>();
        nextObjects.add(startObject); seenObjects.add(startObject);
        int stepCount = 0;
        mainLoop: while (true) {
            currentObjects.addAll(nextObjects); nextObjects.clear();
            for (var object : currentObjects) {
                if (object == goalObject) break mainLoop;
                var connectedObjects = new ArrayList<>(object.orbitingObjects);
                if (object.orbitedObject != null) connectedObjects.add(object.orbitedObject);
                for (var conObj : connectedObjects) if (seenObjects.add(conObj)) nextObjects.add(conObj);
            }
            currentObjects.clear(); stepCount++;
        }
        System.out.println("\nNumber of orbital transfers needed to reach the object orbited by SAN (part 2 answer): "+stepCount);
    }

    private static class OrbitalObject {
        OrbitalObject orbitedObject = null;
        ArrayList<OrbitalObject> orbitingObjects = new ArrayList<>();

        private void addOrbiter(OrbitalObject orbiter) {
            orbitingObjects.add(orbiter);
            orbiter.orbitedObject = this;
        }

    }

}
