package year2015.day17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Part2 {
    static private final int DAY = 17;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final List<Integer> containerSizes = new ArrayList<>();
    static private final List<Integer> numberOfContainersUsedPerSolve = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        testCombinations();
        answerTheQuestion();

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
        int sum, numberOfContainersUsed;
        for (int intAsBinary = 0; intAsBinary < 2 << containerSizes.size()-1; intAsBinary++) {
            sum = 0; numberOfContainersUsed = 0;
            for (int i = 0; i < containerSizes.size(); i++) {
                if ((intAsBinary & 1 << i) != 0) {
                    sum += containerSizes.get(i);
                    if (sum > 150) break;
                    numberOfContainersUsed++;
                }
            }
            if (sum == 150) numberOfContainersUsedPerSolve.add(numberOfContainersUsed);
        }
    }

    private static void answerTheQuestion() {
        int leastNumberOfContainersThatCanBeUsed = Integer.MAX_VALUE, numberOfCombinationsUsingThatManyContainers = 0;
        for (int n : numberOfContainersUsedPerSolve) if (n < leastNumberOfContainersThatCanBeUsed) leastNumberOfContainersThatCanBeUsed = n;
        for (int n : numberOfContainersUsedPerSolve) if (n == leastNumberOfContainersThatCanBeUsed) numberOfCombinationsUsingThatManyContainers++;
        System.out.println("\nLeast number of containers that can be fully used to store all 150 liters: "+ leastNumberOfContainersThatCanBeUsed);
        System.out.println("\nNumber of combinations using exactly that many containers: "+ numberOfCombinationsUsingThatManyContainers);
    }

}
