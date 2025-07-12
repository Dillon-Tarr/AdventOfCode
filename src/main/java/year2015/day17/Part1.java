package year2015.day17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Part1 {
    static private final int DAY = 17;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final List<Integer> containerSizes = new ArrayList<>();
    static private int combinationCount = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        testCombinations();

        System.out.println("Number of distinct container combinations that can exactly fit 150 liters: "+ combinationCount);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                containerSizes.add(Integer.parseInt(inputString));
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void testCombinations() {
        int sum;
        for (int intAsBinary = 0; intAsBinary < 2 << containerSizes.size()-1; intAsBinary++) {
            sum = 0;
            for (int i = 0; i < containerSizes.size(); i++) {
                if ((intAsBinary & 1 << i) != 0) sum += containerSizes.get(i);
                if (sum > 150) break;
            }
            if (sum == 150) combinationCount++;
        }
    }

}
