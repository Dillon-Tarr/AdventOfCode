package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

class Day6 {
    static private final int DAY = 6;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private String inputString;
    static private MemoryBank[] memoryBanks;
    static private int length, oneLessThanLength;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        instantiateMemoryBanks();
        executeReallocationRoutine(); // gets part 1 answer
        countCyclesUntilCurrentStateIsSeenAgain(); // uses state reached in part 1 answer and gets part 2 answer

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void instantiateMemoryBanks() {
        String[] initialValues = inputString.split("\\s+");
        memoryBanks = new MemoryBank[initialValues.length];
        length = memoryBanks.length;
        for (int i = 0; i < length; i++) memoryBanks[i] = new MemoryBank(Integer.parseInt(initialValues[i]));
        oneLessThanLength = length-1;
    }

    private static void executeReallocationRoutine() {
        HashSet<String> seenStates = new HashSet<>();
        int completedCycles = 0;
        while (seenStates.add(getCurrentStateString())) {
            performReallocationCycle();
            completedCycles++;
        }
        System.out.println("\nReallocation cycles performed before reproducing a previously seen configuration (part 1 answer): "+ completedCycles);
    }

    private static String getCurrentStateString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < oneLessThanLength; i++) sb.append(memoryBanks[i].blocks).append(',');
        sb.append(memoryBanks[oneLessThanLength].blocks);
        return sb.toString();
    }

    private static void performReallocationCycle() {
        int blocks, mostBlocks = -1, bankIndex = -1;
        for (int i = oneLessThanLength; i >= 0; i--) {
            blocks = memoryBanks[i].blocks;
            if (blocks >= mostBlocks) {
                bankIndex = i;
                mostBlocks = blocks;
            }
        }
        int blocksToDistribute = memoryBanks[bankIndex].removeBlocksForReallocation();
        while (blocksToDistribute > 0) {
            if (++bankIndex >= length) bankIndex -= length;
            memoryBanks[bankIndex].addBlock();
            blocksToDistribute--;
        }
    }

    private static void countCyclesUntilCurrentStateIsSeenAgain() {
        String loopState = getCurrentStateString();
        int completedCycles = 0;
        do {
            performReallocationCycle();
            completedCycles++;
        } while (!getCurrentStateString().equals(loopState));
        System.out.println("\nReallocation cycles performed to reach that previously seen configuration again (part 2 answer): "+completedCycles);
    }

    private static class MemoryBank {
        int blocks;

        MemoryBank(int initialValue) {
            blocks = initialValue;
        }

        private int removeBlocksForReallocation() {
            int b = blocks;
            blocks = 0;
            return b;
        }

        private void addBlock() {
            blocks++;
        }

    }

}
