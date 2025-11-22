package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

class Day15 {
    static private final int DAY = 15;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private boolean[][] openTiles;
    static private final ArrayList<Unit> units = new ArrayList<>(), elves = new ArrayList<>(), goblins = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        ArrayList<char[]> inputChars = new ArrayList<>(); int mapWidth;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            mapWidth = s.length();
            while (s != null) {
                inputChars.add(s.toCharArray());
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        int mapHeight = inputChars.size();
        openTiles = new boolean[mapHeight][];
        for (int y = 0; y < mapHeight; y++) {
            char[] rowChars = inputChars.get(y);
            boolean[] rowTilesSteppable = new boolean[mapWidth];
            openTiles[y] = rowTilesSteppable;
            for (int x = 0; x < mapWidth; x++) {
                char inputChar = rowChars[x];
                switch (inputChar) {
                    case '.' -> rowTilesSteppable[x] = true;
                    case '#' -> {}
                    default -> units.add(new Unit(inputChar == 'E', y, x));
                }
            }
        }
        for (var unit : units) if (unit.isElf) elves.add(unit); else goblins.add(unit);
    }

    private static void solve() {
        int roundsCompleted = 0; //y, x;
//        String typeString;
        mainLoop: while (true) {
//            System.out.println("Rounds completed: "+roundsCompleted+"; Current map:");
//            printMap();
            units.sort(Unit::compareTo);
            for (int u1 = 0; u1 < units.size(); u1++) {
                Unit unit = units.get(u1);
                if (unit.isElf ? goblins.isEmpty() : elves.isEmpty()) break mainLoop;
//                y = unit.y; x = unit.x; typeString = unit.typeString;
//                System.out.println("Turn of "+typeString+" at "+y+","+x+"; ");
                Unit target = unit.getAdjacentTarget();
                if (target == null) {
                    unit.move();
                    target = unit.getAdjacentTarget();
                }
                if (target == null) continue;
                if (unit.attackAndReturnWhetherDefeated(target)){
                    if (units.indexOf(target) < u1) u1--;
                    units.remove(target);
                }
            }
            roundsCompleted++;
        }
        System.out.println("The last "+(goblins.isEmpty()?"Goblin":"Elf")+" falls.\n");
        System.out.println("Rounds completed: "+roundsCompleted+"; Final map:");
        printMap();
        // (Combat only ends when a unit finds no targets during its turn.)
        // outcome = (number of FULL rounds that were completed)*(the sum of the hit points of all remaining units)
        int hpSum = 0;
        for (var unit : units) {
            System.out.println(unit.typeString+" at "+unit.y+","+unit.x+" has "+unit.hp+" hp remaining.");
            hpSum += unit.hp;
        }
        System.out.println("hp sum: "+hpSum+"; Rounds completed: "+roundsCompleted);
        int outcome = roundsCompleted*hpSum;
        System.out.println("Outcome (part 1 answer): "+outcome);
    }

    static private boolean getWhetherThereIsAPossiblePath(int startY, int startX, int goalY, int goalX) {
        if (startY == goalY && startX == goalX) return true;
        PriorityQueue<QueuedSteplessAttempt> queue = new PriorityQueue<>();
        queue.add(new QueuedSteplessAttempt(startY, startX, goalY, goalX));
        HashSet<String> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            var attempt = queue.poll(); int y = attempt.cY, x = attempt.cX, nY = y-1, wX = x-1, eX = x+1, sY = y+1;
            if ((y == goalY && (wX == goalX || eX == goalX)) || (x == goalX && (nY == goalY || sY == goalY))) return true;
            if (openTiles[nY][x] && visited.add(nY+","+x)) queue.add(new QueuedSteplessAttempt(nY, x, goalY, goalX));
            if (openTiles[y][wX] && visited.add(y+","+wX)) queue.add(new QueuedSteplessAttempt(y, wX, goalY, goalX));
            if (openTiles[y][eX] && visited.add(y+","+eX)) queue.add(new QueuedSteplessAttempt(y, eX, goalY, goalX));
            if (openTiles[sY][x] && visited.add(sY+","+x)) queue.add(new QueuedSteplessAttempt(sY, x, goalY, goalX));
        }
        return false;
    }

    static private class QueuedSteplessAttempt implements Comparable<QueuedSteplessAttempt>{
        int cY, cX, gY, gX, manhattan;

        QueuedSteplessAttempt(int currentY, int currentX, int goalY, int goalX) {
            gY = goalY; gX = goalX; cY = currentY; cX = currentX;
            manhattan = Math.abs(this.cY - this.gY)+Math.abs(this.cX - this.gX);
        }

        public int compareTo(QueuedSteplessAttempt o) { return Integer.compare(this.manhattan, o.manhattan); }

    }

    static private int getShortestPathDistance(int startY, int startX, int goalY, int goalX) {
        HashSet<String> visitCache = new HashSet<>();
        if (startY == goalY && startX == goalX) return 0;
        PriorityQueue<QueuedReachAttempt> queue = new PriorityQueue<>();
        HashSet<String> visited = new HashSet<>(); visited.add(startY+","+startX);
        queue.add(new QueuedReachAttempt(startY, startX, goalY, goalX, 0, visited));
        while (!queue.isEmpty()) {
            var attempt = queue.poll(); int y = attempt.currentY, x = attempt.currentX,
                    nY = y-1, wX = x-1, eX = x+1, sY = y+1, newStepCount = attempt.stepsTaken+1;
            if (y == goalY && x == goalX) return attempt.stepsTaken;
            if ((y == goalY && (wX == goalX || eX == goalX)) || (x == goalX && (nY == goalY || sY == goalY)))
                return newStepCount;
            visited = attempt.visited;
            if (openTiles[nY][x]) { String s = nY+","+x;
                if (!visited.contains(s) && visitCache.add(s+newStepCount)) { var newVisited = new HashSet<>(visited); newVisited.add(s);
                    queue.add(new QueuedReachAttempt(nY, x, goalY, goalX, newStepCount, newVisited)); } }
            if (openTiles[y][wX]) { String s = y+","+wX;
                if (!visited.contains(s) && visitCache.add(s+newStepCount)) { var newVisited = new HashSet<>(visited); newVisited.add(s);
                    queue.add(new QueuedReachAttempt(y, wX, goalY, goalX, newStepCount, newVisited)); } }
            if (openTiles[y][eX]) { String s = y+","+eX;
                if (!visited.contains(s) && visitCache.add(s+newStepCount)) { var newVisited = new HashSet<>(visited); newVisited.add(s);
                    queue.add(new QueuedReachAttempt(y, eX, goalY, goalX, newStepCount, newVisited)); } }
            if (openTiles[sY][x]) { String s = sY+","+x;
                if (!visited.contains(s) && visitCache.add(s+newStepCount)) { var newVisited = new HashSet<>(visited); newVisited.add(s);
                    queue.add(new QueuedReachAttempt(sY, x, goalY, goalX, newStepCount, newVisited)); } }
        }
        throw new RuntimeException("You didn't check that this was even reachable first.");
    }

    private record QueuedReachAttempt(int currentY, int currentX, int goalY, int goalX, int stepsTaken,
                                      HashSet<String> visited) implements Comparable<QueuedReachAttempt>{

        public int compareTo(QueuedReachAttempt o) { return Integer.compare(stepsTaken, o.stepsTaken); }

    }

    private static class Unit implements Comparable<Unit> {
        boolean isElf;
        int y, x, nY, sY, wX, eX, hp = 200; // power = 3
        ArrayList<Unit> opponents;
        String typeString;

        Unit(boolean isElf, int y, int x) {
            this.isElf = isElf;
            typeString = isElf?"Elf":"Goblin";
            this.y = y; nY = y-1; sY = y+1;
            this.x = x; wX = x-1; eX = x+1;
            opponents = isElf ? goblins : elves;
        }

        Unit getAdjacentTarget() {
            Unit n = null, w = null, e = null, s = null;
            int nhp = Integer.MAX_VALUE, whp = Integer.MAX_VALUE, ehp = Integer.MAX_VALUE, shp;
            for (var o : opponents) {
                if (x == o.x) { if (o.y == nY) n = o; else if (o.y == sY) s = o; }
                else if (y == o.y) { if (o.x == wX) w = o; else if (o.x == eX) e = o; }
            }
            int lowestHP = Integer.MAX_VALUE;
            if (n != null) {
                nhp = n.hp;
                lowestHP = nhp;
            }
            if (w != null) {
                whp = w.hp;
                if (whp < lowestHP) lowestHP = whp;
            }
            if (e != null) {
                ehp = e.hp;
                if (ehp < lowestHP) lowestHP = ehp;
            }
            if (s != null) {
                shp = s.hp;
                if (shp < lowestHP) lowestHP = shp;
            }
            if (n != null && nhp == lowestHP) return n;
            if (w != null && whp == lowestHP) return w;
            if (e != null && ehp == lowestHP) return e;
            return s; // if s is null, there's no adjacent target
        }

        boolean attackAndReturnWhetherDefeated(Unit target) {
            target.hp -= 3;
//            System.out.println(typeString+" attacks "+target.typeString+"; hp remaining: "+target.hp);
            if (target.hp <= 0) {
                opponents.remove(target);
                openTiles[target.y][target.x] = true;
                return true;
            }
            return false;
        }

        void move() {
            ArrayList<Unit> reachableOpponents = new ArrayList<>();
            for (var o : opponents) if (getWhetherThereIsAPossiblePath(y, x, o.y, o.x)) reachableOpponents.add(o);
            if (reachableOpponents.isEmpty()) {
//                System.out.println(typeString+" finds no reachable opponents. No move made.");
                return;
            }
            ArrayList<int[]> reachableTargetTileData = new ArrayList<>();
            for (var o : reachableOpponents) {
                int oY = o.y, oX = o.x, oNY = oY-1, oWX = oX-1, oEX = oX+1, oSY = oY+1;
                if (openTiles[oNY][oX] && getWhetherThereIsAPossiblePath(y, x, oNY, oX))
                    reachableTargetTileData.add(new int[]{oNY, oX, getShortestPathDistance(y, x, oNY, oX)});
                if (openTiles[oY][oWX] && getWhetherThereIsAPossiblePath(y, x, oY, oWX))
                    reachableTargetTileData.add(new int[]{oY, oWX, getShortestPathDistance(y, x, oY, oWX)});
                if (openTiles[oY][oEX] && getWhetherThereIsAPossiblePath(y, x, oY, oEX))
                    reachableTargetTileData.add(new int[]{oY, oEX, getShortestPathDistance(y, x, oY, oEX)});
                if (openTiles[oSY][oX] && getWhetherThereIsAPossiblePath(y, x, oSY, oX))
                    reachableTargetTileData.add(new int[]{oSY, oX, getShortestPathDistance(y, x, oSY, oX)});
            }
            int closestTargetDistance = Integer.MAX_VALUE;
            for (var data : reachableTargetTileData) if (data[2] < closestTargetDistance) closestTargetDistance = data[2];
            ArrayList<int[]> closestTargetsData = new ArrayList<>();
            for (var data : reachableTargetTileData) if (data[2] == closestTargetDistance) closestTargetsData.add(data);
            if (closestTargetsData.size() != 1) {
                int lowY = Integer.MAX_VALUE;
                for (var data : closestTargetsData) if (data[0] < lowY) lowY = data[0];
                for (int i = 0; i < closestTargetsData.size(); i++)
                    if (closestTargetsData.get(i)[0] != lowY) closestTargetsData.remove(i--);
            }
            if (closestTargetsData.size() != 1) {
                int lowX = Integer.MAX_VALUE;
                for (var data : closestTargetsData) if (data[1] < lowX) lowX = data[1];
                for (int i = 0; i < closestTargetsData.size(); i++)
                    if (closestTargetsData.get(i)[1] != lowX) closestTargetsData.remove(i--);
            }
            int[] targetData = closestTargetsData.getFirst();
            int targetY = targetData[0], targetX = targetData[1], closestDistanceMinusOne = targetData[2]-1;
            if (openTiles[nY][x] && getWhetherThereIsAPossiblePath(nY, x, targetY, targetX) &&
                    getShortestPathDistance(nY, x, targetY, targetX) == closestDistanceMinusOne) {
//                System.out.println(typeString+" moving north.");
                openTiles[y][x] = true; y--; nY--; sY--; openTiles[y][x] = false;
            } else if (openTiles[y][wX] && getWhetherThereIsAPossiblePath(y, wX, targetY, targetX) &&
                    getShortestPathDistance(y, wX, targetY, targetX) == closestDistanceMinusOne) {
//                System.out.println(typeString+" moving west.");
                openTiles[y][x] = true; x--; wX--; eX--; openTiles[y][x] = false;
            } else if (openTiles[y][eX] && getWhetherThereIsAPossiblePath(y, eX, targetY, targetX) &&
                    getShortestPathDistance(y, eX, targetY, targetX) == closestDistanceMinusOne) {
//                System.out.println(typeString+" moving east.");
                openTiles[y][x] = true; x++; wX++; eX++; openTiles[y][x] = false;
            } else if (openTiles[sY][x] && getWhetherThereIsAPossiblePath(sY, x, targetY, targetX) &&
                    getShortestPathDistance(sY, x, targetY, targetX) == closestDistanceMinusOne) {
//                System.out.println(typeString+" moving south.");
                openTiles[y][x] = true; y++; nY++; sY++; openTiles[y][x] = false;
            }
//            else System.out.println(typeString+" moving NOWHERE");
        }

        public int compareTo(Unit o) {
            if (y != o.y) return Integer.compare(y, o.y);
            return Integer.compare(x, o.x);
        }

    }

    private static void printMap() {
        for (int y = 0; y < openTiles.length; y++) {
            boolean[] row = openTiles[y];
            xLoop: for (int x = 0; x < row.length; x++) {
                for (var unit : units) {
                    if (y == unit.y && x == unit.x) {
                        System.out.print(unit.isElf?"E":"G");
                        continue xLoop;
                    }
                }
                System.out.print(openTiles[y][x]?'.':'#');
            }
            System.out.println();
        }
    }

}
