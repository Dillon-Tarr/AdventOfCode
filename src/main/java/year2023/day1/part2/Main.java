package year2023.day1.part2;

import java.io.*;
import java.util.ArrayList;

public class Main {
    static private final int DAY = 1;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    private static final ArrayList<String> values = new ArrayList<>(1000);

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getGarbledValues();
        changeFirstAndLastNumberWordsToNumerals();
        removeNonNumeralsFromValues();
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

    private static void changeFirstAndLastNumberWordsToNumerals() {
        values.replaceAll(Main::changeFirstAndLastNumberWordsToNumerals);
        System.out.println("\nReal values obtained from numeral-only values.\nReal values:\n" + values);
    }

    private static String changeFirstAndLastNumberWordsToNumerals(String s) {
        int[] last = getLastNumberWordNumeralAndIndex(s);
        int lastWordNumeral = last[0], lastWordIndex = last[1], lastWordLastLetterIndex = last[1] + last[2] - 1;
        if (lastWordIndex == -1) return s;

        String beforeLastWordSubstring = s.substring(0, lastWordIndex);
        String afterLastWordSubstring = "";
        if (s.length() > lastWordLastLetterIndex) afterLastWordSubstring += s.substring(lastWordLastLetterIndex + 1);
        String workingString = beforeLastWordSubstring+lastWordNumeral+afterLastWordSubstring;

        int[] first = getFirstNumberWordNumeralAndIndex(s);
        int firstWordNumeral = first[0], firstWordIndex = first[1], firstWordLastLetterIndex = first[1] +first [2] - 1;
        if (firstWordIndex == lastWordIndex) return workingString;

        String beforeFirstWordSubstring = s.substring(0, firstWordIndex);
        String afterFirstWordSubstring = "";
        if (firstWordLastLetterIndex == lastWordIndex) afterFirstWordSubstring += workingString.substring(firstWordLastLetterIndex);
        else afterFirstWordSubstring += workingString.substring(firstWordLastLetterIndex+1);

        return beforeFirstWordSubstring+firstWordNumeral+afterFirstWordSubstring;
    }

    private static int[] getFirstNumberWordNumeralAndIndex(String s){
        int i = 0;
        while (i <= (s.length()-5)) {
            if (s.charAt(i) == 'o' && s.charAt(i+1) == 'n' && s.charAt(i+2) == 'e') return returnInts(1, i, 3);
            if (s.charAt(i) == 't'){
                if (s.charAt(i+1) == 'w' && s.charAt(i+2) == 'o') return returnInts(2, i, 3);
                if (s.charAt(i+1) == 'h' && s.charAt(i+2) == 'r' && s.charAt(i+3) == 'e' && s.charAt(i+4) == 'e') return returnInts(3, i, 5);
            }
            if (s.charAt(i) == 'f') {
                if (s.charAt(i+1) == 'o' && s.charAt(i+2) == 'u' && s.charAt(i+3) == 'r') return returnInts(4, i, 4);
                if (s.charAt(i+1) == 'i' && s.charAt(i+2) == 'v' && s.charAt(i+3) == 'e') return returnInts(5, i, 4);
            }
            if (s.charAt(i) == 's') {
                if (s.charAt(i+1) == 'i' && s.charAt(i+2) == 'x') return returnInts(6, i, 3);
                if (s.charAt(i+1) == 'e' && s.charAt(i+2) == 'v' && s.charAt(i+3) == 'e' && s.charAt(i+4) == 'n') return returnInts(7, i, 5);
            }
            if (s.charAt(i) == 'e' && s.charAt(i+1) == 'i' && s.charAt(i+2) == 'g' && s.charAt(i+3) == 'h' && s.charAt(i+4) == 't') return returnInts(8, i, 5);
            if (s.charAt(i) == 'n' && s.charAt(i+1) == 'i' && s.charAt(i+2) == 'n' && s.charAt(i+3) == 'e') return returnInts(9, i, 4);
            else i++;
        }
        if (i == (s.length()-4)) {
            if (s.charAt(i) == 'o' && s.charAt(i+1) == 'n' && s.charAt(i+2) == 'e') return returnInts(1, i, 3);
            if (s.charAt(i) == 't' && s.charAt(i+1) == 'w' && s.charAt(i+2) == 'o') return returnInts(2, i, 3);
            if (s.charAt(i) == 'f') {
                if (s.charAt(i+1) == 'o' && s.charAt(i+2) == 'u' && s.charAt(i+3) == 'r') return returnInts(4, i, 4);
                if (s.charAt(i+1) == 'i' && s.charAt(i+2) == 'v' && s.charAt(i+3) == 'e') return returnInts(5, i, 4);
            }
            if (s.charAt(i) == 's' && s.charAt(i+1) == 'i' && s.charAt(i+2) == 'x') return returnInts(6, i, 3);
            if (s.charAt(i) == 'n' && s.charAt(i+1) == 'i' && s.charAt(i+2) == 'n' && s.charAt(i+3) == 'e') return returnInts(9, i, 4);
            else i++;
        }
        if (i == (s.length()-3)) {
            if (s.charAt(i) == 'o' && s.charAt(i+1) == 'n' && s.charAt(i+2) == 'e') return returnInts(1, i, 3);
            if (s.charAt(i) == 't' && s.charAt(i+1) == 'w' && s.charAt(i+2) == 'o') return returnInts(2, i, 3);
            if (s.charAt(i) == 's' && s.charAt(i+1) == 'i' && s.charAt(i+2) == 'x') return returnInts(6, i, 3);
        }
        return returnInts(-1, -1, -1);
    }

    private static int[] getLastNumberWordNumeralAndIndex(String s){
        int i = s.length()-3;
        if (i >= 0) {
            if (s.charAt(i) == 'o' && s.charAt(i+1) == 'n' && s.charAt(i+2) == 'e') return returnInts(1, i, 3);
            if (s.charAt(i) == 't' && s.charAt(i+1) == 'w' && s.charAt(i+2) == 'o') return returnInts(2, i, 3);
            if (s.charAt(i) == 's' && s.charAt(i+1) == 'i' && s.charAt(i+2) == 'x') return returnInts(6, i, 3);
            else i--;
        }
        if (i >= 0) {
            if (s.charAt(i) == 'o' && s.charAt(i+1) == 'n' && s.charAt(i+2) == 'e') return returnInts(1, i, 3);
            if (s.charAt(i) == 't' && s.charAt(i+1) == 'w' && s.charAt(i+2) == 'o') return returnInts(2, i, 3);
            if (s.charAt(i) == 'f') {
                if (s.charAt(i+1) == 'o' && s.charAt(i+2) == 'u' && s.charAt(i+3) == 'r') return returnInts(4, i, 4);
                if (s.charAt(i+1) == 'i' && s.charAt(i+2) == 'v' && s.charAt(i+3) == 'e') return returnInts(5, i, 4);
            }
            if (s.charAt(i) == 's' && s.charAt(i+1) == 'i' && s.charAt(i+2) == 'x') return returnInts(6, i, 3);
            if (s.charAt(i) == 'n' && s.charAt(i+1) == 'i' && s.charAt(i+2) == 'n' && s.charAt(i+3) == 'e') return returnInts(9, i, 4);
            else i--;
        }
        while (i >= 0) {
            if (s.charAt(i) == 'o' && s.charAt(i+1) == 'n' && s.charAt(i+2) == 'e') return returnInts(1, i, 3);
            if (s.charAt(i) == 't'){
                if (s.charAt(i+1) == 'w' && s.charAt(i+2) == 'o') return returnInts(2, i, 3);
                if (s.charAt(i+1) == 'h' && s.charAt(i+2) == 'r' && s.charAt(i+3) == 'e' && s.charAt(i+4) == 'e') return returnInts(3, i, 5);
            }
            if (s.charAt(i) == 'f') {
                if (s.charAt(i+1) == 'o' && s.charAt(i+2) == 'u' && s.charAt(i+3) == 'r') return returnInts(4, i, 4);
                if (s.charAt(i+1) == 'i' && s.charAt(i+2) == 'v' && s.charAt(i+3) == 'e') return returnInts(5, i, 4);
            }
            if (s.charAt(i) == 's') {
                if (s.charAt(i+1) == 'i' && s.charAt(i+2) == 'x') return returnInts(6, i, 3);
                if (s.charAt(i+1) == 'e' && s.charAt(i+2) == 'v' && s.charAt(i+3) == 'e' && s.charAt(i+4) == 'n') return returnInts(7, i, 5);
            }
            if (s.charAt(i) == 'e' && s.charAt(i+1) == 'i' && s.charAt(i+2) == 'g' && s.charAt(i+3) == 'h' && s.charAt(i+4) == 't') return returnInts(8, i, 5);
            if (s.charAt(i) == 'n' && s.charAt(i+1) == 'i' && s.charAt(i+2) == 'n' && s.charAt(i+3) == 'e') return returnInts(9, i, 4);
            else i--;
        }
        return returnInts(-1, -1, -1);
    }

    private static int[] returnInts(int numeral, int index, int wordLength){
        int[] array = new int[3];
        array[0] = numeral;
        array[1] = index;
        array[2] = wordLength;
        return array;
    }

    private static void removeNonNumeralsFromValues() {
        values.replaceAll(value -> value.replaceAll("[^1-9]", ""));
        System.out.println("\nNon-numerals removed from all values.\nCurrent values:\n" + values);
    }

    private static void getRealCalibrationValues() {
        for (int i = 0; i < values.size(); i++) {
            String fake = values.get(i);
            String real = ""+fake.charAt(0)+fake.charAt(fake.length()-1);
            values.set(i, real);
        }
        System.out.println("\nReal values obtained from numeral-only values.\nReal values:\n" + values);
    }

    private static void sumAllCalibrationValues() {
        int sumOfRealCalibrationValues = 0;
        for (String value : values) {
            sumOfRealCalibrationValues += Integer.parseInt(value);
        }
        System.out.println("\nSUM OF REAL CALIBRATION VALUES:\n"+sumOfRealCalibrationValues);
    }
}