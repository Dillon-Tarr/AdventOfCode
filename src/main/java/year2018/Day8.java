package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day8 {
    static private final int DAY = 8;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private int[] numbers;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        String[] numberStrings;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            numberStrings = br.readLine().split(" ");
        } catch (IOException e) {throw new RuntimeException(e);}
        numbers = new int[numberStrings.length];
        for (int i = 0; i < numbers.length; i++) numbers[i] = Integer.parseInt(numberStrings[i]);
    }

    private static void solve() {
        System.out.println("\nMetadata sum (part 1 answer): "+parseNode(0)[0]);
        System.out.println("\nRoot node value (part 2 answer): "+part2Parse(0)[0]);
    }

    private static int[] parseNode(int index) { // return new int[]{metadataSum, index};
        int childNodeCount = numbers[index++];
        int metadataEntryCount = numbers[index++];
        int metadataSum = 0;
        for (int i = 0; i < childNodeCount; i++) {
            var childNodeData = parseNode(index);
            metadataSum += childNodeData[0];
            index = childNodeData[1];
        }
        for (int i = 0; i < metadataEntryCount; i++) metadataSum += numbers[index++];
        return new int[]{metadataSum, index};
    }

    private static int[] part2Parse(int index) { // return new int[]{nodeValue, index};
        int childNodeCount = numbers[index++];
        int metadataEntryCount = numbers[index++];
        int nodeValue = 0;
        if (childNodeCount == 0) for (int i = 0; i < metadataEntryCount; i++) nodeValue += numbers[index++];
        else {
            int[] childNodeValues = new int[childNodeCount+1];
            for (int i = 1; i <= childNodeCount; i++) {
                var childNodeData = part2Parse(index);
                childNodeValues[i] = childNodeData[0];
                index = childNodeData[1];
            }
            for (int i = 0; i < metadataEntryCount; i++) {
                int n = numbers[index++];
                if (n <= childNodeCount) nodeValue += childNodeValues[n];
            }
        }
        return new int[]{nodeValue, index};
    }

}
