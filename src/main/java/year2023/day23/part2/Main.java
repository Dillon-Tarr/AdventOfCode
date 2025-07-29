package year2023.day23.part2;

import shared.CardinalDirection;
import shared.Coordinates;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

class Main {
    static private final int DAY = 23;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private int gridLength, gridWidth, gridArea, startStretch, endStretch, stepsOfLongestHike = -1;
    static private char[][] grid;
    static private Node[][] nodes;
    static private final ArrayList<Node> nodesArrayList = new ArrayList<>();
    static private Node effectiveStartNode, effectiveEndNode;
    static private final Stack<Step> steps = new Stack<>();
    static private Coordinates startCoordinates, endCoordinates;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        identifyAndCreateNodes();
        connectNodes();
        establishTrueStartAndEnd();
        groupRoadsAndCheckForDoubleConnections();
        findLongestHike();

        System.out.println("\nExecution time in seconds: " + ((double) (System.nanoTime() - startTime) / 1000000000));
    }

    private static void processInputData() {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(INPUT_FILE))) {
            lnr.skip(Long.MAX_VALUE);
            gridLength = lnr.getLineNumber();
            grid = new char[gridLength][];
        } catch (IOException e) {throw new RuntimeException();}
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            int counter = 0;
            char ch;
            String line = br.readLine();
            gridWidth = line.length();
            nodes = new Node[gridLength][gridWidth];
            gridArea = gridLength*gridWidth;
            startCoordinates = new Coordinates(0, line.indexOf("."));
            for (int y = 0; y < gridLength-1; y++) {
                grid[y] = new char[gridWidth];
                for (int x = 0; x < gridWidth; x++) {
                    ch = line.charAt(x);
                    if (ch != '#') counter++;
                    grid[y][x] = ch;
                }
                line = br.readLine();
            }
            grid[gridLength-1] = new char[gridWidth];
            for (int x = 0; x < gridWidth; x++) {
                ch = line.charAt(x);
                grid[gridLength-1][x] = ch;
                if (ch == '.') {
                    counter++;
                    endCoordinates = new Coordinates(gridLength-1, x);
                }
            }
            System.out.println("\nGrid size (length x width = total number of tiles):\n"+gridLength+" * "+gridWidth+" = "+gridArea);
            System.out.println("\nNumber of walkable tiles: "+ counter);
            System.out.println("Percentage of tiles which are walkable: "+(100*(float)counter/(float)gridArea)+"%");
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void identifyAndCreateNodes() {
        Node node = new Node(startCoordinates.y, startCoordinates.x);
        nodes[startCoordinates.y][startCoordinates.x] = node;
        nodesArrayList.add(node);
        node = new Node(endCoordinates.y, endCoordinates.x);
        nodesArrayList.add(node);
        nodes[endCoordinates.y][endCoordinates.x] = node;
        for (int y = 0; y < gridLength; y++) {
            for (int x = 0; x < gridWidth; x++) {
                if (grid[y][x] == '#') continue;
                int counter = 0;
                if (y != 0 && grid[y-1][x] != '#') counter++;
                if (y != gridLength-1 && grid[y+1][x] != '#') counter++;
                if (x != 0 && grid[y][x-1] != '#') counter++;
                if (x != gridWidth-1 && grid[y][x+1] != '#') counter++;
                if (counter >= 3) {
                    node = new Node(y, x);
                    nodesArrayList.add(node);
                    nodes[y][x] = node;
                }
            }
        }
        int nodeCount = nodesArrayList.size();
        System.out.println("\nNumber of nodes: "+nodeCount);
        System.out.println("Percentage of tiles which are nodes: "+(100*(float)nodeCount/(float)gridArea)+"%\n");
    }

    private static void connectNodes() {
        for (int y = 0; y < gridLength; y++) for (int x = 0; x < gridWidth; x++) {
            if (nodes[y][x] == null) continue;
            if (nodes[y][x].northRoad == null && y > 0 && grid[y - 1][x] != '#') measureRoad(y, x, CardinalDirection.NORTH);
            if (nodes[y][x].eastRoad == null && x < gridWidth - 1 && grid[y][x + 1] != '#') measureRoad(y, x, CardinalDirection.EAST);
            if (nodes[y][x].southRoad == null && y < gridLength - 1 && grid[y + 1][x] != '#') measureRoad(y, x, CardinalDirection.SOUTH);
            if (nodes[y][x].westRoad == null && x > 0 && grid[y][x - 1] != '#') measureRoad(y, x, CardinalDirection.WEST);
        }
    }

    private static void measureRoad(int y, int x, CardinalDirection nextStepDirection) {
        Node startNode = nodes[y][x];
        CardinalDirection startDirection = nextStepDirection;
        int distance = 0;
        while (true) {
            switch (nextStepDirection) {
                case NORTH -> y--;
                case EAST -> x++;
                case SOUTH -> y++;
                case WEST -> x--;
                default -> throw new IllegalArgumentException();
            }
            distance++;
            if (nodes[y][x] != null) break;
            switch (nextStepDirection) {
                case NORTH, SOUTH -> {
                    if (x < gridWidth-1 && grid[y][x+1] != '#') nextStepDirection = CardinalDirection.EAST;
                    if (x > 0 && grid[y][x-1] != '#') nextStepDirection = CardinalDirection.WEST;
                }
                case EAST, WEST -> {
                    if (y > 0 && grid[y-1][x] != '#') nextStepDirection = CardinalDirection.NORTH;
                    if (y < gridLength-1 && grid[y+1][x] != '#') nextStepDirection = CardinalDirection.SOUTH;
                }
            }
        }
        Node endNode = nodes[y][x];
        startNode.connectTo(endNode, distance, startDirection, nextStepDirection);
    }

    private static void establishTrueStartAndEnd() {
        Node startNode = nodes[startCoordinates.y][startCoordinates.x];
        effectiveStartNode = startNode.southRoad.destination;
        startStretch = startNode.southRoad.distance;

        Node endNode = nodes[endCoordinates.y][endCoordinates.x];
        effectiveEndNode = endNode.northRoad.destination;
        endStretch = endNode.northRoad.distance;

        nodesArrayList.remove(startNode);
        nodesArrayList.remove(endNode);
        nodes[startCoordinates.y][startCoordinates.x] = null;
        effectiveStartNode.northRoad = null;
        nodes[endCoordinates.y][endCoordinates.x] = null;
        effectiveEndNode.southRoad = null;
    }

    private static void groupRoadsAndCheckForDoubleConnections() {
        for (Node node : nodesArrayList) node.groupRoadsAndCheckForDoubleConnections();
    }

    private static void findLongestHike() {
        steps.push(new Step(effectiveStartNode, startStretch));
        int stepCount;
        while (!steps.isEmpty()) {
            Step step = steps.pop();
            stepCount = step.stepCount;

            if (step.node == effectiveEndNode) {
                stepCount+= endStretch;
                if (stepCount > stepsOfLongestHike) {
                    stepsOfLongestHike = stepCount;
                    System.out.println("Steps of longest hike updated: "+stepsOfLongestHike);
                }
                continue;
            }

            for (Step newStep : step.node.attemptTravel(step))
                steps.push(newStep);
        }

        System.out.println("\nStep count of longest hike: "+stepsOfLongestHike);
    }

}
