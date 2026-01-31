package year2019;

import shared.math.PermutationGenerators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class Day7 {
    static private final int DAY = 7;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private int[] origVals;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var ss = br.readLine().split(","); origVals = new int[ss.length];
            for (int i = 0; i < ss.length; i++) origVals[i] = Integer.parseInt(ss[i]);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solve() {
        var sequences = PermutationGenerators.generatePermutations(5);
        System.out.println("\nMax thrust without feedback loop (part 1 answer): "+testSequences(sequences));
        for (var sequence : sequences) for (int i = 0; i < 5; i++) sequence[i] += 5;
        System.out.println("\nMax thrust with feedback loop (part 2 answer): "+testWithFeedbackLoop(sequences));
    }

    private static int testSequences(ArrayList<int[]> sequences) {
        int maxThrust = 0;
        for (var sequence : sequences) {
            int output = 0;
            for (int ampIndex = 0; ampIndex < 5; ampIndex++) {
                output = runStandardProgram(sequence[ampIndex], output, Arrays.copyOf(origVals, origVals.length));
            }
            if (output > maxThrust) maxThrust = output;
        }
        return maxThrust;
    }

    private static int runStandardProgram(int phaseSetting, int inputSignal, int[] vals) {
        boolean firstInputCompleted = false;
        int pos = 0, outputValue = 1;
        programLoop: while (true) {
            var s = Integer.toString(vals[pos++]); int sLength = s.length(); char lastChar = s.charAt(sLength-1);
            switch (lastChar) {
                case '9' -> { break programLoop;}
                case '3' -> {
                    vals[vals[pos++]] = firstInputCompleted ? inputSignal : phaseSetting;
                    firstInputCompleted = true;
                }
                case '4' -> outputValue = vals[vals[pos++]];
                default -> { // for all other operations:
                    int param1, param2;
                    switch (sLength) { // get param values
                        case 1, 2 -> {
                            param1 = vals[vals[pos++]];
                            param2 = vals[vals[pos++]];
                        }
                        case 3 -> {
                            param1 = s.charAt(0) == '0' ? vals[vals[pos++]] : vals[pos++];
                            param2 = vals[vals[pos++]];
                        }
                        case 4 -> {
                            param1 = s.charAt(1) == '0' ? vals[vals[pos++]] : vals[pos++];
                            param2 = s.charAt(0) == '0' ? vals[vals[pos++]] : vals[pos++];
                        }
                        case 5 -> {
                            param1 = s.charAt(2) == '0' ? vals[vals[pos++]] : vals[pos++];
                            param2 = s.charAt(1) == '0' ? vals[vals[pos++]] : vals[pos++];
                        }
                        default -> throw new RuntimeException("Invalid operation string: "+s);
                    }
                    switch (lastChar) { // perform appropriate operation
                        case '1' -> vals[vals[pos++]] = param1+param2;
                        case '2' -> vals[vals[pos++]] = param1*param2;
                        case '5' -> { if (param1 != 0) pos = param2; }
                        case '6' -> { if (param1 == 0) pos = param2; }
                        case '7' -> vals[vals[pos++]] = param1 < param2 ? 1 : 0;
                        case '8' -> vals[vals[pos++]] = param1 == param2 ? 1 : 0;
                        default -> throw new RuntimeException("Invalid final opCode character: "+lastChar);
                    }
                }
            }
        }
        return outputValue;
    }

    private static int testWithFeedbackLoop(ArrayList<int[]> sequences) {
        int maxThrust = 0;
        for (var sequence : sequences) {
            int result = runLoopyProgram(sequence);
            if (result > maxThrust) maxThrust = result;
        }
        return maxThrust;
    }

    private static int runLoopyProgram(int[] sequence) {
        int[][] valsByAmp = new int[5][origVals.length];
        for (int i = 0; i < 5; i++) valsByAmp[i] = Arrays.copyOf(origVals, origVals.length);
        int[] positions = new int[5], vals = valsByAmp[0];
        int ampNum = 0, pos = 0, outputSignal = 0;
        boolean[] firstLoopCompleted = new boolean[5];
        programLoop: while (true) {
            var s = Integer.toString(vals[pos++]); int sLength = s.length(); char lastChar = s.charAt(sLength-1);
            switch (lastChar) {
                case '9' -> { break programLoop;}
                case '3' -> {
                    vals[vals[pos++]] = firstLoopCompleted[ampNum] ? outputSignal : sequence[ampNum];
                    firstLoopCompleted[ampNum] = true;
                }
                case '4' -> {
                    outputSignal = vals[vals[pos++]];
                    positions[ampNum] = pos;
                    if (++ampNum == 5) ampNum = 0;
                    vals = valsByAmp[ampNum];
                    pos = positions[ampNum];
                }
                default -> { // for all other operations:
                    int param1, param2;
                    switch (sLength) { // get param values
                        case 1, 2 -> {
                            param1 = vals[vals[pos++]];
                            param2 = vals[vals[pos++]];
                        }
                        case 3 -> {
                            param1 = s.charAt(0) == '0' ? vals[vals[pos++]] : vals[pos++];
                            param2 = vals[vals[pos++]];
                        }
                        case 4 -> {
                            param1 = s.charAt(1) == '0' ? vals[vals[pos++]] : vals[pos++];
                            param2 = s.charAt(0) == '0' ? vals[vals[pos++]] : vals[pos++];
                        }
                        case 5 -> {
                            param1 = s.charAt(2) == '0' ? vals[vals[pos++]] : vals[pos++];
                            param2 = s.charAt(1) == '0' ? vals[vals[pos++]] : vals[pos++];
                        }
                        default -> throw new RuntimeException("Invalid operation string: "+s);
                    }
                    switch (lastChar) { // perform appropriate operation
                        case '1' -> vals[vals[pos++]] = param1+param2;
                        case '2' -> vals[vals[pos++]] = param1*param2;
                        case '5' -> { if (param1 != 0) pos = param2; }
                        case '6' -> { if (param1 == 0) pos = param2; }
                        case '7' -> vals[vals[pos++]] = param1 < param2 ? 1 : 0;
                        case '8' -> vals[vals[pos++]] = param1 == param2 ? 1 : 0;
                        default -> throw new RuntimeException("Invalid final opCode character: "+lastChar);
                    }
                }
            }
        }
        return outputSignal;
    }

}
