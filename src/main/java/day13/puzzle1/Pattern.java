package day13.puzzle1;

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
        for (int i = 0; i < rows.length-1; i++) if (returnIsMirrored(i, rows)) return (i+1)*100;
        for (int i = 0; i < columns.length-1; i++) if (columns[i].equals(columns[i+1])) if (returnIsMirrored(i, columns)) return i+1;
        return 0;
    }

    private boolean returnIsMirrored(int checkIndex, String[] lines) {
        int i1 = checkIndex;
        int i2 = checkIndex+1;
        while (i1 >= 0 && i2 < lines.length) {
            if (!lines[i1].equals(lines[i2])) return false;
            i1--; i2++;
        }
        return true;
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
