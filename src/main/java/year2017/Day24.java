package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day24 {
    static private final int DAY = 24;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final ArrayList<Component> components = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        createComponents();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void createComponents() {
        String[] split;
        for (String s : inputStrings) {
            split = s.split("/");
            components.add(new Component(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
        }
    }

    private static void solve() {
        ArrayList<ArrayList<Component>> combos = build(0, new ArrayList<>());
        int comboStrength, highestStrength = -1, longestLength = -1;
        for (var combo : combos) {
            if (combo.size() > longestLength) longestLength = combo.size();
            comboStrength = 0;
            for (Component c : combo) comboStrength += c.strength;
            if (comboStrength > highestStrength) highestStrength = comboStrength;
        }
        System.out.println("\nHighest possible bridge strength (part 1 answer): "+highestStrength);
        highestStrength = -1;
        for (var combo : combos) {
            if (combo.size() != longestLength) continue;
            comboStrength = 0;
            for (Component c : combo) comboStrength += c.strength;
            if (comboStrength > highestStrength) highestStrength = comboStrength;
        }
        System.out.println("\nHighest possible bridge strength of longest possible length bridge (part 2 answer): "+highestStrength);
    }

    private static ArrayList<ArrayList<Component>> build(int currentPort, ArrayList<Component> currentUsed) {
        ArrayList<ArrayList<Component>> combos = new ArrayList<>(); combos.add(currentUsed);
        ArrayList<Component> available = new ArrayList<>(), nextUsed; int nextPort;
        for (Component c : components) if (!currentUsed.contains(c)) available.add(c);
        for (Component a : available) {
            nextPort = a.checkCompatibility(currentPort); if (nextPort == -1) continue;
            nextUsed = new ArrayList<>(currentUsed); nextUsed.add(a);
            combos.addAll(build(nextPort, nextUsed));
        }
        return combos;
    }

    private static class Component {
        private final int p1, p2, strength;

        private Component(int a, int b) { p1 = a; p2 = b; strength = a+b; }

        private int checkCompatibility(int p) { if (p == p1) return p2; if (p == p2) return p1; return -1; }

    }

}
