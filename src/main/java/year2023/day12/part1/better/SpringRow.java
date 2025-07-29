package year2023.day12.part1.better;

import java.util.Arrays;

class SpringRow {
    private static int sumOfAllRowsArrangementCounts = 0;

    public static int getSumOfAllRowsArrangementCounts() {return sumOfAllRowsArrangementCounts;}

    SpringRow(String dataLine) {
        int spaceIndex = dataLine.indexOf(" ");
        String[] groupCountStrings = dataLine.substring(spaceIndex+1).split(",");

        String row = dataLine.substring(0, spaceIndex);
        int[] groupCounts = new int[groupCountStrings.length];
        for (int i = 0; i < groupCountStrings.length; i++) groupCounts[i] = Integer.parseInt(groupCountStrings[i]);

        int minimumRemainingLength = groupCounts.length-1;
        for (int groupCount : groupCounts) minimumRemainingLength += groupCount;
        sumOfAllRowsArrangementCounts += countArrangements(row, groupCounts, minimumRemainingLength);
    }

    private int countArrangements(String remainingRow, int[] remainingGroupCounts, int minimumRemainingLength) {
        if (remainingRow.isEmpty()) { // Out of spring data points, so should also be out of group counts.
            if (remainingGroupCounts.length == 0) return 1;
            else return 0;}
        if (remainingGroupCounts.length == 0) {
            if (remainingRow.contains("#")) return 0;
            else return 1;}

        int count = 0;

        if ((remainingRow.length() > minimumRemainingLength) && (remainingRow.charAt(0) == '.' || remainingRow.charAt(0) == '?'))
                count += countArrangements(discardBeginningOfString(remainingRow, 1), remainingGroupCounts, minimumRemainingLength);
        if (remainingRow.charAt(0) == '#' || remainingRow.charAt(0) == '?') {
            String groupString = remainingRow.substring(0, remainingGroupCounts[0]);
            boolean noBrokenSpringRightAfterGroup = remainingRow.length() == remainingGroupCounts[0] || remainingRow.charAt(remainingGroupCounts[0]) != '#';

            if (remainingRow.length() >= minimumRemainingLength && !(groupString.contains(".")) && noBrokenSpringRightAfterGroup)
                count += countArrangements(discardBeginningOfString(remainingRow, remainingGroupCounts[0]+1),
                        discardFirstElement(remainingGroupCounts), minimumRemainingLength-remainingGroupCounts[0]-1);
        }
        return count;
    }

    private String discardBeginningOfString(String string, int indexBeforeWhichToDiscard) {
        if (indexBeforeWhichToDiscard >= string.length()) return "";
        else return string.substring(indexBeforeWhichToDiscard);
    }

    private int[] discardFirstElement(int[] array) {
        if (array.length <= 1) return new int[0];
        else {
            return Arrays.copyOfRange(array, 1, array.length);
        }
    }

}
