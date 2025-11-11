package year2017;

import shared.CardinalDirection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static shared.CardinalDirection.*;
import static year2017.Day22.State.*;

class Day22 {
    static private final int DAY = 22;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final HashMap<String, Boolean> coordinatesStringToInfectionStatusMap = new HashMap<>();
    static private final HashMap<String, State> part2Map = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        processInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processInputData() { // y increases going DOWN, x increases going RIGHT
        int height = inputStrings.size(), width = inputStrings.getFirst().length(),
                yOffset = height/2,  xOffset = width/2;
        String s, positionString; boolean infected;
        for (int y = 0; y < height; y++) {
            s = inputStrings.get(y);
            for (int x = 0; x < width; x++) {
                positionString = (y-yOffset)+","+(x-xOffset);
                infected = s.charAt(x) == '#';
                coordinatesStringToInfectionStatusMap.put(positionString, infected);
                part2Map.put(positionString, infected ? INFECTED : CLEAN);
            }
        }
    }

    private static void solvePart1() {
        int y = 0, x = 0, infectionBursts = 0;
        String positionString;
        CardinalDirection orientation = NORTH;
        boolean infected;
        for (int burstsCompleted = 0; burstsCompleted < 10_000; burstsCompleted++) {
            positionString = y+","+x;
            infected = coordinatesStringToInfectionStatusMap.getOrDefault(positionString, false);
            if (infected) orientation = orientation.right();
            else { orientation = orientation.left(); infectionBursts++; }
            coordinatesStringToInfectionStatusMap.put(positionString, !infected);
            switch (orientation) {
                case NORTH -> y--;
                case SOUTH -> y++;
                case WEST -> x--;
                case EAST -> x++;
            }
        }
        System.out.println("\nNumber of infection bursts (part 1): "+infectionBursts);
    }

    private static void solvePart2() {
        int y = 0, x = 0, infectionBursts = 0;
        String positionString;
        CardinalDirection orientation = NORTH;
        State state;
        for (int burstsCompleted = 0; burstsCompleted < 10_000_000; burstsCompleted++) {
            positionString = y+","+x;
            state = part2Map.getOrDefault(positionString, CLEAN);
            switch (state) {
                case CLEAN -> orientation = orientation.left();
                case WEAKENED -> infectionBursts++;
                case INFECTED -> orientation = orientation.right();
                case FLAGGED -> orientation = orientation.reverse();
            }
            part2Map.put(positionString, state.next());
            switch (orientation) {
                case NORTH -> y--;
                case SOUTH -> y++;
                case WEST -> x--;
                case EAST -> x++;
            }
        }
        System.out.println("\nNumber of infection bursts (part 2): "+infectionBursts);
    }

    enum State {
        CLEAN { State next() { return WEAKENED; } },
        WEAKENED { State next() { return INFECTED; } },
        INFECTED { State next() { return FLAGGED; } },
        FLAGGED { State next() { return CLEAN; } };

        abstract State next();
    }

}
