package day19.puzzle2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    static private final File INPUT_FILE = new File("input-files/day19input.txt");
    static private final HashMap<String, Workflow> workflows = new HashMap<>();
    static private final ArrayList<RatingRangeSet> rangeSetsToProcess = new ArrayList<>();
    static private final ArrayList<RatingRangeSet> acceptedRangeSets = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        processRanges();
        countPossibilities();

        System.out.println("\nExecution time in seconds: " + ((double) (System.nanoTime() - startTime) / 1000000000));
    }

    private static void processInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String cl = br.readLine();
            while (cl != null && !cl.isEmpty()) {
                int firstBracketIndex = cl.indexOf("{");
                workflows.put(cl.substring(0, firstBracketIndex), new Workflow(cl.substring(firstBracketIndex + 1, cl.length() - 1)));
                cl = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processRanges() {
        rangeSetsToProcess.add(new RatingRangeSet("in", 1,4000, 1, 4000, 1, 4000, 1, 4000));
        Workflow currentWorkflow;
        while (!rangeSetsToProcess.isEmpty()) {
            RatingRangeSet currentRangeSet = rangeSetsToProcess.remove(0);
            currentWorkflow = workflows.get(currentRangeSet.resultOrNextWorkflow);
            ArrayList<RatingRangeSet> resultingRangeSets = currentWorkflow.processRangeSet(currentRangeSet);
            for (RatingRangeSet resultingRangeSet : resultingRangeSets) {
                if (resultingRangeSet.resultOrNextWorkflow.equals("A")) acceptedRangeSets.add(resultingRangeSet);
                else if (!resultingRangeSet.resultOrNextWorkflow.equals("R")) rangeSetsToProcess.add(resultingRangeSet);
            }
        }
    }

    private static void countPossibilities() {
        long possibilityCount = 0;
        for (RatingRangeSet rangeSet : acceptedRangeSets) {
            possibilityCount += (1+rangeSet.xRange.end()-rangeSet.xRange.start())*(1+rangeSet.mRange.end()-rangeSet.mRange.start())
                    *(1+rangeSet.aRange.end()-rangeSet.aRange.start())*(1+rangeSet.sRange.end()-rangeSet.sRange.start());
        }
        System.out.println("Count of possible combinations: "+possibilityCount);
    }

}