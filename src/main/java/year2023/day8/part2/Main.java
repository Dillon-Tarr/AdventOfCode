package year2023.day8.part2;

import static Math.LCMFinders.findLCMWithPrimes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class Main {
    static private final int DAY = 8;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private char[] instructionSet;
    static private final ArrayList<Node> nodes = new ArrayList<>();
    static private final ArrayList<Path> paths = new ArrayList<>();
    static private final ArrayList<ArrayList<KeyFindRecord>> allZEndingKeyFindRecords = new ArrayList<>();
    static private final ArrayList<BigInteger> bigIntegerLeastCommonMultiples = new ArrayList<>();
    static private final ArrayList<Long> longLeastCommonMultiples = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        createPaths();
        getAllZEndingKeyFindRecords();
        findFirstStepCountWhereAllPathsHaveZEndingKey();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            instructionSet = br.readLine().toCharArray();
            br.readLine();
            String currentLine = br.readLine();
            while (currentLine != null) {
                nodes.add(new Node(currentLine));
                currentLine = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createPaths() {
        for (Node node : nodes) {
            if (node.getKey()[2] == 'A') {
                paths.add(new Path(nodes, instructionSet, node));
            }
        }
        System.out.println("\nNumber of starting locations: "+ paths.size());
    }

    private static void getAllZEndingKeyFindRecords() {
        for (Path path : paths) {
            allZEndingKeyFindRecords.add(path.getZEndingKeyFindRecords());
        }
    }

    private static void findFirstStepCountWhereAllPathsHaveZEndingKey() {
        long[] lengths = new long[allZEndingKeyFindRecords.size()];
        for (int i = 0; i < allZEndingKeyFindRecords.size(); i++) lengths[i] = allZEndingKeyFindRecords.get(i).size();
        long allCombosLength = findLCMWithPrimes(lengths);

        for (int i = 0; i < allCombosLength; i++) { // Cycle KeyFindRecords until all are checked.
            // Return least common multiple among possibilities.
            BigInteger[] bigIntegerStepValues = new BigInteger[allZEndingKeyFindRecords.size()];
            long[] longStepValues = new long[allZEndingKeyFindRecords.size()];
            System.out.println("\nCombo check "+i+": ");
            BigInteger product = BigInteger.valueOf(1);
            for (int j = 0; j < allZEndingKeyFindRecords.size(); j++) {
                int o = i;
                while (o >= allZEndingKeyFindRecords.get(j).size()) {
                    o -= allZEndingKeyFindRecords.get(j).size();
                }
                bigIntegerStepValues[j] = BigInteger.valueOf(allZEndingKeyFindRecords.get(j).get(o).getStepCount());
                longStepValues[j] = allZEndingKeyFindRecords.get(j).get(o).getStepCount();
                System.out.println("value "+j+": "+ bigIntegerStepValues[j]);
                product = product.multiply(bigIntegerStepValues[j]);
            }
            System.out.println("Product of these values: "+product);
            long longLCMOfThese = findLCMWithPrimes(longStepValues);
            longLeastCommonMultiples.add(longLCMOfThese);
            BigInteger BigIntegerLCMOfThese = BigInteger.valueOf(longLCMOfThese);
            bigIntegerLeastCommonMultiples.add(BigIntegerLCMOfThese);
            System.out.println("LCM of these values: "+ BigIntegerLCMOfThese);
            System.out.println("Product divided by LCM: "+(product.divide(BigIntegerLCMOfThese)));
            i++;
        }
        System.out.println("\nNumber of possible intersections: "+ bigIntegerLeastCommonMultiples.size());
        System.out.println("Least common multiples, in the order in which they were encountered:");
        System.out.println(bigIntegerLeastCommonMultiples);
        longLeastCommonMultiples.sort(Long::compare);
        System.out.println("Sorted least common multiples.");
        System.out.println("Least common multiples:");
        System.out.println(longLeastCommonMultiples);
    }

}
