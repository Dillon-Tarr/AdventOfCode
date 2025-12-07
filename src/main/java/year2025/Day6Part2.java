package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day6Part2 {
    static private final int DAY = 6;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final ArrayList<char[]> operandArrays = new ArrayList<>();
    static private int operandRowLength;
    static private char[] operatorRow;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        var inputStrings = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        var operandRowStringBuilders = new ArrayList<StringBuilder>();
        operandRowLength = 0;
        for (var s : inputStrings) {
            switch (s.charAt(0)) {
                case '*', '+' -> operatorRow = s.toCharArray();
                default -> {
                    int length = s.length();
                    if (length > operandRowLength) operandRowLength = length;
                    operandRowStringBuilders.add(new StringBuilder(s));
                }
            }
        }
        for (var sb : operandRowStringBuilders) {
            int diff = operandRowLength - sb.length();
            if (diff > 0) sb.repeat(' ', diff);
            operandArrays.add(sb.toString().toCharArray());
        }
    }

    private static void solve() {
        long total = 0;
        int startIndex = 0, endIndex;
        boolean lastColumn = false;
        while (true) {
            endIndex = -1;
            nextOperatorLoop: for (int i = startIndex+1; i < operatorRow.length; i++) {
                switch (operatorRow[i]) { case '+', '*' -> { endIndex = i; break nextOperatorLoop; } }
            }
            if (endIndex == -1) {
                endIndex = operandRowLength+1;
                lastColumn = true;
            }
            var columnNumbers = new ArrayList<Integer>();
            for (int i = startIndex; i < endIndex-1; i++) {
                var sb = new StringBuilder();
                for (var row : operandArrays) {
                    char ch = row[i];
                    if (Character.isDigit(ch)) sb.append(ch);
                }
                columnNumbers.add(Integer.parseInt(sb.toString()));
            }
            long value;
            if (operatorRow[startIndex] == '+') {
                value = 0;
                for (var n : columnNumbers) value += n;
            } else {
                value = 1;
                for (var n : columnNumbers) value *= n;
            }
            total += value;
            if (lastColumn) break;
            startIndex = endIndex;
        }
        System.out.println("\nGrand total (part 2 answer): "+total);
    }

}
