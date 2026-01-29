package year2015.day8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Part2 {
    static private final int DAY = 8;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private int numberOfNewCodeCharsNeededForLiterals = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getAndProcessInputData();

        System.out.println("\nNumber of new code characters needed for literals to represent the input strings (answer): "+ numberOfNewCodeCharsNeededForLiterals);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                numberOfNewCodeCharsNeededForLiterals += 4; // for new surrounding quote marks for string literal plus escape slashes for originals
                for (int j = 1; j < inputString.length()-1; j++) {
                    switch (inputString.charAt(j)) {
                        case '\\', '\"' -> numberOfNewCodeCharsNeededForLiterals++;
                    }
                }
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

}
