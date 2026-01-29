package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day17 {
    static private final int DAY = 17;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private int stepSize;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            stepSize = Integer.parseInt(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solve() {
        ArrayList<Integer> buffer = new ArrayList<>();
        buffer.add(0);
        int currentPosition = 0;
        for (int nextValue = 1; nextValue <= 2017; nextValue++) {
            currentPosition += stepSize;
            while (currentPosition >= nextValue) currentPosition -= nextValue;
            buffer.add(++currentPosition, nextValue);
        }
        System.out.println("\nValue after 2017 (part 1 answer): "+buffer.get(currentPosition+1));
        int index1Value = buffer.get(1);
        for (int nextValue = 2018; nextValue <= 50_000_000; nextValue++) {
            currentPosition += stepSize;
            while (currentPosition >= nextValue) currentPosition -= nextValue;
            if (currentPosition++ == 0) index1Value = nextValue;
        }
        System.out.println("\nValue after 0 (i.e. at index 1) after 50,000,000 is inserted (part 2 answer): "+index1Value);
    }

}
