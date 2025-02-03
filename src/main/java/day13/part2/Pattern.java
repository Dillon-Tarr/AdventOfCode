package day13.part2;

import java.util.ArrayList;

public class Pattern {
    private final String[] rows;
    private final String[] columns;
    private final int value;
    private static int sumOfAllValues = 0;

    public int getValue() {return value;}
    public static int getSumOfAllValues() {return sumOfAllValues;}

    Pattern(ArrayList<String> inputRows) {
        rows = new String[inputRows.size()];
        for (int i = 0; i < inputRows.size(); i++) rows[i] = inputRows.get(i);
        columns = new String[rows[0].length()];
        for (int i = 0; i < rows[0].length(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String row : rows) stringBuilder.append(row.charAt(i));
            columns[i] = stringBuilder.toString();
        }
        value = findValue();
        sumOfAllValues += value;
    }

    private int findValue() {
        for (int i = 0; i < rows.length-1; i++) if (returnIsMostlyMirrored(i, rows)) return (i+1)*100;
        for (int i = 0; i < columns.length-1; i++) if (returnIsMostlyMirrored(i, columns)) return i+1;
        return 0;
    }

    private boolean returnIsMostlyMirrored(int checkIndex, String[] lines) {
        int i1 = checkIndex;
        int i2 = checkIndex+1;
        int smudgeCount = 0;
        while (i1 >= 0 && i2 < lines.length && smudgeCount < 2) {
            smudgeCount += returnStringDifferenceCount(lines[i1], lines[i2]);
            i1--; i2++;
        }
        return smudgeCount == 1;
    }

    private int returnStringDifferenceCount(String string1, String string2) {
        if (string1.length() != string2.length()) return 2;
        int differenceCount = 0;
        for (int i = 0; i < string1.length(); i++) {
            if (string1.charAt(i) != string2.charAt(i)) differenceCount++;
            if (differenceCount > 1) return 2;
        }
        return differenceCount;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String row : rows) {
            stringBuilder.append(row).append(System.lineSeparator());
        }
        stringBuilder.setLength(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

}
