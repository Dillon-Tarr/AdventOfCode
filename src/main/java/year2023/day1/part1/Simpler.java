package year2023.day1.part1;

import java.io.*;
import java.util.ArrayList;

class Simpler {
    static private final int DAY = 1;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");

    private static final ArrayList<String> values = new ArrayList<>(1000);
    private static int sumOfRealCalibrationValues;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getGarbledValues();
        removeNonNumeralsFromValues();
        getRealCalibrationValues();

        System.out.println("\nSUM OF REAL CALIBRATION VALUES:\n"+sumOfRealCalibrationValues);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getGarbledValues() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))){
            String currentLine = br.readLine();

            while (currentLine != null) {
                values.add(currentLine);
                currentLine = br.readLine();
            }
            System.out.println("\nValues obtained from file.\nCurrent values:\n" + values);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private static void removeNonNumeralsFromValues() {
        values.replaceAll(value -> value.replaceAll("[^0-9]", ""));
        System.out.println("\nNon-numerals removed from all values.\nCurrent values:\n" + values);
    }

    private static void getRealCalibrationValues() {
        for (int i = 0; i < values.size(); i++) {
            String fake = values.get(i);
            String real = ""+fake.charAt(0)+fake.charAt(fake.length()-1);
            values.set(i, real);
            sumOfRealCalibrationValues += Integer.parseInt(real);
        }
        System.out.println("\nReal values obtained from numeral-only values.\nReal values:\n" + values);
    }
}
