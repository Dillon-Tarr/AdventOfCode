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

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
//        solvePart2();

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
        for (var machine : machines) sum += getFewestPressesToReachGoalState(machine, machine.goalLights);
        System.out.println("\nPart 1 presses sum: "+sum);
    }

    private static int getFewestPressesToReachGoalState(Machine machine, boolean[] goalLights) {
        int width = goalLights.length;
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

//    private static void solvePart2() {
//        int sum = 0;
//        for (var machine : machines) {
//            int width = machine.goalCounts.length, buttonCount  = machine.buttons.size(), presses = 0;
//            var part2GoalLights = new boolean[machine.goalCounts.length];
//            for (int i = 0; i < machine.goalCounts.length; i++) { part2GoalLights[i] = machine.goalCounts[i] % 2 == 1; }
//            var buttonOptionsToGetGoalLights = new ArrayList<boolean[]>();
//
//            var stack = new ArrayDeque<QueuedPart1Press>();
//            for (int i = 0; i < buttons.size(); i++)
//                stack.add(new QueuedPart1Press(new boolean[width], 0, i));
//            stack.add(new QueuedPart1Press(new boolean[width], 0, 0));
//            while (!stack.isEmpty()) {
//                var queuedPress = stack.pop();
//            }
//
//            boolean[] goalLights = machine.goalLights;
//            ArrayList<int[]> buttons = machine.buttons;
//
//            var queue = new PriorityQueue<QueuedPart1Press>();
//            for (int i = 0; i < buttons.size(); i++)
//                queue.add(new QueuedPart1Press(new boolean[goalLights.length], 0, i));
//            while (!queue.isEmpty()) {
//                var queuedPress = queue.poll();
//                var newState = queuedPress.state;
//                presses = 1+queuedPress.presses;
//                int nextButtonIndex = queuedPress.nextButtonIndex;
//                var button =  buttons.get(nextButtonIndex);
//                for (var index : button) newState[index] = !newState[index];
//                if (Arrays.equals(newState, goalLights)) break;
//                for (int i = 0; i < buttons.size(); i++)
//                    queue.add(new QueuedPart1Press(Arrays.copyOf(newState, newState.length), presses, i));
//            }
//            sum += presses;
//        }
//        System.out.println("\nPart 2 presses sum: "+sum);
//    }

    private record QueuedPart2Press(int[] state, int presses, int nextButtonIndex, int stateSum) implements Comparable<QueuedPart2Press> {

        @Override
        public int compareTo(QueuedPart2Press o) {
            if (presses < o.presses) return -1;
            else if (presses == o.presses) return Integer.compare(o.stateSum, stateSum);
            else return 1;
        }

    }

    private static class Machine {
        boolean[] goalLights;
        ArrayList<int[]> buttons;
        int[] goalCounts;

        Machine(boolean[] goalLights, ArrayList<int[]> buttons, int[] goalCounts) {
            this.goalLights = goalLights;
            this.buttons = buttons;
            this.goalCounts = goalCounts;
        }

    }
}
