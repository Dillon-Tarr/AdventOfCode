package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Day10 {
    static private final int DAY = 10;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final ArrayList<Machine> machines = new ArrayList<>();
    static private int activeSearchCount;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
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
            int[] goalCounts = null;
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
                        goalCounts = new int[subSplit.length];
                        for (int j = 0; j < subSplit.length; j++) goalCounts[j] = Integer.parseInt(subSplit[j]);
                    }
                }
            }
            if (goalState == null || buttons.isEmpty() || goalCounts == null) throw new RuntimeException("Bad input file.");
            machines.add(new Machine(goalState, buttons, goalCounts));
        }
    }

    private static void solvePart1() {
        int sum = 0;
        for (var machine : machines) sum += getFewestPressesToAchieveGoalLights(machine);
        System.out.println("\nPart 1 presses sum: "+sum);
    }

    private static int getFewestPressesToAchieveGoalLights(Machine machine) {
        var goalLights = machine.part1GoalLights; int width = goalLights.length;
        var buttons = machine.buttons; int buttonCount = buttons.size();
        int fewestPresses = Integer.MAX_VALUE;
        for (int binaryCombo = 1; binaryCombo < 1<<buttonCount; binaryCombo++) {
            boolean[] state = new boolean[width];
            int presses = 0;
            for (int i = 0; i < buttonCount; i++) {
                if ((binaryCombo & (1 << i)) != 0) {
                    presses++;
                    var button = buttons.get(i);
                    for (int j = 0; j < button.length; j++) state[button[j]] = !state[button[j]];
                }
                if (presses < fewestPresses && Arrays.equals(state, goalLights)) fewestPresses = presses;
            }
        }
        return fewestPresses;
    }

    private static void solvePart2() {
        int sum = 0; int machineNumber = 0;
        for (var machine : machines) {
            System.out.println("\tTrying machine #"+(++machineNumber)+"...");
            activeSearchCount = 0;
            int presses = getFewestRemainingPresses(machine.buttons, machine.goalCounts);
            System.out.println("\tFewest presses: "+presses);
            sum += presses;
        }
        System.out.println("\nPart 2 presses sum: "+sum);
    }

    private static int getFewestRemainingPresses(ArrayList<int[]> buttons, int[] goalCounts) {
        System.out.println("Active search count increased to: "+(++activeSearchCount));
        boolean anyZeros = false, anyNonZeros = false;
        for (int count : goalCounts) {
            if (count < 0) throw new RuntimeException("How less than 0?");
            if (count == 0) anyZeros = true;
            else anyNonZeros = true;
        }
        if (!anyNonZeros) {
            System.out.println("Active search count decreased to: "+(--activeSearchCount));
            return 0;
        }
        int buttonCount = buttons.size(), width = goalCounts.length;
        int fewestPresses = Integer.MAX_VALUE;
        if (anyZeros) {
            fewestPresses = getRemainingPressesWithStack(buttons, goalCounts);
//            System.out.println("Amount returned from stack: "+fewestPresses);
        }
        else { // no zeros:
            var goalLights = new boolean[width];
            boolean anyOdds = false;
            for (int i = 0; i < width; i++) if (goalCounts[i] % 2 == 1) { goalLights[i] = true; anyOdds = true; }
            if (anyOdds) {
                var buttonComboInts = getEveningButtonComboInts(buttons, goalLights);
//                System.out.println("Number of button combos we're working with: "+buttonComboInts.size());
                int buttonComboNumber = 0;
                buttonComboLoop: for (int buttonComboInt : buttonComboInts) {
//                    System.out.println("Button combo number: "+(buttonComboNumber++));
                    int initialPresses = 0;
                    var counts = Arrays.copyOf(goalCounts, width);
                    for (int i = 0; i < buttonCount; i++) {
                        if ((buttonComboInt & (1 << i)) != 0) {
                            initialPresses++;
                            var button = buttons.get(i);
                            for (int j = 0; j < button.length; j++) counts[button[j]]--;
                        }
                    }
                    anyZeros = false;
                    for (var count : counts) {
                        if (count < 0) continue buttonComboLoop;
                        else if (count == 0) anyZeros = true;
                    }
                    int doubleFactor = 1;
                    if (!anyZeros) halvingLoop: while (true) {
                        for (var count : counts) if (count % 2 == 1) break halvingLoop;
                        for (int i = 0; i < width; i++) counts[i] /= 2;
                        doubleFactor *= 2;
                    }
                    int subRemaining = getFewestRemainingPresses(buttons, counts);
                    if (subRemaining == Integer.MAX_VALUE) continue;
                    int totalPresses = initialPresses+(doubleFactor*subRemaining);
                    if (totalPresses < fewestPresses) fewestPresses = totalPresses;
                }
            } else { // no odds to work out, only evens (no zeros):
                int doubleFactor = 1;
                var counts = Arrays.copyOf(goalCounts, width);
                halvingLoop: while (true) {
                    for (var count : counts) if (count % 2 == 1) break halvingLoop;
                    for (int i = 0; i < width; i++) counts[i] /= 2;
                    doubleFactor *= 2;
                }
                int subRemaining = getFewestRemainingPresses(buttons, counts);
                fewestPresses = subRemaining == Integer.MAX_VALUE ? Integer.MAX_VALUE : doubleFactor*subRemaining;
            }
        }
        System.out.println("Active search count decreased to: "+(--activeSearchCount));
        return fewestPresses;
    }

    private static int getRemainingPressesWithStack(ArrayList<int[]> buttons, int[] goalCounts) {
        int width = goalCounts.length;
        var queue = new PriorityQueue<PressRecord>(); var visited = new HashSet<String>();
        for (var button : buttons) queue.add(new PressRecord(Arrays.copyOf(goalCounts, width), 0, button, button.length));
        queueLoop: while (!queue.isEmpty()) {
            var queuedPress = queue.remove();
            int[] counts = queuedPress.counts;
            int presses = 1+queuedPress.presses;
            for (int value : queuedPress.nextButton) if (--counts[value] < 0) continue queueLoop;
            if (!visited.add(Arrays.toString(counts))) continue;
            boolean onlyZeros = true;
            for (var count : counts) if (count != 0) { onlyZeros = false; break;}
            if (onlyZeros) return presses;
            String countsString = Arrays.toString(counts);
            buttonLoop: for (int i = 0; i < buttons.size(); i++) {
                var button = buttons.get(i);
                for (var n : button) if (counts[n] == 0) continue buttonLoop;
                if (visited.add(countsString+i)) queue.add(new PressRecord(Arrays.copyOf(counts, width), presses, button, button.length));
            }
        }
        return Integer.MAX_VALUE;
    }

    private static ArrayList<Integer> getEveningButtonComboInts(ArrayList<int[]> buttons, boolean[] goalLights) {
        var ints = new ArrayList<Integer>();
        int width = goalLights.length;
        int buttonCount = buttons.size();
        for (int binaryCombo = 1; binaryCombo < 1<<buttonCount; binaryCombo++) {
            boolean[] state = new boolean[width];
            for (int i = 0; i < buttonCount; i++) {
                if ((binaryCombo & (1 << i)) != 0) {
                    var button = buttons.get(i);
                    for (int j = 0; j < button.length; j++) state[button[j]] = !state[button[j]];
                }
            }
            if (Arrays.equals(state, goalLights)) ints.add(binaryCombo);
        }
        return ints;
    }

    private record PressRecord(int[] counts, int presses, int[] nextButton, int nextButtonLength) implements Comparable<PressRecord> {
        @Override
        public int compareTo(PressRecord o) { return Integer.compare(o.nextButtonLength, nextButtonLength); }

    }

    private static class Machine {
        boolean[] part1GoalLights;
        ArrayList<int[]> buttons;
        int[] goalCounts;

        Machine(boolean[] goalLights, ArrayList<int[]> buttons, int[] goalCounts) {
            this.part1GoalLights = goalLights;
            this.buttons = buttons;
            this.goalCounts = goalCounts;
        }

    }
}
