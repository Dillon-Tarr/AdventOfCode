package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day18 {
    static private final int DAY = 18;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private char[][] map;
    static private int height, width;
    /**Stores the coordinates of each key (a-z) plus the start point (@) at index 26 as: y, b*/
    static private final int[][] keyCoordinates = new int[27][2]; // index 26 for start point
    /**Stores distance and door requirements between the points where key a and key b reside*/
    static private final int[][][] keyAKeyBData = new int[27][27][2];

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        calculateDistancesAndDoors();
        solve();

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
        map = new char[inputRows.size()][];
        Arrays.fill(keyCoordinates, new int[]{-1, -1}) ;
        for (int i = 0; i < map.length; i++) map[i] = inputRows.get(i).toCharArray();
        height = map.length;
        width = map[0].length;
        for (int y = 1; y < height-1; y++) {
            var row = map[y];
            for (int x = 1; x < width-1; x++) {
                char c = row[x];
                switch(c) {
                    case '@' -> keyCoordinates[26] = new int[]{y, x};
                    case '#', '.' -> {}
                    default -> { if (c >= 'a' && c <= 'z') keyCoordinates[c-'a'] = new int[]{y, x}; }
                }
            }
        }
    }

    private static void calculateDistancesAndDoors() {
        for (int a = 0; a < 27; a++) for (int b = 0; b < 27; b++) {
            if (a == b) {
                keyAKeyBData[a][a] = new int[]{0, 0};
                continue;
            }
            int[] data = distanceAndDoorsBetween(a, b);
            keyAKeyBData[a][b] = data;
            keyAKeyBData[b][a] = data;
        }
    }

    private static int[] distanceAndDoorsBetween(int keyA, int keyB) {
        int[] aCoords = keyCoordinates[keyA], bCoords = keyCoordinates[keyB];
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
        throw new RuntimeException("This should never happen, since we walk through all doors and other keys.");
    }

    private record QueuedStep(int y, int x, int steps, int doorsMask) implements Comparable<QueuedStep> {
        @Override
        public int compareTo(QueuedStep o) { return Integer.compare(steps, o.steps); }
    }

    private static void solve() {
        int allKeysValue = (1<<26)-1, fastestTrip = Integer.MAX_VALUE;
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
            destinationLoop: for (int b = 0; b < 26; b++) {
                int bPosValue = 1<<b;
                if ((keysMask & bPosValue) != 0) continue; // <-- continue if (we already have the destination key)
                int[] data = keyAData[b]; int doorsMask = data[1];
                for (int d = 0; d < 26; d++) {
                    int doorPosVal = 1<<d;
                    if (((doorsMask & doorPosVal) != 0) && ((keysMask & doorPosVal) == 0)) continue destinationLoop;
                } // ^^ continue with next destination if (any key d is required and we don't have it) ^^
                int newSteps = data[0];
                queue.add(new QueuedLeap(new TripState(b, keysMask | bPosValue), steps+newSteps));
            }
        }
        System.out.println("All keys obtained. Fastest trip: "+fastestTrip);
    }
    
    private record QueuedLeap(TripState tripState, int steps) implements  Comparable<QueuedLeap> {
        @Override
        public int compareTo(QueuedLeap o) { return Integer.compare(steps, o.steps); }
    }

    private record TripState(int a, int keysMask) {}

}
