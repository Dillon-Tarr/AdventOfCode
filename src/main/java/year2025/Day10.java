package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static shared.StringMethods.toBinaryString;

class Day10 {
    static private final int DAY = 10;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final ArrayList<Machine> machines = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
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
            var split = s.split(" ");
            boolean[] goalState = null;
            int[] startCounts = null;
            var buttons = new ArrayList<int[]>();
            for (int i = 0; i < split.length; i++) {
                String sub = split[i];
                switch (sub.charAt(0)) {
                    case '[' -> {
                        sub = sub.substring(1, sub.length()-1);
                        goalState = new boolean[sub.length()];
                        for (int j = 0; j < sub.length(); j++) if (sub.charAt(j) == '#') goalState[j] = true;
                    }
                    case '(' -> {
                        sub = sub.substring(1, sub.length()-1);
                        String[] subSplit = sub.split(",");
                        var button = new int[subSplit.length];
                        for (int j = 0; j < subSplit.length; j++) button[j] = Integer.parseInt(subSplit[j]);
                        buttons.add(button);
                    }
                    case '{' -> {
                        sub = sub.substring(1, sub.length()-1);
                        String[] subSplit = sub.split(",");
                        startCounts = new int[subSplit.length];
                        for (int j = 0; j < subSplit.length; j++) startCounts[j] = Integer.parseInt(subSplit[j]);
                    }
                }
            }
            if (goalState == null || buttons.isEmpty() || startCounts == null) throw new RuntimeException("Bad input file.");
            machines.add(new Machine(goalState, buttons, startCounts));
        }
    }

    private static void solvePart1() {
        int sum = 0;
        for (var machine : machines) sum += getFewestPressesToAchieveGoalLights(machine);
        System.out.println("\nPart 1 presses sum: "+sum);
    }

    private static int getFewestPressesToAchieveGoalLights(Machine machine) {
        int buttonCount = machine.buttons.size(), fewestPresses = Integer.MAX_VALUE;
        var binaryComboInts = machine.oddParityStringToButtonComboIntsMap.get(toBinaryString(machine.part1GoalLights));
        for (int binaryCombo : binaryComboInts) {
            int presses = 0;
            for (int i = 0; i < buttonCount; i++) if ((binaryCombo & (1 << i)) != 0) presses++;
            if (presses < fewestPresses) fewestPresses = presses;
        }
        return fewestPresses;
    }

    private static void solvePart2() {
        int sum = 0;
        for (var machine : machines) sum += getFewestNeededPressesForCount(machine);
        System.out.println("\nPart 2 presses sum: "+sum);
    }

    private static int getFewestNeededPressesForCount(Machine machine) {
        var buttons = machine.buttons; var startCounts = machine.startCounts;
        int width = startCounts.length, initialPresses = 1,
                fewestPresses = getFewestRemainingPresses(machine, machine.startCounts);
        while (initialPresses < 5 || fewestPresses == Integer.MAX_VALUE) {
            // initialPresses could be required to be higher for correct fewest presses, depending on puzzle input.
            // In my case, only 2 are required. I was off by a very small number when not accounting for this.
            buttonLoop: for (var button : buttons) {
                var counts = Arrays.copyOf(startCounts, width);
                for (int n : button) if ((counts[n] -= initialPresses) < 0) continue buttonLoop;
                int presses = getFewestRemainingPresses(machine, counts);
                if (presses == Integer.MAX_VALUE) continue;
                presses += initialPresses;
                if (presses < fewestPresses) fewestPresses = presses;
            }
            initialPresses++;
        }
        return fewestPresses;
    }

    private static int getFewestRemainingPresses(Machine machine, int[] startCounts) {
        if (intArrayContainsOnlyZeros(startCounts)) return 0;
        var buttons = machine.buttons;
        int buttonCount = buttons.size(), width = startCounts.length, fewestPresses = Integer.MAX_VALUE;
        var goalLights = new boolean[width]; boolean allEven = true; int lightIndex = -1;
        while (++lightIndex < width)
            if (startCounts[lightIndex] % 2 == 1) { goalLights[lightIndex] = true; allEven = false; break; }
        while (++lightIndex < width)
            if (startCounts[lightIndex] % 2 == 1) goalLights[lightIndex] = true;
        if (allEven) {
            int doubleFactor = 1;
            var counts = Arrays.copyOf(startCounts, width);
            halvingLoop: while (true) {
                for (var count : counts) if (count % 2 == 1) break halvingLoop;
                for (int i = 0; i < width; i++) counts[i] >>= 1;
                doubleFactor <<= 1;
            }
            int subRemaining = getFewestRemainingPresses(machine, counts);
            fewestPresses = subRemaining == Integer.MAX_VALUE ? Integer.MAX_VALUE : doubleFactor*subRemaining;
        } else { // Continue with each possible way to make parity even.
            var buttonComboInts = machine.oddParityStringToButtonComboIntsMap.get(toBinaryString(goalLights));
            if (buttonComboInts != null) buttonComboLoop: for (int buttonComboInt : buttonComboInts) {
                int presses = 0;
                var counts = Arrays.copyOf(startCounts, width);
                for (int i = 0; i < buttonCount; i++) if ((buttonComboInt & (1 << i)) != 0) {
                    presses++;
                    var button = buttons.get(i);
                    for (int n : button) if (--counts[n] < 0) continue buttonComboLoop;
                }
                int doubleFactor = 1;
                if (!intArrayContainsOnlyZeros(counts)) {
                    halvingLoop: while (true) {
                        for (var count : counts) if (count % 2 == 1) break halvingLoop;
                        for (int i = 0; i < width; i++) counts[i] >>= 1;
                        doubleFactor <<= 1;
                    }
                    int subRemaining = getFewestRemainingPresses(machine, counts);
                    if (subRemaining == Integer.MAX_VALUE) continue;
                    presses += doubleFactor*subRemaining;
                }
                if (presses < fewestPresses) fewestPresses = presses;
            }
        }
        return fewestPresses;
    }

    private static boolean intArrayContainsOnlyZeros(int[] ints) {
        for (int n : ints) if (n != 0) return false;
        return true;
    }

    private static class Machine {
        boolean[] part1GoalLights;
        ArrayList<int[]> buttons;
        int[] startCounts;
        HashMap<String, ArrayList<Integer>> oddParityStringToButtonComboIntsMap = new HashMap<>();

        Machine(boolean[] goalLights, ArrayList<int[]> buttons, int[] startCounts) {
            this.part1GoalLights = goalLights;
            this.buttons = buttons;
            this.startCounts = startCounts;

            int width = startCounts.length, buttonCount = buttons.size();
            for (int binaryCombo = 1; binaryCombo < 1<<buttonCount; binaryCombo++) {
                boolean[] state = new boolean[width];
                for (int i = 0; i < buttonCount; i++) {
                    if ((binaryCombo & (1 << i)) != 0) {
                        var button = buttons.get(i);
                        for (int n : button) state[n] = !state[n];
                    }
                }
                var stateString = toBinaryString(state);
                var ints = oddParityStringToButtonComboIntsMap.getOrDefault(stateString, new ArrayList<>());
                if (ints.isEmpty()) oddParityStringToButtonComboIntsMap.put(stateString, ints);
                ints.add(binaryCombo);
            }
        }

    }

}
