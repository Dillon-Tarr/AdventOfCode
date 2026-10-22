package year2019;

import shared.Coordinates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

class Day15 {
    static private final int DAY = 15;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private long[] origVals;
    static private char[][] area;
    static private final int startX = 21, startY = 21; // may need adjusted if input program creates larger area
    static private Coordinates oxygenCoordinates;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        runProgramToGenerateAreaMap();
        printArea();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var ss = br.readLine().split(","); origVals = new long[ss.length];
            for (int i = 0; i < ss.length; i++) origVals[i] = Long.parseLong(ss[i]);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void runProgramToGenerateAreaMap() {
        var vals = new ExtendedLongArray(Arrays.copyOf(origVals, origVals.length));
        long pos = 0, relativeBase = 0, inputValue = -1, outputValue;
        // Let's create a full map of the area, running into every wall.
        int y = startY, x = startX;
        area = new char[y<<1][x<<1]; // default value: 0; . - empty; # - wall; O - oxygen;
        var here = new Coordinates(y, x);
        area[y][x] = '.';
        int nY = y-1, sY = y+1, wX = x-1, eX = x+1;
        final int NORTH = 1, SOUTH = 2, WEST = 3, EAST = 4; //input command values
        final int HIT_WALL = 0, MOVED_EMPTY = 1, MOVED_OXYGEN = 2; // status code outputs
        var routeBackStack = new ArrayDeque<Integer>();
        boolean backtrackMode = false;
        programLoop: while (true) {
            var s = Long.toString(vals.get(pos++));
            int sLength = s.length();
            if (sLength < 5) {
                switch (sLength) {
                    case 1 -> s = "0000"+s;
                    case 2 -> s = "000"+s;
                    case 3 -> s = "00"+s;
                    case 4 -> s = "0"+s;
                }
                sLength = 5;
            }
            char lastChar = s.charAt(s.length()-1);
            if (lastChar == '9' && s.charAt(sLength-2) == '9') break;
            char p1Char = s.charAt(sLength-3);
            long p1Pos = switch (p1Char) {
                case '0' -> vals.get(pos++);
                case '1' -> pos++;
                default -> relativeBase+vals.get(pos++);
            };
            switch (lastChar) {
                case '3' -> {
                    if (area[nY][x] == 0) { inputValue = NORTH; backtrackMode = false; }
                    else if (area[sY][x] == 0) { inputValue = SOUTH; backtrackMode = false; }
                    else if (area[y][wX] == 0) { inputValue = WEST; backtrackMode = false; }
                    else if (area[y][eX] == 0) { inputValue = EAST; backtrackMode = false; }
                    else {
                        if (routeBackStack.isEmpty()) { area[y][x] = 'S'; break programLoop; }
                        backtrackMode = true;
                        inputValue = routeBackStack.removeLast();
                    }
                    vals.set(p1Pos, inputValue);
                }
                case '4' -> {
                    outputValue = vals.get(p1Pos);
                    if (outputValue == HIT_WALL) switch ((int) inputValue) {
                        case NORTH -> area[nY][x] = '#';
                        case SOUTH -> area[sY][x] = '#';
                        case WEST -> area[y][wX] = '#';
                        case EAST -> area[y][eX] = '#';
                        default -> throw new IllegalArgumentException("Invalid inputValue "+inputValue);
                    } else {
                        switch ((int) inputValue) {
                            case NORTH -> { y--; nY--; sY--; here.updateY(y); if (!backtrackMode) routeBackStack.add(SOUTH); }
                            case SOUTH -> { y++; nY++; sY++; here.updateY(y); if (!backtrackMode) routeBackStack.add(NORTH); }
                            case WEST -> { x--; wX--; eX--; here.updateX(x); if (!backtrackMode) routeBackStack.add(EAST);}
                            case EAST -> { x++; wX++; eX++; here.updateX(x); if (!backtrackMode) routeBackStack.add(WEST); }
                            default -> throw new IllegalArgumentException("Invalid inputValue "+inputValue);
                        }
                        if (outputValue == MOVED_EMPTY) area[y][x] = '.';
                        else if (outputValue == MOVED_OXYGEN) {
                            if (oxygenCoordinates == null) {
                                oxygenCoordinates = here.copy();
                                area[y][x] = 'O';
                            }
                        } else throw new IllegalArgumentException("Invalid outputValue "+outputValue);
                    }
                }
                case '9' -> relativeBase += vals.get(p1Pos);
                default -> {
                    long p2Val = vals.get(switch (s.charAt(sLength-4)) {
                        case '0' -> vals.get(pos++);
                        case '1' -> pos++;
                        default -> relativeBase+vals.get(pos++);
                    });
                    switch (lastChar) {
                        case '5' -> { if (vals.get(p1Pos) != 0) pos = p2Val; }
                        case '6' -> { if (vals.get(p1Pos) == 0) pos = p2Val; }
                        default -> {
                            long p3Pos = s.charAt(s.length()-5) == '0' ? vals.get(pos++) : relativeBase+vals.get(pos++);
                            switch (lastChar) {
                                case '1' -> vals.set(p3Pos, vals.get(p1Pos)+p2Val);
                                case '2' -> vals.set(p3Pos, vals.get(p1Pos)*p2Val);
                                case '7' -> vals.set(p3Pos, vals.get(p1Pos) < p2Val ? 1 : 0);
                                case '8' -> vals.set(p3Pos, vals.get(p1Pos) == p2Val ? 1 : 0);
                                default -> throw new RuntimeException("Invalid final opCode character: "+lastChar);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Program loop complete.");
    }

    private static void printArea() {
        System.out.println("Area:");
        for (int y = 0; y < area.length; y++) {
            var row = area[y];
            var sb = new StringBuilder();
            for (int x = 0; x < row.length; x++) sb.append(row[x]);
            System.out.println(sb);
        }
    }

    private static void solvePart1() {
        if (oxygenCoordinates == null) throw new RuntimeException("Somehow oxygenCoordinates is null.");
        int[][] fewestSteps = new int[area.length][area[0].length];
        for (var row : fewestSteps) Arrays.fill(row, Integer.MAX_VALUE); // from start point to every reachable point
        var queue = new PriorityQueue<QueuedStep>();
        queue.add(new QueuedStep(startY, startX, 0));
        while (!queue.isEmpty()) {
            var step = queue.remove();
            int stepCount = step.stepCount;
            int y = step.y, x = step.x;
            if (stepCount >= fewestSteps[y][x]) continue;
            int nY = y-1, sY = y+1, wX = x-1, eX = x+1;
            fewestSteps[y][x] = stepCount;
            stepCount++;
            if (area[nY][x] != '#' && fewestSteps[nY][x] > stepCount) queue.add(new QueuedStep(nY, x, stepCount));
            if (area[sY][x] != '#' && fewestSteps[sY][x] > stepCount) queue.add(new QueuedStep(sY, x, stepCount));
            if (area[y][wX] != '#' && fewestSteps[y][wX] > stepCount) queue.add(new QueuedStep(y, wX, stepCount));
            if (area[y][eX] != '#' && fewestSteps[y][eX] > stepCount) queue.add(new QueuedStep(y, eX, stepCount));
        }
        int stepsRequired = fewestSteps[oxygenCoordinates.y][oxygenCoordinates.x];
        System.out.println("\nFewest steps required to move repair droid to oxygen system (part 1 answer): "+stepsRequired);
    }

    private static void solvePart2() {
        if (oxygenCoordinates == null) throw new RuntimeException("Somehow oxygenCoordinates is null.");
        int[][] fewestSteps = new int[area.length][area[0].length]; // from start point to every reachable point
        for (var row : fewestSteps) Arrays.fill(row, Integer.MAX_VALUE);
        var queue = new PriorityQueue<QueuedStep>();
        queue.add(new QueuedStep(oxygenCoordinates.y, oxygenCoordinates.x, 0));
        while (!queue.isEmpty()) {
            var step = queue.remove();
            int stepCount = step.stepCount;
            int y = step.y, x = step.x;
            if (stepCount >= fewestSteps[y][x]) continue;
            int nY = y-1, sY = y+1, wX = x-1, eX = x+1;
            fewestSteps[y][x] = stepCount;
            stepCount++;
            if (area[nY][x] != '#' && fewestSteps[nY][x] > stepCount) queue.add(new QueuedStep(nY, x, stepCount));
            if (area[sY][x] != '#' && fewestSteps[sY][x] > stepCount) queue.add(new QueuedStep(sY, x, stepCount));
            if (area[y][wX] != '#' && fewestSteps[y][wX] > stepCount) queue.add(new QueuedStep(y, wX, stepCount));
            if (area[y][eX] != '#' && fewestSteps[y][eX] > stepCount) queue.add(new QueuedStep(y, eX, stepCount));
        }
        int mostSteps = -1;
        for (var row : fewestSteps) for (var x : row) if (x != Integer.MAX_VALUE && x > mostSteps) mostSteps = x;
        System.out.println("\nNumber of minutes required to fill the area with oxygen (part 2 answer): "+mostSteps);
    }

    private record QueuedStep(int y, int x, int stepCount) implements Comparable<QueuedStep> {
        @Override
        public int compareTo(QueuedStep o) {
            return Integer.compare(stepCount, o.stepCount);
        }
    }

    private static class ExtendedLongArray {
        long[] baseArray;
        int baseLength;
        HashMap<Long, Long> extendedMap = new HashMap<>();

        ExtendedLongArray(long[] baseArray) {
            this.baseArray = baseArray;
            baseLength = baseArray.length;
        }

        private void set(long index, long value) {
            if (index < 0 || index >= baseLength) extendedMap.put(index, value);
            else baseArray[(int)index] = value;
        }

        private long get(long index) {
            return index < 0 || index >= baseLength ? extendedMap.getOrDefault(index, 0L) : baseArray[(int)index];
        }

    }

}
