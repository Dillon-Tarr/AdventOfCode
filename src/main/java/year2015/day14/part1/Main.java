package year2015.day14.part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Main {
    static private final int DAY = 14;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final ArrayList<Reindeer> reindeer = new ArrayList<>();
    static private int longestDistanceRaced = Integer.MIN_VALUE;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        raceTheReindeer();

        System.out.println("Longest distance raced: "+longestDistanceRaced);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                reindeer.add(new Reindeer(inputString));
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void raceTheReindeer() {
        int distanceRaced;
        for (Reindeer racer : reindeer) {
            distanceRaced = racer.race();
            if (distanceRaced > longestDistanceRaced) longestDistanceRaced = distanceRaced;
        }
    }

}
