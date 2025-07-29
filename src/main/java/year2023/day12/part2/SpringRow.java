package year2023.day12.part2;

import java.util.Arrays;
import java.util.HashMap;

class SpringRow {
    private static long sumOfAllRowsArrangementCounts = 0;
    private static final HashMap<String, Long> cache = new HashMap<>();

    public static long getSumOfAllRowsArrangementCounts() {return sumOfAllRowsArrangementCounts;}

    SpringRow(String dataLine) {
        int spaceIndex = dataLine.indexOf(" ");

        String originalRow = dataLine.substring(0, spaceIndex);
        String row = originalRow+"?"+originalRow+"?"+originalRow+"?"+originalRow+"?"+originalRow;

        String[] groupCountStrings = dataLine.substring(spaceIndex+1).split(",");
        int[] groupCounts = new int[groupCountStrings.length*5];
        for (int i = 0; i< groupCountStrings.length; i++) {
            int count = Integer.parseInt(groupCountStrings[i]);
            for (int x = 0; x <= 4; x++) groupCounts[groupCountStrings.length*x+i] = count;
        }

        int minimumRemainingLength = groupCounts.length-1; // = number of spaces required between groups (minimum 1 operational spring between groups).
        for (int groupCount : groupCounts) minimumRemainingLength += groupCount;
        sumOfAllRowsArrangementCounts += countArrangements(row, groupCounts, minimumRemainingLength);
    }

    private long countArrangements(String remainingRow, int[] remainingGroupCounts, int minimumRemainingLength) {
        if (remainingRow.isEmpty()) { // Out of spring data points, so should also be out of group counts.
            if (remainingGroupCounts.length == 0) return 1;
            else return 0;}
        if (remainingGroupCounts.length == 0) {
            if (remainingRow.contains("#")) return 0;
            else return 1;}

        String key = remainingRow+Arrays.toString(remainingGroupCounts);
        if (cache.containsKey(key)) return cache.get(key);

        long count = 0;

        if ((remainingRow.length() > minimumRemainingLength) && (remainingRow.charAt(0) == '.' || remainingRow.charAt(0) == '?'))
            count += countArrangements(discardBeginningOfString(remainingRow, 1), remainingGroupCounts, minimumRemainingLength);
        if (remainingRow.charAt(0) == '#' || remainingRow.charAt(0) == '?') {
            String groupString = remainingRow.substring(0, remainingGroupCounts[0]);
            boolean noBrokenSpringRightAfterGroup = remainingRow.length() == remainingGroupCounts[0] || remainingRow.charAt(remainingGroupCounts[0]) != '#';
            if (remainingRow.length() >= minimumRemainingLength && !(groupString.contains(".")) && noBrokenSpringRightAfterGroup)
                count += countArrangements(discardBeginningOfString(remainingRow, remainingGroupCounts[0]+1),
                        discardFirstElement(remainingGroupCounts), minimumRemainingLength-remainingGroupCounts[0]-1);
        }
        cache.put(key, count);
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
