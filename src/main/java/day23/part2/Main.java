package day23.part2;

import shared.CardinalDirection;
import shared.Coordinates;

import java.io.*;
import java.util.ArrayList;

public class Main {
    static private final File INPUT_FILE = new File("input-files/day23input.txt");
    static private int gridLength, gridWidth, gridArea, trueStartStretch, trueEndStretch, stepsOfLongestHike = -1;
    static private char[][] grid;
    static private Node[][] nodes;
    static private final ArrayList<Node> nodesArrayList = new ArrayList<>();
    static private final ArrayList<Step> steps = new ArrayList<>();
    static private Coordinates startCoordinates, trueStartCoordinates, endCoordinates, trueEndCoordinates;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        identifyAndCreateNodes();
        connectNodes();
        establishTrueStartAndEnd();
        checkIfAnyNodesConnectTwice();
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
        Node node = new Node(startCoordinates);
        nodes[startCoordinates.y][startCoordinates.x] = node;
        nodesArrayList.add(node);
        node = new Node(endCoordinates);
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
                    node = new Node(new Coordinates(y, x));
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
        for (Node node : nodesArrayList) {
            int y = node.coordinates.y, x = node.coordinates.x;
            if (node.northRoad == null && y > 0 && grid[y-1][x] != '#') measureRoad(y, x, CardinalDirection.NORTH);
            if (node.eastRoad == null && x < gridWidth-1 && grid[y][x+1] != '#') measureRoad(y, x, CardinalDirection.EAST);
            if (node.southRoad == null && y < gridLength-1 && grid[y+1][x] != '#') measureRoad(y, x, CardinalDirection.SOUTH);
            if (node.westRoad == null && x > 0 && grid[y][x-1] != '#') measureRoad(y, x, CardinalDirection.WEST);
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
        trueStartCoordinates = startNode.southRoad.destination.coordinates;
        trueStartStretch = startNode.southRoad.distance;

        Node endNode = nodes[endCoordinates.y][endCoordinates.x];
        trueEndCoordinates = endNode.northRoad.destination.coordinates;
        trueEndStretch = endNode.northRoad.distance;

        nodesArrayList.remove(startNode);
        nodesArrayList.remove(endNode);
        nodes[startCoordinates.y][startCoordinates.x] = null;
        nodes[trueStartCoordinates.y][trueStartCoordinates.x].northRoad = null;
        nodes[endCoordinates.y][endCoordinates.x] = null;
        nodes[trueEndCoordinates.y][trueEndCoordinates.x].southRoad = null;
    }

    private static void checkIfAnyNodesConnectTwice() {
        for (Node node : nodesArrayList) node.groupRoadsAndCheckForDoubleConnections();
    }

    private static void findLongestHike() {
        steps.add(new Step(trueStartCoordinates.y, trueStartCoordinates.x, trueStartStretch));
        int stepCount;
        while (!steps.isEmpty()) {
            Step step = steps.remove(0);
            stepCount = step.stepCount;

            if (step.currentCoordinates == trueEndCoordinates) {
                stepCount+=trueEndStretch;
                if (stepCount > stepsOfLongestHike) stepsOfLongestHike = stepCount;
                continue;
            }

            steps.addAll(nodes[step.currentCoordinates.y][step.currentCoordinates.x].attemptTravel(step));
        }

        System.out.println("\nStep count of longest hike: "+stepsOfLongestHike);
    }

}
