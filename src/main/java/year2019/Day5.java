package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

class Day5 {
    static private final int DAY = 5;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private int[] origVals;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        System.out.println("\nPart 1 diagnostic code: "+runProgram(1, Arrays.copyOf(origVals, origVals.length)));
        System.out.println("\nPart 2 diagnostic code: "+runProgram(5, Arrays.copyOf(origVals, origVals.length)));

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var ss = br.readLine().split(","); origVals = new int[ss.length];
            for (int i = 0; i < ss.length; i++) origVals[i] = Integer.parseInt(ss[i]);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static int runProgram(int inputValue, int[] vals) {
        int pos = 0, outputValue = 1;
        programLoop: while (true) {
            var s = Integer.toString(vals[pos++]); int sLength = s.length(); char lastChar = s.charAt(sLength-1);
            switch (lastChar) {
                case '9' -> { break programLoop;}
                case '3' -> vals[vals[pos++]] = inputValue;
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
                        default -> throw  new RuntimeException("Invalid final opCode character: "+lastChar);
                    }
                }
            }
        }
        return outputValue;
    }

}
