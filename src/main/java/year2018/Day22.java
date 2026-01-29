package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

class Day22 {
    static private final int DAY = 22;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private int DEPTH, TARGET_Y, TARGET_X;
    static private int[][] regions;
    static private int[][][] yXEquipMinRecords;
    static private final Integer[][] equipSwapArray
            = new Integer[][] { { null, 2, 1 }, { 2, null, 0 }, { 1, 0, null } };
    // region types: 0, 1, 2 (rocky, wet, narrow); equipment types: 0, 1, 2 (none, torch, climbingGear);
    // allowed equipment types by region type (region > equipment):
    // 0 > 1, 2; 1 > 0, 2; 2 > 0, 1

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        prepare();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            DEPTH =  Integer.parseInt(s.substring(s.lastIndexOf(' ')+1));
            s = br.readLine();
            var ss = s.substring(s.lastIndexOf(' ')+1).split(",");
            TARGET_Y = Integer.parseInt(ss[1]);
            TARGET_X = Integer.parseInt(ss[0]);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void prepare() {
        int FAR_BOUNDARY = Math.max(TARGET_Y, TARGET_X)*6/5; // == the greater target dimension + 20%
        var erosionLevels = new int[FAR_BOUNDARY + 1][FAR_BOUNDARY + 1];
        regions = new int[FAR_BOUNDARY + 1][FAR_BOUNDARY + 1];
        erosionLevels[0][0] = erosionLevels[TARGET_Y][TARGET_X] = calculateErosionLevel(0);
        for (int x = 1; x <= FAR_BOUNDARY; x++) {
            int level = calculateErosionLevel(x * 16807);
            erosionLevels[0][x] = level; regions[0][x] = level % 3;
        }
        for (int y = 1; y <= FAR_BOUNDARY; y++) {
            int level = calculateErosionLevel(y * 48271);
            erosionLevels[y][0] = level; regions[y][0] = level % 3;
        }
        for (int y = 1; y <= FAR_BOUNDARY; y++) {
            int leftLevel = erosionLevels[y][0];
            if (y == TARGET_Y) {
                for (int x = 1; x < TARGET_X; x++) {
                    leftLevel = calculateErosionLevel(leftLevel * erosionLevels[y-1][x]);
                    erosionLevels[y][x] = leftLevel; regions[y][x] = leftLevel % 3;
                }
                leftLevel = erosionLevels[0][0];
                for (int x = TARGET_X+1; x <= FAR_BOUNDARY; x++) {
                    leftLevel = calculateErosionLevel(leftLevel * erosionLevels[y-1][x]);
                    erosionLevels[y][x] = leftLevel; regions[y][x] = leftLevel % 3;
                }
            } else {
                for (int x = 1; x <= FAR_BOUNDARY; x++) {
                    leftLevel = calculateErosionLevel(leftLevel * erosionLevels[y-1][x]);
                    erosionLevels[y][x] = leftLevel; regions[y][x] = leftLevel % 3;
                }
            }
        }
        yXEquipMinRecords = new int[FAR_BOUNDARY +1][FAR_BOUNDARY+1][3];
        for (int y = 0; y <= FAR_BOUNDARY; y++) {
            for (int x = 0; x <= FAR_BOUNDARY; x++) {
                var records = yXEquipMinRecords[y][x];
                records[0] = 99999; records[1] = 99999; records[2] = 99999;
            }
        }
    }

    private static int calculateErosionLevel(int geologicIndex) { return (geologicIndex + DEPTH) % 20183; }

    private static void solvePart1() {
        int sum = 0;
        for (int y = 0; y <= TARGET_Y; y++) for (int x = 0; x <= TARGET_X; x++) sum += regions[y][x];
        System.out.println("\nRisk level sum (part 1 answer): "+ sum);
    }

    private static void solvePart2() {
        int fewestMins = 99999;
        var queue = new PriorityQueue<TravelRecord>(Comparator.comparingInt(r -> r.minutes));
        queue.add(new TravelRecord(0, 0, 0, 1, 0));
        while (!queue.isEmpty()) {
            var travelRecord = queue.poll();
            int mins = travelRecord.minutes;
            if (mins >= fewestMins) break;
            int y = travelRecord.y, x = travelRecord.x, equip = travelRecord.equipmentType;
            if (y == TARGET_Y && x == TARGET_X) {
                if (equip != 1) mins += 7;
                if (mins < fewestMins) fewestMins = mins;
                continue;
            }
            int swEquip = equipSwapArray[travelRecord.regionType][equip], swMins = mins + 7;
            var hereRecords = yXEquipMinRecords[y][x];
            if (mins >= hereRecords[equip]) continue;
            hereRecords[equip] = mins;
            if (swMins < hereRecords[swEquip]) hereRecords[swEquip] = swMins;
            queue.add(newRecord(++y, x, equip, ++mins, swEquip, ++swMins)); // south
            if ((y -= 2) >= 0) queue.add(newRecord(y, x, equip, mins, swEquip, swMins)); // north
            queue.add(newRecord(++y, ++x, equip, mins, swEquip, swMins)); // east
            if ((x -= 2) > 0) queue.add(newRecord(y, x, equip, mins, swEquip, swMins)); // west
        }
        System.out.println("\nFewest minutes needed to rescue friend (part 2 answer): "+ fewestMins);
    }

    private record TravelRecord(int y, int x, int regionType, int equipmentType, int minutes){}

    private static TravelRecord newRecord(int y, int x, int equip, int mins, int swEquip, int swMins) {
        int region = regions[y][x];
        return region == equip ? new TravelRecord(y, x, region, swEquip, swMins)
                : new TravelRecord(y, x, region, equip, mins);
    }

}
