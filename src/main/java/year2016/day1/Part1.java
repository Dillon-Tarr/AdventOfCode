package year2016.day1;

import shared.CardinalDirection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Part1 {
    static private final int DAY = 1;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private String[] instructionStrings;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        calculateDistanceToEasterBunnyHQ();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            instructionStrings = inputString.split(", ");
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void calculateDistanceToEasterBunnyHQ() {
        CardinalDirection currentOrientation = CardinalDirection.NORTH;
        int x = 0, y = 0, stepDistance;
        char turnChar;
        for (String instruction : instructionStrings) {
            turnChar = instruction.charAt(0);
            stepDistance = Integer.parseInt(instruction.substring(1));
            switch (currentOrientation) { // turn
                case NONE -> throw new RuntimeException("That shouldn't happen!");
                case NORTH -> currentOrientation = turnChar == 'R' ? CardinalDirection.EAST : CardinalDirection.WEST;
                case SOUTH -> currentOrientation = turnChar == 'R' ? CardinalDirection.WEST : CardinalDirection.EAST;
                case EAST -> currentOrientation = turnChar == 'R' ? CardinalDirection.SOUTH : CardinalDirection.NORTH;
                case WEST -> currentOrientation = turnChar == 'R' ? CardinalDirection.NORTH : CardinalDirection.SOUTH;
            }
            switch (currentOrientation) { // travel
                case NORTH -> y += stepDistance;
                case SOUTH -> y -= stepDistance;
                case EAST -> x += stepDistance;
                case WEST -> x -= stepDistance;
            }
        }
        System.out.println("Distance from start point to Easter Bunny HQ: "+(Math.abs(x)+Math.abs(y)));
    }

}
