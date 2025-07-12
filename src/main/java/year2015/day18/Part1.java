package year2015.day18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Part1 {
    static private final int DAY = 18;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final int gridWidth = 100;
    static private final int numberOfStepsLimit = 100;
    static private final boolean printMode = false;
    static private final boolean[][] lights = new boolean[gridWidth][gridWidth];
    static private final boolean[][] nextState = new boolean[gridWidth][gridWidth];
    static private int numberOfStepsCompleted = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        animateLightGrid();
        countLitLights();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            int y = 0;
            while (inputString != null) {
                for (int x = 0; x < gridWidth; x++) {
                    if (inputString.charAt(x) == '#') {
                        lights[y][x] = true;
                        nextState[y][x] = true;
                    }
                }
                inputString = br.readLine();
                y++;
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void animateLightGrid() {
        if (printMode) {
            printState();
            for (int i = 0; i < numberOfStepsLimit; i++) {
                determineNextState();
                setNewState();
                numberOfStepsCompleted++;
                printState();
            }
        } else {
            for (int i = 0; i < numberOfStepsLimit; i++) {
                determineNextState();
                setNewState();
                numberOfStepsCompleted++;
            }
        }
    }

    private static void determineNextState() {
        int litNeighborsCount;
        for (int y = 0; y < gridWidth; y++) for (int x = 0; x < gridWidth; x++) {
            litNeighborsCount = countLitNeighbors(y, x);
            if (lights[y][x]) switch (litNeighborsCount) {
                case 2, 3 -> {}
                default -> nextState[y][x] = false;
            } else if (litNeighborsCount == 3) nextState[y][x] = true;
        }
    }

    private static int countLitNeighbors(int y, int x) {
        int count = 0;
        if (y > 0) {
            if (x > 0 && lights[y-1][x-1]) count++;
            if (lights[y-1][x]) count++;
            if (x < gridWidth-1 && lights[y-1][x+1]) count++;
        }
        if (y < gridWidth-1) {
            if (x > 0 && lights[y+1][x-1]) count++;
            if (lights[y+1][x]) count++;
            if (x < gridWidth-1 && lights[y+1][x+1]) count++;
        }
        if (x > 0 && lights[y][x-1]) count++;
        if (x < gridWidth-1 && lights[y][x+1]) count++;
        return count;
    }

    private static void setNewState() {
        for (int y = 0; y < gridWidth; y++) for (int x = 0; x < gridWidth; x++) lights[y][x] = nextState[y][x];
    }

    private static void printState() {
        System.out.println("\nState after completion of "+numberOfStepsCompleted+" steps:");
        for (int y = 0; y < gridWidth; y++) {
            for (int x = 0; x < gridWidth; x++) System.out.print(lights[y][x] ? '#' : '.');
            System.out.println();
        }
    }

    private static void countLitLights() {
        int numberOfLightsLit = 0;
        for (int y = 0; y < gridWidth; y++) for (int x = 0; x < gridWidth; x++) if (lights[y][x]) numberOfLightsLit++;
        System.out.println("\nNumber of lights lit after "+ numberOfStepsLimit +" steps: "+ numberOfLightsLit);
    }

}
