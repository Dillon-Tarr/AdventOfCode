package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

class Day20 {
    static private final int DAY = 20;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private String s;
    static private int length, lowY = 1000, highY = 1000, lowX = 1000, highX = 1000;
    static private final char[][] map = new char[2000][2000];
    static private final Node[][] nodesGrid = new Node[2000][2000];
    static private final ArrayList<Node> nodesList = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));

    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            s = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
        s = s.substring(1, s.length()-1);
        length = s.length();
    }

    private static void solve() {
        for (int y = 0; y < 2000; y++) for (int x = 0; x < 2000; x++) map[y][x] = '#';
        map[1000][1000] = 'X';
        traverseFrom(1000, 1000, 0);
        printMap();
        postTraversalPrep();
        getShortestPathToEachNode();
        int furthestRoomDistance = 0, part2Count = 0;
        for (var node : nodesList) {
            int distance = node.recordFewestStepsToReach;
            if (distance > furthestRoomDistance) furthestRoomDistance = distance;
            if (distance >= 1000) part2Count++;
        }
        System.out.println("\nFewest steps needed to reach the hardest-to-reach room (part 1 answer): "+furthestRoomDistance+
                "\n\nNumber of rooms requiring at least 1000 steps to reach: "+part2Count);
    }

    private static void traverseFrom(int y, int x, int i) {
        while (i < length) {
            switch (s.charAt(i)) {
                case 'N' -> { map[--y][x] = '-'; map[--y][x] = '.'; if (y < lowY) lowY = y; }
                case 'S' -> { map[++y][x] = '-'; map[++y][x] = '.'; if (y > highY) highY = y; }
                case 'W' -> { map[y][--x] = '|'; map[y][--x] = '.'; if (x < lowX) lowX = x; }
                case 'E' -> { map[y][++x] = '|'; map[y][++x] = '.'; if (x > highX) highX = x; }
                case '(' -> {
                    int optionLevel = 0;
                    boolean newPathTime = true;
                    while (++i < length) switch (s.charAt(i)) {
                        case '(' -> optionLevel++;
                        case ')' -> { if (--optionLevel < 0) { traverseFrom(y, x, ++i); return; } }
                        case '|' -> { if (optionLevel == 0) newPathTime = true; }
                        default -> {if (optionLevel == 0 && newPathTime) { traverseFrom(y, x, i); newPathTime = false; } }
                    }
                }
                default -> { return; }
            }
            i++;
        }
    }

    private static void printMap() {
        System.out.println("\nMap:");
        var sb = new StringBuilder();
        for (int y = lowY-1; y <= highY+1; y++) {
            sb.setLength(0); char[] row = map[y];
            for (int x = lowX-1; x <= highX+1; x++) sb.append(row[x]);
            System.out.println(sb);
        }
    }

    private static void postTraversalPrep() {
        for (int y = lowY; y <= highY; y+=2) for (int x = lowX; x <= highX; x+=2)
            if (map[y][x] != '#') { var node = new Node(y, x); nodesGrid[y][x] = node; nodesList.add(node); }
        for (int y = lowY; y <= highY; y+=2) for (int x = lowX; x <= highX; x+=2) {
            Node node = nodesGrid[y][x]; if (node == null) continue;
            Node otherNode;
            if (map[y][x+1] != '#') {
                otherNode = nodesGrid[y][x+2];
                if (otherNode != null) {
                    node.adjacentNodes.add(otherNode);
                    otherNode.adjacentNodes.add(node);
                }
            }
            if (map[y+1][x] != '#') {
                otherNode = nodesGrid[y+2][x];
                if (otherNode != null) {
                    node.adjacentNodes.add(otherNode);
                    otherNode.adjacentNodes.add(node);
                }
            }
        }
    }

    static private void getShortestPathToEachNode() {
        ArrayDeque<Step> queue = new ArrayDeque<>();
        queue.add(new Step(nodesGrid[1000][1000], 0));
        while (!queue.isEmpty()) {
            var attempt = queue.poll();
            Node currentNode = attempt.currentNode;
            int newStepCount = attempt.stepsTaken+1;
            for (Node node : currentNode.adjacentNodes) {
                if (newStepCount >= node.recordFewestStepsToReach) continue;
                node.recordFewestStepsToReach = newStepCount;
                queue.add(new Step(node, newStepCount));
            }
        }
    }

    private record Step(Node currentNode, int stepsTaken) {}

    private static class Node {
        final int y, x; int recordFewestStepsToReach = Integer.MAX_VALUE;
        ArrayList<Node> adjacentNodes = new ArrayList<>();

        Node(int y, int x) {
            this.y = y; this.x = x;
        }

    }

}
