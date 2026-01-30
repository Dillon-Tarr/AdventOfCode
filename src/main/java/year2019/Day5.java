package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

class Day5 {
    static private final int DAY = 5;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private final int inputValue = 1;
    static private int outputValue = -1;
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
        runProgram(vals);
        System.out.println("\nDiagnostic code: "+outputValue);
    }

    private static void solvePart2() {

    }

    private static void runProgram(int[] vals) {
        int pos = 0;
        programLoop: while (true) {
            var sb = new StringBuilder(Integer.toString(vals[pos++]));
            for (int i = sb.length(); i < 5; i++) sb.insert(0, '0');
            var opCode = sb.substring(3);
            switch (opCode) {
                case "99" -> { break programLoop; }
                case "01", "02" -> {
                    int operand1 = sb.charAt(2) == '0' ? vals[vals[pos++]] : vals[pos++];
                    int operand2 = sb.charAt(1) == '0' ? vals[vals[pos++]] : vals[pos++];
                    int outputIndex = vals[pos++];
                    if (opCode.equals("01")) vals[outputIndex] = operand1+operand2;
                    else vals[outputIndex] = operand1*operand2;
                }
                case "03" -> vals[vals[pos++]] = inputValue;
                case "04" -> outputValue = vals[vals[pos++]];
            }
        }
    }

}
