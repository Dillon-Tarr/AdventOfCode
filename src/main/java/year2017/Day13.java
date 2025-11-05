package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day13 {
    static private final int DAY = 13;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private Integer[] layerRanges, layerPeriods;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        processInputData();
        traverseAndGetTripSeverity(); // part 1
        findShortestPossibleSafeDelay(); // part 2

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

    private static void processInputData() {
        String[] depthAndRange = inputStrings.getLast().split(": ");
        int maxDepth = Integer.parseInt(depthAndRange[0]), depth, range;
        layerRanges = new Integer[maxDepth+1];
        layerPeriods = new Integer[maxDepth+1];
        for (int i = 0; i < inputStrings.size(); i++) {
            depthAndRange = inputStrings.get(i).split(": ");
            depth = Integer.parseInt(depthAndRange[0]);
            range = Integer.parseInt(depthAndRange[1]);
            layerRanges[depth] = range;
            layerPeriods[depth] = (range-1)*2;
        }
    }

    private static void traverseAndGetTripSeverity() {
        System.out.println("\nResults of leaving at time 0:");
        int sum = 0, severity, period; Integer layerRange;
        for (int i = 0; i < layerRanges.length; i++) {
            layerRange = layerRanges[i];
            if (layerRange != null) {
                period = layerPeriods[i];
                if (i % period == 0) {
                    severity = i*layerRange;
                    sum += severity;
                    System.out.println("Caught at layer "+i+", range "+layerRange+" (period "+period+"); severity: "+severity+"; Accumulated severity: "+sum);
                }
            }
        }
        System.out.println("\nSeverity of whole trip (part 1 answer): "+sum);
    }

    private static void findShortestPossibleSafeDelay() {
        int d = -1;
        mainLoop: while (d++ < Integer.MAX_VALUE) {
            for (int i = 0; i < layerRanges.length; i++) if (layerRanges[i] != null && (i+d) % layerPeriods[i] == 0) continue mainLoop;
            break;
        }
        System.out.println("\nShortest possible delay while not getting caught (part 2 answer): "+d);
    }

}
