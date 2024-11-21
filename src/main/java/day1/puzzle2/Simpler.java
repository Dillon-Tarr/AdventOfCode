package day1.puzzle2;

import java.io.*;
import java.util.ArrayList;

public class Simpler {
    static private final String INPUT_FILE_PATH = "input-files/day1input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    private static final ArrayList<String> values = new ArrayList<>(1000);
    private static final String[][] numbers = {{"one","1"},{"two","2"},{"three","3"},{"four","4"},
            {"five","5"}, {"six","6"},{"seven","7"}, {"eight","8"},{"nine","9"},{"1","1"},{"2","2"},
            {"3","3"},{"4","4"},{"5","5"}, {"6","6"},{"7","7"},{"8","8"},{"9","9"}};

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getGarbledValues();
        getRealCalibrationValues();
        sumAllCalibrationValues();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getGarbledValues() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))){
            String currentLine = br.readLine();

            while (currentLine != null) {
                values.add(currentLine);
                currentLine = br.readLine();
            }
            System.out.println("\nValues obtained from file.\nOriginal values:\n" + values);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private static void getRealCalibrationValues() {
        values.replaceAll(Simpler::getRealCalibrationValue);
        System.out.println("\nReal values obtained.\nReal values:\n" + values);
    }

    private static String getRealCalibrationValue(String value) {
        String firstNumber = getFirstNumber(value);
        String lastNumber = getLastNumber(value);
        return firstNumber+lastNumber;
    }

    private static String getFirstNumber(String value) {
        int firstNumberCandidateIndex = Integer.MAX_VALUE;
        String firstNumberCandidate = "";
        for (String[] number : numbers) {
            int indexResult = value.indexOf(number[0]);
            if (indexResult > -1 && indexResult < firstNumberCandidateIndex) {
                firstNumberCandidateIndex = indexResult;
                firstNumberCandidate = number[1];
            }
        }
        return firstNumberCandidate;
    }

    private static String getLastNumber(String value) {
        int lastNumberCandidateIndex = -1;
        String lastNumberCandidate = "";
        for (String[] number : numbers) {
            int indexResult = value.lastIndexOf(number[0]);
            if (indexResult > -1 && indexResult > lastNumberCandidateIndex) {
                lastNumberCandidateIndex = indexResult;
                lastNumberCandidate = number[1];
            }
        }
        return lastNumberCandidate;
    }

    private static void sumAllCalibrationValues() {
        int sumOfRealCalibrationValues = 0;
        for (String value : values) {
            sumOfRealCalibrationValues += Integer.parseInt(value);
        }
        System.out.println("\nSUM OF REAL CALIBRATION VALUES:\n"+sumOfRealCalibrationValues);
    }
}