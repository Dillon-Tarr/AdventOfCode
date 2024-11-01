package day12.puzzle1;

import java.util.ArrayList;

public class SpringRow {
    private static int sumOfAllRowsArrangementCounts = 0;

    public static int getSumOfAllRowsArrangementCounts() {return sumOfAllRowsArrangementCounts;}

    private final ArrayList<Integer> requiredDamagedSpringGroupLengths = new ArrayList<>();
    private final char[] springRecords;
    private int numberOfQuestionMarks;
    private final int[] questionMarkIndices;
    private final ArrayList<char[]> possibleArrangements = new ArrayList<>();

    private void incrementArrangementCount() {sumOfAllRowsArrangementCounts++;}

    SpringRow(String dataLine) {
        int spaceIndex = dataLine.indexOf(" ");
        String[] lengthStrings = dataLine.substring(spaceIndex+1).split(",");

        springRecords = dataLine.substring(0, spaceIndex).toCharArray();
        for (String lengthString : lengthStrings) requiredDamagedSpringGroupLengths.add(Integer.parseInt(lengthString));
        for (char springRecord : springRecords) if (springRecord == '?') numberOfQuestionMarks++;
        questionMarkIndices = new int[numberOfQuestionMarks];
        int qmIndicesIndex = 0;
        for (int i = 0; i < springRecords.length; i++) if (springRecords[i] == '?') {
            questionMarkIndices[qmIndicesIndex] = i;
            qmIndicesIndex++;
        }
        generatePossibleArrangements();
        evaluatePossibleArrangements();
    }

    void generatePossibleArrangements() {
        int possibilityCount = (int) Math.pow(2, numberOfQuestionMarks);

        int numberOfLeadingZeroesNeeded = numberOfQuestionMarks-1;
        String formatPattern = "%"+numberOfLeadingZeroesNeeded+"s";

        for (int i = 0; i < possibilityCount; i++) {
            if (numberOfLeadingZeroesNeeded != 0 && i > 1 && (i & (i-1)) == 0) {
                numberOfLeadingZeroesNeeded--;
                formatPattern = "%"+numberOfLeadingZeroesNeeded+"s";
            }
            String binaryString;
            if(numberOfLeadingZeroesNeeded > 0)
                binaryString = String.format(formatPattern + Integer.toBinaryString(i), "0").replaceAll(" ", "0");
            else binaryString = Integer.toBinaryString(i);
            char[] possibleArrangement = springRecords.clone();
            for (int q = 0; q < numberOfQuestionMarks; q++) {
                if (binaryString.charAt(q) == '0') possibleArrangement[questionMarkIndices[q]] = '.';
                else possibleArrangement[questionMarkIndices[q]] = '#';
            }
            possibleArrangements.add(possibleArrangement);
        }
    }

    private void evaluatePossibleArrangements() {
        for (char[] possibleArrangement : possibleArrangements) {
            ArrayList<Integer> requiredDamagedSpringGroupLengthsCopy = new ArrayList<>(requiredDamagedSpringGroupLengths);
            boolean lastSpringWasDamaged = false;
            int currentDamagedSpringGroupSize = 0;

            for (int i = 0; i < possibleArrangement.length; i++) {
                if (!requiredDamagedSpringGroupLengthsCopy.isEmpty()) {
                    if (possibleArrangement[i] == '#') {
                        currentDamagedSpringGroupSize++;
                        if (!lastSpringWasDamaged) lastSpringWasDamaged = true;
                        if (i == possibleArrangement.length-1 && requiredDamagedSpringGroupLengthsCopy.size() == 1 && currentDamagedSpringGroupSize == requiredDamagedSpringGroupLengthsCopy.get(0))
                            incrementArrangementCount();
                    } else { // possibleArrangement[i] == '.'; This spring is operational.
                        if (lastSpringWasDamaged) {
                            if (currentDamagedSpringGroupSize != requiredDamagedSpringGroupLengthsCopy.get(0)) break;
                            else {
                                requiredDamagedSpringGroupLengthsCopy.remove(0);
                                currentDamagedSpringGroupSize = 0;
                                lastSpringWasDamaged = false;
                                if (i == possibleArrangement.length-1 && requiredDamagedSpringGroupLengthsCopy.isEmpty()) incrementArrangementCount();
                            }
                        }
                    }
                } else { // All necessary damaged spring groups have been found. There should be no more damaged springs to the right.
                    if (possibleArrangement[i] == '#') break;
                    if (i == possibleArrangement.length-1) incrementArrangementCount();
                }
            }
        }
    }

}
