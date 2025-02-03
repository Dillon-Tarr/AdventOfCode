package day7.part1;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    static private final String INPUT_FILE_PATH = "input-files/day7input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    static private final ArrayList<Hand> hands = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        sortHands();
        printHands();
        sumWinnings();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
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

    private static void sortHands() {
        hands.sort(Comparator.comparing(Hand::getScore));
    }

    private static void printHands() {
        System.out.println("\nAll hands:");
        for (Hand hand : hands) {
            System.out.println(hand);
        }
    }

    private static void sumWinnings() {
        int winnings = 0;
        for (int i = 0; i < hands.size(); i++)
            winnings += hands.get(i).getBid() * (i + 1);
        System.out.println("\nTOTAL WINNINGS:\n\n"+winnings);
    }

}
