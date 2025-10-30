package year2016;

import shared.CardinalDirection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import static shared.CardinalDirection.*;

class Day24 {
    static private final int DAY = 24;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private int gridHeight, gridWidth;
    static private char[][] grid;
    static private Node startNode;
    static private final ArrayList<Node> nodes = new ArrayList<>();
    static private boolean part2Mode;

    static void main(String[] args) {
        long startTime = System.nanoTime();

        part2Mode = args.length > 0;

        getInputData();
        createGridAndNodes();
        findShortestPathsBetweenNodes();
        findShortestPathTouchingAllNodes();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            br.readLine(); // Ignore first line
            String inputString = br.readLine();
            while (inputString != null) {
                inputStrings.add(inputString.substring(1, inputString.length()-1)); // Ignore boundary characters.
                inputString = br.readLine();
            }
            inputStrings.removeLast(); // Ignore last line.
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void createGridAndNodes() {
        gridHeight = inputStrings.size();
        gridWidth = inputStrings.getFirst().length();
        grid = new char[gridHeight][];
        char[] currentRow;
        for (int y = 0; y < gridHeight; y++) {
            currentRow = inputStrings.get(y).toCharArray();
            grid[y] = currentRow;
            for (int x = 0; x < gridWidth; x++) {
                switch (currentRow[x]) {
                    case '.', '#' -> {}
                    case '0' -> {
                        startNode = new Node(Character.getNumericValue(currentRow[x]), x, y);
                        nodes.add(startNode);
                    }
                    default -> nodes.add(new Node(Character.getNumericValue(currentRow[x]), x, y));
                }
            }
        }
    }

    private static void findShortestPathsBetweenNodes() {
        Node node;
        for (int i = 0; i < nodes.size()-1; i++) {
            node = nodes.get(i);
            for (int j = i+1; j < nodes.size(); j++) findShortestConnectingPath(node, nodes.get(j));
        }
    }

    private static void findShortestConnectingPath(Node a, Node b) {
        HashSet<String> visitStrings = new HashSet<>();
        QueuedStep step;
        int stepCount = 0, newStepCount, x = a.x, y = a.y, newX, newY;
        CardinalDirection direction;
        PriorityQueue<QueuedStep> queue = new PriorityQueue<>();
        queue.add(new QueuedStep(0, x, y, NONE));
        visitStrings.add(stepCount+","+x+","+y);
        while (!queue.isEmpty()) {
            step = queue.remove(); stepCount = step.stepCount(); x = step.x(); y = step.y();
            if (x == b.x && y == b.y) {
                a.otherNodeDistanceMap.put(b, stepCount);
                b.otherNodeDistanceMap.put(a, stepCount);
                System.out.println("Shortest path distance between "+a.number +" and "+b.number +": "+stepCount);
                return;
            }
            newStepCount = stepCount+1;
            direction = step.direction();
            if (direction != EAST && x > 0) {
                newX = x-1;
                if (grid[y][newX] != '#' && visitStrings.add(newStepCount+","+newX+","+y)) queue.add(new QueuedStep(newStepCount, newX, y, WEST));
            }
            if (direction != WEST && x < gridWidth-1) {
                newX = x+1;
                if (grid[y][newX] != '#' && visitStrings.add(newStepCount+","+newX+","+y)) queue.add(new QueuedStep(newStepCount, newX, y, EAST));
            }
            if (direction != SOUTH && y > 0) {
                newY = y-1;
                if (grid[newY][x] != '#' && visitStrings.add(newStepCount+","+x+","+newY)) queue.add(new QueuedStep(newStepCount, x, newY, NORTH));
            }
            if (direction != NORTH && y < gridHeight-1) {
                newY = y+1;
                if (grid[newY][x] != '#' && visitStrings.add(newStepCount+","+x+","+newY)) queue.add(new QueuedStep(newStepCount, x, newY, SOUTH));
            }
        }
        throw new RuntimeException("If this was reached, a path could not be found.");
    }

    private static void findShortestPathTouchingAllNodes() {
        ArrayList<Node> nonStartNodes = new ArrayList<>(nodes);
        nonStartNodes.remove(startNode);
        int nonStartNodeCount = nonStartNodes.size(), visitedNodeCount;
        ArrayList<PathRecord> queue = new ArrayList<>(); // Actual queue functionality not needed since checking all possible paths.
        Node otherNode; Node[] visitedNodes = {}, newVisitedNodes; PathRecord pathRecord; int stepCount, newStepCount;
        for (int i = 0; i < nonStartNodeCount; i++) {
            otherNode = nonStartNodes.get(i);
            queue.add(new PathRecord(startNode.otherNodeDistanceMap.get(otherNode), new Node[]{otherNode}));
        }

        while (!queue.isEmpty()) {
            pathRecord = queue.removeFirst();
            if (pathRecord.visitedNodes().length == nonStartNodeCount) {
                queue.add(pathRecord);
                break;
            }
            stepCount = pathRecord.stepCount();
            visitedNodes = pathRecord.visitedNodes(); visitedNodeCount = visitedNodes.length;
            newNodeLoop: for (int o = 0; o < nonStartNodeCount; o++) {
                otherNode = nonStartNodes.get(o);
                for (int v = 0; v < visitedNodeCount; v++) if (otherNode == visitedNodes[v]) continue newNodeLoop;
                newStepCount = stepCount+visitedNodes[visitedNodeCount-1].otherNodeDistanceMap.get(otherNode);
                newVisitedNodes = new Node[visitedNodeCount+1];
                for (int v = 0; v < visitedNodeCount; v++) newVisitedNodes[v] = visitedNodes[v];
                newVisitedNodes[visitedNodeCount] = otherNode;
                queue.add(new PathRecord(newStepCount, newVisitedNodes));
            }
        }
        int shortestPathDistance = Integer.MAX_VALUE, lastIndex = nonStartNodeCount-1;
        HashMap<Node, Integer> startToEndDistanceMap = startNode.otherNodeDistanceMap;
        for (int i = 0; i < queue.size(); i++) {
            pathRecord = queue.get(i);
            stepCount = pathRecord.stepCount();
            if (part2Mode) stepCount += startToEndDistanceMap.get(pathRecord.visitedNodes()[lastIndex]);
            if (stepCount < shortestPathDistance) {
                shortestPathDistance = stepCount;
                visitedNodes = pathRecord.visitedNodes();
            }
        }
        System.out.print("\nShortest path ("+(part2Mode?"including going back to 0 for part 2":
                "not worrying about going back to 0 for part 1")+"):\n0");
        for (int i = 0; i < visitedNodes.length; i++) System.out.print(" -> "+visitedNodes[i].number);
        if (part2Mode) System.out.print(" -> 0");
        System.out.println("\nDistance: "+shortestPathDistance);
    }

    private record PathRecord (int stepCount, Node[] visitedNodes) {}

    private record QueuedStep (int stepCount, int x, int y, CardinalDirection direction) implements Comparable<QueuedStep> {
        @Override
        public int compareTo(QueuedStep o) { return Integer.compare(stepCount, o.stepCount); }
    }

    private static class Node {
        final int number;
        final int x;
        final int y;
        final HashMap<Node, Integer> otherNodeDistanceMap = new HashMap<>();

        private Node(int number, int x, int y) {
            this.number = number;
            this.x = x;
            this.y = y;
        }
    }

}
