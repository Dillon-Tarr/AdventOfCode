package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day1 {
    static private final int DAY = 1;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private char[] inputChars;
    static private int length;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputChars = br.readLine().toCharArray();
        } catch (IOException e) {throw new RuntimeException(e);}
        length = inputChars.length;
    }

    private static void solvePart1() {
        char c1 = inputChars[length-1], c2;
        int sum = 0;
        for (int i = 0; i < length; i++) {
            c2 = c1;
            c1 = inputChars[i];
            if (c1 == c2) sum += Character.getNumericValue(c1);
        }
        System.out.println("\nSum of digits that match following digit (part 1): "+sum);
    }

    private static void solvePart2() {
        char c1, c2;
        int sum = 0, o = (length/2)-1;
        for (int i = 0; i < length; i++) {
            if (++o >= length) o -= length;
            c1 = inputChars[i];
            c2 = inputChars[o];
            if (c1 == c2) sum += Character.getNumericValue(c1);
        }
        System.out.println("\nSum of digits that match opposite digit (part 2): "+sum);
    }

}
