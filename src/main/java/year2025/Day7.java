package year2025;

import shared.Coordinates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

class Day7 {
    static private final int DAY = 7;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private char[][] map;
    static private int height, startX;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        var inputStrings = new ArrayList<String>();
        int width;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            width = s.length();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        height = inputStrings.size();
        map = new char[height][width];
        for (int y = 0; y < height; y++) map[y] = inputStrings.get(y).toCharArray();
        startX = inputStrings.getFirst().indexOf('S');
    }

    private static void solve() {
        int splitCount = 0;
        var emitPoints = new ArrayDeque<Coordinates>();
        emitPoints.add(new Coordinates(1, startX));
        HashSet<String> splitPoints = new HashSet<>();
        while (!emitPoints.isEmpty()) {
            var point = emitPoints.removeLast();
            int y = point.y, x = point.x;
            while (++y < height) {
                if (map[y][x] == '^') {
                    String s = y+","+x;
                    if (splitPoints.add(s)) {
                        splitCount++;
                        if (map[y][x-1] == '.') emitPoints.add(new Coordinates(y, x-1));
                        if (map[y][x+1] == '.') emitPoints.add(new Coordinates(y, x+1));
                    }
                    break;
                }
            }
        }
        System.out.println("\nSplit count (part 1 answer): "+splitCount);
    }

}
