package year2016.day3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part1 {
    static private final int DAY = 3;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<int[]> numberTrios = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        countPossibleTriangles();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            String[] stringTrio;
            int[] numberTrio;
            while (inputString != null) {
                stringTrio = inputString.trim().split(" +");
                numberTrio = new int[3];
                for (int i = 0; i < 3; i++) numberTrio[i] = Integer.parseInt(stringTrio[i]);
                numberTrios.add(numberTrio);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void countPossibleTriangles() {
        int count = 0, longestEdgeLength, longestEdgeIndex, otherEdgesSum;
        for (int[] trio : numberTrios) {
            longestEdgeLength = -1; longestEdgeIndex = -1;
            for (int i = 0; i < 3; i++) {
                if (trio[i] > longestEdgeLength) {
                    longestEdgeLength = trio[i];
                    longestEdgeIndex = i;
                }
            }
            otherEdgesSum = 0;
            for (int i = 0; i < 3; i++) {
                if (i != longestEdgeIndex) otherEdgesSum += trio[i];
            }
            if (otherEdgesSum > longestEdgeLength) count++;
        }
        System.out.println("Number of possible triangles: "+count);
    }

}
