package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

class Day2 {
    static private final int DAY = 2;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        parseAndSolveBothParts();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void parseAndSolveBothParts() {
        int part1Sum = 0, part2Sum = 0;
        for (String s : inputStrings) {
            ArrayList<Integer> numbersFromRow = getNumbersFromRow(s);
            part1Sum += getDifferenceBetweenHighestAndLowestNumberInRow(numbersFromRow);
            part2Sum += getQuotientOfOnlyEvenlyDivisiblePairInRow(numbersFromRow);
        }
        System.out.println("\nPart 1 sum: "+ part1Sum);
        System.out.println("\nPart 2 sum: "+part2Sum);
    }

    private static ArrayList<Integer> getNumbersFromRow(String s) {
        int length = s.length();
        boolean parsingNumberMode = false;
        ArrayList<Integer> numbers = new ArrayList<>();
        int i1 = -1, i2 = -1;
        while (true) {
            if (parsingNumberMode) {
                if (++i2 >= length) {
                    numbers.add(Integer.parseInt(s.substring(i1)));
                    break;
                } else if (!Character.isDigit(s.charAt(i2))) {
                    numbers.add(Integer.parseInt(s.substring(i1, i2)));
                    i1 = i2;
                    parsingNumberMode = false;
                }
            } else { // not parsing number
                if (++i1 >= length) break;
                else if (Character.isDigit(s.charAt(i1))) {
                    i2 = i1;
                    parsingNumberMode = true;
                }
            }
        }
        return numbers;
    }

    private static int getDifferenceBetweenHighestAndLowestNumberInRow(ArrayList<Integer> numbers) {
        int highestSeen = Integer.MIN_VALUE, lowestSeen = Integer.MAX_VALUE;
        for (int n : numbers) {
            if (n > highestSeen) highestSeen = n;
            if (n < lowestSeen) lowestSeen = n;
        }
        return highestSeen-lowestSeen;
    }

    private static int getQuotientOfOnlyEvenlyDivisiblePairInRow(ArrayList<Integer> numbersFromRow) {
        numbersFromRow.sort(Collections.reverseOrder());
        int high, low;
        for (int i = 0; i < numbersFromRow.size()-1; i++) {
            high = numbersFromRow.get(i);
            for (int j = i +1; j < numbersFromRow.size(); j++) {
                low = numbersFromRow.get(j);
                if (low > high) throw new RuntimeException("Something has gone horribly wrong.");
                else if (low == high) continue;
                if (high % low == 0) return high/low;
            }
        }
        throw new RuntimeException("No evenly divisible pair could be found. Check input.");
    }

}
