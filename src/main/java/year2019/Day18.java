package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day18 {
    static private final int DAY = 18;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private char[][] falseMap, trueMap;
    static private int height, width;
    static private final int allKeysValue = (1<<26)-1;
    /**Stores the coordinates of each key (a-z) plus the start point (@) at index 26 as: y, b*/
    static private final int[][] keyCoordinates = new int[27][2];
    /**Stores key coordinates and the coordinates of the true start points*/
    static private final int[][] trueKeyCoordinates = new int[30][2];
    /**Stores distance and door requirements between the points where key a and key b reside*/
    static private final int[][][] keyAKeyBData = new int[27][27][2];
    /**Same function as keyAKeyBData, but for the true map. Indices 26-29 represent
     * the top-left, top-right, bottom-left, and bottom-right entrances successively.*/
    static private final int[][][] trueKeyAKeyBData = new int[30][30][2];

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        calculateDistancesAndDoors();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        var inputRows = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var s = br.readLine();
            while (s != null) {
                inputRows.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        Arrays.fill(keyCoordinates, new int[]{-1, -1});
        height = inputRows.size();
        falseMap = new char[height][]; trueMap = new char[height][];
        for (int i = 0; i < falseMap.length; i++) {
            var charArray = inputRows.get(i).toCharArray();
            falseMap[i] = charArray; trueMap[i] = charArray.clone();
        }
        width = falseMap[0].length;
        for (int y = 1; y < height-1; y++) {
            var row = falseMap[y];
            for (int x = 1; x < width-1; x++) {
                char c = row[x];
                switch(c) {
                    case '@' -> keyCoordinates[26] = new int[]{y, x};
                    case '#', '.' -> {}
                    default -> {
                        if (c >= 'a' && c <= 'z') {
                            int index = c-'a';
                            keyCoordinates[index] = new int[]{y, x};
                            trueKeyCoordinates[index] = new int[]{y, x};
                        }
                    }
                }
            }
        }
        var startCoords = keyCoordinates[26];
        int startY = startCoords[0], startX = startCoords[1], i = 0;
        char[] correction = new char[]{'@','#','@','#','#','#','@','#','@'};
        for (int y = startY-1; y <= startY+1 ; y++)    // ... -> @#@
            for (int x = startX-1; x <= startX+1; x++) // .@. -> ###
                trueMap[y][x] = correction[i++];       // ... -> @#@
        trueKeyCoordinates[26] = new int[]{startY-1, startX-1};
        trueKeyCoordinates[27] = new int[]{startY-1, startX+1};
        trueKeyCoordinates[28] = new int[]{startY+1, startX-1};
        trueKeyCoordinates[29] = new int[]{startY+1, startX+1};
    }

    private static void calculateDistancesAndDoors() {
        for (int a = 0; a < 27; a++) for (int b = 0; b < 27; b++) {
            if (a == b) {
                keyAKeyBData[a][a] = new int[]{0, 0};
                continue;
            }
            int[] data = distanceAndDoorsBetween(a, b, keyCoordinates, falseMap);
            if (data == null) throw new RuntimeException("This should never happen, since we walk through all doors and other keys.");
            keyAKeyBData[a][b] = data;
            keyAKeyBData[b][a] = data;
        }
        for (int a = 0; a < 30; a++) {
            for (int b = 0; b < 30; b++) {
                if (a == b) {
                    trueKeyAKeyBData[a][a] = new int[]{0, 0};
                    continue;
                }
                int[] data = distanceAndDoorsBetween(a, b, trueKeyCoordinates, trueMap);
                trueKeyAKeyBData[a][b] = data;
                trueKeyAKeyBData[b][a] = data;
            }
        }

    }

    private static int[] distanceAndDoorsBetween(int keyA, int keyB, int[][] coordinates, char[][] map) {
        int[] aCoords = coordinates[keyA], bCoords = coordinates[keyB];
        int aY = aCoords[0], aX = aCoords[1], bY = bCoords[0], bX = bCoords[1];
        int heightLimit = height - 3;
        int widthLimit = width - 3;
        int[][] fewestSteps = new int[height][width];
        for (var row : fewestSteps) Arrays.fill(row, Integer.MAX_VALUE);
        fewestSteps[aY][aX] = 0;
        var queue = new PriorityQueue<QueuedStep>();
        queue.add(new QueuedStep(aY, aX, 0, 0));
        while (!queue.isEmpty()) {
            var e = queue.remove();
            int y = e.y, x = e.x, steps = e.steps+1, doorsMask = e.doorsMask, nY = y-1, sY = y+1, wX = x-1, eX = x+1, oY = -1, oX = -1;
            if ((y == bY && (wX == bX || eX == bX)) || (x == bX && (nY == bY || sY == bY))) return new int[]{steps, doorsMask};
            for (int i = 0; i < 4; i++) {
                switch(i) {
                    case 0 -> { if (y < 2) continue; oY = nY; oX = x; }
                    case 1 -> { if (y > heightLimit) continue; oY = sY; oX = x; }
                    case 2 -> { if (x < 2) continue; oY = y; oX = wX; }
                    case 3 -> { if (x > widthLimit) continue; oY = y; oX = eX; }
                }
                char c = map[oY][oX];
                if ((c != '#') && steps < fewestSteps[oY][oX]) {
                    fewestSteps[oY][oX] = steps;
                    queue.add(new QueuedStep(oY, oX, steps, c >= 'A' && c <= 'Z' ? (doorsMask | 1<<(c-'A')) : doorsMask));
                }
            }
        }
        return null;
    }

    private record QueuedStep(int y, int x, int steps, int doorsMask) implements Comparable<QueuedStep> {
        @Override
        public int compareTo(QueuedStep o) { return Integer.compare(steps, o.steps); }
    }

    private static void solvePart1() {
        int fastestTrip = Integer.MAX_VALUE;
        var stateToBestSteps = new HashMap<TripState, Integer>();
        var queue = new PriorityQueue<QueuedLeap>();
        for (int b = 0; b < 26; b++) {
            int[] data = keyAKeyBData[26][b]; int steps = data[0], doorsMask = data[1];
            if (doorsMask == 0) queue.add(new QueuedLeap(new TripState(b, 0), steps));
        }
        while (!queue.isEmpty()) {
            var trip = queue.remove();
            var tripState = trip.tripState;
            int steps = trip.steps, keysMask = tripState.keysMask;
            if (keysMask == allKeysValue) {
                fastestTrip = steps;
                break;
            }
            Integer bestSteps = stateToBestSteps.get(tripState);
            if (bestSteps == null || steps < bestSteps) stateToBestSteps.put(tripState, steps);
            else continue;
            int[][] keyAData = keyAKeyBData[tripState.a];
            for (int b = 0; b < 26; b++) {
                int bPosValue = 1<<b;
                if ((keysMask & bPosValue) != 0) continue; // <-- continue if (we already have the destination key)
                int[] data = keyAData[b]; int doorsMask = data[1];
                if ((doorsMask & keysMask) != doorsMask) continue;
                // ^^ continue with next destination if (missing any required key) ^^
                queue.add(new QueuedLeap(new TripState(b, keysMask | bPosValue), steps+data[0]));
            }
        }
        System.out.println("All keys obtained on fictional map. Fastest trip (part 1 answer): "+fastestTrip);
    }
    
    private record QueuedLeap(TripState tripState, int steps) implements  Comparable<QueuedLeap> {
        @Override
        public int compareTo(QueuedLeap o) { return Integer.compare(steps, o.steps); }
    }

    private record TripState(int a, int keysMask) {}

    private static void solvePart2() {
        int[] keyToRobotNumber = new int[26];
        for (int a = 0; a < 26; a++) {
            var keyData = trueKeyAKeyBData[a];
            if (keyData[26] != null) keyToRobotNumber[a] = 26;
            else if (keyData[27] != null) keyToRobotNumber[a] = 27;
            else if (keyData[28] != null) keyToRobotNumber[a] = 28;
            else if (keyData[29] != null) keyToRobotNumber[a] = 29;
            else throw new RuntimeException("Invalid keyData");
        }
        int fastestTrip = Integer.MAX_VALUE;
        var stateToBestSteps = new HashMap<TripStateV2, Integer>();
        var queue = new PriorityQueue<QueuedLeapV2>();
        queue.add(new QueuedLeapV2(new TripStateV2(26, 27, 28, 29, 0), 0));
        while (!queue.isEmpty()) {
            var trip = queue.remove();
            var tripState = trip.tripState;
            int steps = trip.steps, keysMask = tripState.keysMask;
            Integer currentBest = stateToBestSteps.get(tripState);
            if (currentBest != null && steps > currentBest) continue;
            if (keysMask == allKeysValue) {
                fastestTrip = steps;
                break;
            }
            for (int b = 0; b < 26; b++) {
                int bPosValue = 1<<b;
                if ((keysMask & bPosValue) != 0) continue; // <-- continue if (we already have the destination key)
                int robotNumber = keyToRobotNumber[b];
                int a26 = tripState.a26, a27 = tripState.a27, a28 = tripState.a28, a29 = tripState.a29;
                int robotPosition = switch(robotNumber) {
                    case 26 -> a26;
                    case 27 -> a27;
                    case 28 -> a28;
                    case 29 -> a29;
                    default -> throw new RuntimeException("Invalid keyData");
                };
                var data = trueKeyAKeyBData[robotPosition][b]; int doorsMask = data[1];
                if ((doorsMask & keysMask) != doorsMask) continue;
                // ^^ continue with next destination if (missing any required key) ^^
                switch (robotNumber) {
                    case 26 -> a26 = b;
                    case 27 -> a27 = b;
                    case 28 -> a28 = b;
                    case 29 -> a29 = b;
                }
                var newState = new TripStateV2(a26, a27, a28, a29, keysMask | bPosValue);
                int newSteps = steps+data[0];
                Integer bestSteps = stateToBestSteps.get(newState);
                if (bestSteps == null || newSteps < bestSteps) {
                    stateToBestSteps.put(newState, newSteps);
                    queue.add(new QueuedLeapV2(newState, newSteps));
                }
            }
        }
        System.out.println("All keys obtained on true map. Fastest trip (part 2 answer): "+fastestTrip);
    }

    private record QueuedLeapV2(TripStateV2 tripState, int steps) implements  Comparable<QueuedLeapV2> {
        @Override
        public int compareTo(QueuedLeapV2 o) { return Integer.compare(steps, o.steps); }
    }

    private record TripStateV2(int a26, int a27, int a28, int a29, int keysMask) {}

}
