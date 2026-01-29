package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day7 {
    static private final int DAY = 7;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private char[][] map;
    static private int height, width, startX;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        var inputStrings = new ArrayList<String>();
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
        int splitCount = 0, y = 0;
        long[] beamCountByXIndex = new long[width];
        beamCountByXIndex[startX] = 1;
        while ((y+=2) < height) {
            char[] row = map[y];
            for (int x = 0; x < width; x++) {
                if (row[x] == '^') {
                    long beamCount = beamCountByXIndex[x];
                    if (beamCount > 0) {
                        splitCount++;
                        beamCountByXIndex[x] = 0;
                        beamCountByXIndex[x-1] += beamCount;
                        beamCountByXIndex[x+1] += beamCount;
                    }
                }
            }
        }
        long timelineCount = 0;
        for (int x = 0; x < width; x++) timelineCount += beamCountByXIndex[x];
        System.out.println("\nSplit count (part 1 answer): "+splitCount+
                "\n\nTimeline count (part 2 answer): "+timelineCount);
    }

}
