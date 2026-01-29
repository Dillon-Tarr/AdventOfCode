package year2023.day9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part1 {
    static private final int DAY = 9;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private final ArrayList<Integer[]> histories = new ArrayList<>();
    static private final ArrayList<Integer> nextValueForEachSequence = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        extrapolateNextValueForEachHistory();
        sumExtrapolatedValues();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
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

    private static void extrapolateNextValueForEachHistory() {
        System.out.println("\nNext value for each history:");
        for (Integer[] history : histories) {
            ArrayList<Integer[]> subSequences = new ArrayList<>();
            int nextValueForThisHistory = generateSubSequencesAndReturnNextValueForThisSequence(history, subSequences);
            System.out.println(nextValueForThisHistory);
            nextValueForEachSequence.add(nextValueForThisHistory);
        }
    }

    private static int generateSubSequencesAndReturnNextValueForThisSequence(Integer[] history, ArrayList<Integer[]> subSequences) {
        Integer[] previousSequence;
        if (subSequences.isEmpty()) previousSequence = history;
        else previousSequence = subSequences.get(subSequences.size() - 1);

        Integer[] newSubSequence = new Integer[previousSequence.length - 1];
        for (int i = 0; i < newSubSequence.length; i++) newSubSequence[i] = previousSequence[i + 1] - previousSequence[i];

        boolean allZeroes = true;
        for (Integer integer : newSubSequence) if (integer != 0) {allZeroes = false; break;}

        if (allZeroes) return previousSequence[previousSequence.length-1];
        subSequences.add(newSubSequence);
        return previousSequence[previousSequence.length-1] + generateSubSequencesAndReturnNextValueForThisSequence(history, subSequences);
    }

    private static void sumExtrapolatedValues() {
        int sumOfExtrapolatedValues = 0;
        for (Integer value : nextValueForEachSequence) sumOfExtrapolatedValues += value;
        System.out.println("SUM OF ALL EXTRAPOLATED VALUES:\n\n"+sumOfExtrapolatedValues);
    }

}
