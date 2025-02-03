package day16.part2;

import java.io.*;

public class Main {
    static private final File INPUT_FILE = new File("input-files/day16input.txt");
    static private char[][] gridOfCharacters;
    static private int greatestEnergizationOfAnyBeamConfiguration = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        tryEveryPossibleBeamConfiguration();
        System.out.println("\nEnergized tile count of best beam configuration:\n\n"+greatestEnergizationOfAnyBeamConfiguration);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(INPUT_FILE))) {
            lnr.skip(Long.MAX_VALUE);
            int rowCount = lnr.getLineNumber();
            gridOfCharacters = new char[rowCount][];
        } catch (IOException e) {throw new RuntimeException();}
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            for (int y = 0; y < gridOfCharacters.length; y++) gridOfCharacters[y] = br.readLine().toCharArray();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void tryEveryPossibleBeamConfiguration() {
        for (int y = 0; y < gridOfCharacters.length; y++) {
            BeamConfigurationTest rightTest = new BeamConfigurationTest('r', y, 0, gridOfCharacters);
            checkIfGreatest(rightTest.run());
        }
        for (int y = 0; y < gridOfCharacters.length; y++) {
            BeamConfigurationTest leftTest = new BeamConfigurationTest('l', y, gridOfCharacters[0].length-1, gridOfCharacters);
            checkIfGreatest(leftTest.run());
        }
        for (int x = 0; x < gridOfCharacters[0].length; x++) {
            BeamConfigurationTest upTest = new BeamConfigurationTest('u', gridOfCharacters.length-1, x, gridOfCharacters);
            checkIfGreatest(upTest.run());
        }
        for (int x = 0; x < gridOfCharacters[0].length; x++) {
            BeamConfigurationTest downTest = new BeamConfigurationTest('d', 0, x, gridOfCharacters);
            checkIfGreatest(downTest.run());
        }
    }

    private static void checkIfGreatest(int energization) {
        if (energization > greatestEnergizationOfAnyBeamConfiguration) greatestEnergizationOfAnyBeamConfiguration = energization;
    }

}
