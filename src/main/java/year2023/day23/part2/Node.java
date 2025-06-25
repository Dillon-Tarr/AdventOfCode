package year2023.day23.part2;

import shared.CardinalDirection;
import shared.Coordinates;

import java.util.ArrayList;
import java.util.HashSet;

public class Node {
    Coordinates coordinates;
    HashSet<Road> roads = new HashSet<>(4);
    Road northRoad = null;
    Road eastRoad = null;
    Road southRoad = null;
    Road westRoad = null;

    Node(int y, int x) {
        this.coordinates = new Coordinates(y, x);
    }

    void connectTo(Node otherNode, int distance, CardinalDirection startDirection, CardinalDirection endDirection) {
        switch (startDirection) {
            case NORTH -> this.northRoad = new Road(otherNode, distance);
            case EAST -> this.eastRoad = new Road(otherNode, distance);
            case SOUTH -> this.southRoad = new Road(otherNode, distance);
            case WEST -> this.westRoad = new Road(otherNode, distance);
            default -> throw new IllegalArgumentException();
        }
        switch (endDirection) {
            case NORTH -> otherNode.southRoad = new Road(this, distance);
            case EAST -> otherNode.westRoad = new Road(this, distance);
            case SOUTH -> otherNode.northRoad = new Road(this, distance);
            case WEST -> otherNode.eastRoad = new Road(this, distance);
            default -> throw new IllegalArgumentException();
        }
    }

    void groupRoadsAndCheckForDoubleConnections() {
        int counter = 0;
        if (northRoad != null) roads.add(northRoad);
        if (eastRoad != null) if (!roads.add(eastRoad)) counter++;
        if (southRoad != null) if (!roads.add(southRoad)) counter++;
        if (westRoad != null) if (!roads.add(westRoad)) counter++;
        if (counter > 0) System.out.println("A node connected to the same other node with two or more paths.");
        else System.out.println("A node connected to each of its neighbors with only one path each.");
        System.out.println("Number of roads: "+roads.size());
    }

    ArrayList<Step> attemptTravel(Step step) {
        ArrayList<Step> newSteps = new ArrayList<>();
        for (Road road : roads) {
            if (!step.visitedNodes.contains(road.destination))
                newSteps.add(new Step(road.destination, step.stepCount + road.distance, step.visitedNodes));
        }
        return newSteps;
    }

}
