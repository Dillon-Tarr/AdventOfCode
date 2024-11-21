package day9.puzzle2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static private final String INPUT_FILE_PATH = "input-files/day9input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    static private final ArrayList<Integer[]> histories = new ArrayList<>();
    static private final ArrayList<Integer> firstValueForEachSequence = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        extrapolateFirstValueForEachHistory();
        sumExtrapolatedValues();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            while (currentLine != null) {
                String[] stringArray = currentLine.split(" ");
                Integer[] intArray = new Integer[stringArray.length];
                for (int i = 0; i < intArray.length; i++) {
                    intArray[i] = Integer.parseInt(stringArray[i]);
                }
                histories.add(intArray);
                currentLine = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void extrapolateFirstValueForEachHistory() {
        System.out.println("\nFirst value for each history:");
        for (Integer[] history : histories) {
            ArrayList<Integer[]> subSequences = new ArrayList<>();
            int firstValueForThisHistory = generateSubSequencesAndReturnFirstValueForThisSequence(history, subSequences);
            System.out.println(firstValueForThisHistory);
            firstValueForEachSequence.add(firstValueForThisHistory);
        }
    }

    private static int generateSubSequencesAndReturnFirstValueForThisSequence(Integer[] history, ArrayList<Integer[]> subSequences) {
        Integer[] previousSequence;
        if (subSequences.isEmpty()) previousSequence = history;
        else previousSequence = subSequences.get(subSequences.size() - 1);

        Integer[] newSubSequence = new Integer[previousSequence.length - 1];
        for (int i = 0; i < previousSequence.length-1; i++) newSubSequence[i] = previousSequence[i+1] - previousSequence[i];

        boolean allZeroes = true;
        for (Integer integer : newSubSequence) if (integer != 0) {allZeroes = false; break;}

        if (allZeroes) return previousSequence[0];
        subSequences.add(newSubSequence);
        return previousSequence[0] - generateSubSequencesAndReturnFirstValueForThisSequence(history, subSequences);
    }

    private static void sumExtrapolatedValues() {
        int sumOfExtrapolatedValues = 0;
        for (Integer value : firstValueForEachSequence) sumOfExtrapolatedValues += value;
        System.out.println("SUM OF ALL EXTRAPOLATED VALUES:\n\n"+sumOfExtrapolatedValues);
    }

}
