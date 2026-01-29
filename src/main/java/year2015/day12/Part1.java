package year2015.day12;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Part1 {
    static private final int DAY = 12;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private String inputString;
    static private int sum = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputString();
        findAndSumNumbers();

        System.out.println("\nSum of all numbers in the document: "+sum);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputString() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void findAndSumNumbers() {
        boolean numberMode = false;
        int currentNumberStartIndex = Integer.MIN_VALUE;
        for (int i = 0; i < inputString.length(); i++) {
            if (numberMode) {
                switch (inputString.charAt(i)) {
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {}
                    default -> {
                        numberMode = false;
                        sum += Integer.parseInt(inputString.substring(currentNumberStartIndex, i));
                    }
                }
            } else {
                switch (inputString.charAt(i)) {
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                        numberMode = true;
                        currentNumberStartIndex = inputString.charAt(i-1) == '-' ? i-1 : i;
                    }
                }
            }
        }
    }

}
