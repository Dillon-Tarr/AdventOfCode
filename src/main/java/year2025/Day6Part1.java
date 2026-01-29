package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day6Part1 {
    static private final int DAY = 6;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final ArrayList<int[]> operandArrays = new ArrayList<>();
    static private boolean[] operatorIsAddition;


    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        var inputStrings = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s.trim());
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (var s : inputStrings) {
            switch (s.charAt(0)) {
                case '*', '+' -> {
                    operatorIsAddition = new boolean[operandArrays.getFirst().length];
                    int operandI = 0;
                    for (int i = 0; i < s.length(); i++) {
                        switch (s.charAt(i)) {
                            case '+' -> operatorIsAddition[operandI++] = true;
                            case '*' -> operandI++;
                        }
                    }
                }
                default -> {
                    String[] strings = s.split("\\s+");
                    int[] ints = new int[strings.length];
                    for (int i = 0; i < strings.length; i++) ints[i] = Integer.parseInt(strings[i]);
                    operandArrays.add(ints);
                }
            }
        }

    }

    private static void solve() {
        long total = 0;
        for (int i = 0; i < operatorIsAddition.length; i++) {
            long value = 0;
            if (operatorIsAddition[i]) {
                for (var array : operandArrays) value += array[i];
            } else {
                value = 1;
                for (var array : operandArrays) value *= array[i];
            }
            total += value;
        }
        System.out.println("\nGrand total (part 1 answer): "+total);
    }

}
