package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

class Day2 {
    static private final int DAY = 2;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private int[] originalValues;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var ss = br.readLine().split(",");
            originalValues = new int[ss.length]; for (int i = 0; i < ss.length; i++) originalValues[i] = Integer.parseInt(ss[i]);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solvePart1() {
        var vals = Arrays.copyOf(originalValues, originalValues.length);
        vals[1] = 12; vals[2] = 2;
        runProgram(vals);
        System.out.println("\nPosition 0 value after program halt (part 1 answer): "+ vals[0]);
    }

    private static void solvePart2() {
        int noun = -1, verb = 99;
        int[] vals = new int[]{0};
        while (vals[0] != 19690720) {
            if (verb == 99) { noun++; verb = 0; }
            else verb++;
            vals = Arrays.copyOf(originalValues, originalValues.length);
            vals[1] = noun; vals[2] = verb;
            runProgram(vals);
        }
        System.out.println("\n(100*noun)+verb (part 2 answer): "+((100*noun)+verb));
    }

    private static void runProgram(int[] vals) {
        int pos = 0;
        while (true) {
            int opCode = vals[pos++];
            if (opCode == 99) break;
            if (opCode == 1 || opCode == 2) {
                int operand1 = vals[vals[pos++]];
                int operand2 = vals[vals[pos++]];
                int outputIndex =  vals[pos++];
                if (opCode == 1) vals[outputIndex] = operand1+operand2;
                else vals[outputIndex] = operand1*operand2;
            } else pos += 3;
        }
    }

}
