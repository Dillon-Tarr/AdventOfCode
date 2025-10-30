package year2017;

import shared.CardinalDirection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static shared.CardinalDirection.*;

class Day3 {
    static private final int DAY = 3;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private int goal;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        findGoalDistance(); // part 1

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            goal = Integer.parseInt(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void findGoalDistance() {
        if (goal < 1) throw new IllegalArgumentException("Illegal goal: "+goal);
        int x = 0, y = 0, n = 1, stepSize = 0;
        CardinalDirection direction = EAST;
        while (true) {
            switch (direction) { case EAST, WEST -> stepSize++; }
            switch (direction) {
                case EAST -> x+=stepSize;
                case NORTH -> y+=stepSize;
                case WEST -> x-=stepSize;
                case SOUTH -> y-=stepSize;
                default -> throw new IllegalArgumentException("Illegal direction: "+direction);
            }
            n+=stepSize;
            if (n > goal) {
                int overshotDistance = n-goal;
                switch (direction) {
                    case EAST -> x-=overshotDistance;
                    case NORTH -> y-=overshotDistance;
                    case WEST -> x+=overshotDistance;
                    case SOUTH -> y+=overshotDistance;
                }
                break;
            } else direction = CardinalDirection.rotateLeft(direction);
        }
        int distance = Math.abs(x)+Math.abs(y);
        System.out.println("\nManhattan distance from square 1 to square "+goal+" (part 1 solution): "+distance);
    }

}
