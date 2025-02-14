package day23.part2;

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

    Node(Coordinates coordinates) {
        this.coordinates = coordinates;
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

    ArrayList<Step> attemptTravel(Step previousStep) {
        ArrayList<Step> newSteps = new ArrayList<>();
        for (Road road : roads) {
            if (!previousStep.pastCoordinates.contains(road.destination.coordinates))
                newSteps.add(new Step(road.destination.coordinates.y, road.destination.coordinates.x,
                        previousStep.stepCount + road.distance, previousStep.pastCoordinates));
        }
//        if (newSteps.isEmpty()) System.out.println("A node was a dead end.");
        return newSteps;
    }

}
