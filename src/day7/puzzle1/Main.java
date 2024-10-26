package day7.puzzle1;

import java.io.*;
import java.util.ArrayList;

public class Main {
    static private final String INPUT_FILE_PATH = "src/day7/input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    static private final ArrayList<Hand> hands = new ArrayList<>();

    public static void main(String[] args) {
        getInputData();
        printHands();
    }

    private static void printHands() {
        System.out.println("\nAll hands:");
        for (Hand hand : hands) {
            System.out.println(hand);
        }
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            while (currentLine != null) {
                hands.add(new Hand(currentLine.split(" ")));
                currentLine = br.readLine();
            }

        } catch (IOException e) {throw new RuntimeException(e);}
    }

}
