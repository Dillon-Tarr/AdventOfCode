package day8.puzzle2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
//import org.apache.commons.math; // Going to convert to Maven project to make adding external dependencies easier.

public class Main {
    static private final String INPUT_FILE_PATH = "src/day8/input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    static private char[] instructionSet;
    static private final ArrayList<Node> nodes = new ArrayList<>();
    static private final ArrayList<Path> paths = new ArrayList<>();
    static private final ArrayList<ArrayList<KeyFindRecord>> allZEndingKeyFindRecords = new ArrayList<>();
    static private final ArrayList<Integer> leastCommonMultiples = new ArrayList<>();

    public static void main(String[] args) {
        getInputData();
        createPaths();
        getAllZEndingKeyFindRecords();
        findStepCountWhenPathsAllHaveZEndingKey();
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

    private static void findStepCountWhenPathsAllHaveZEndingKey() {
        int i = 0;
//        while (true) { // Cycle KeyFindRecords until all are checked.
//            // Return least common multiple among possibilities.
//            int leastCommonMultiple = 1;
//            for (int j = 0; j < allZEndingKeyFindRecords.size(); j++) {
//
//
//            }
//
//
//            i++;
//        }
    }

}
