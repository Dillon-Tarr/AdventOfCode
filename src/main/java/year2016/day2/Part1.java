package year2016.day2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part1 {
    static private final int DAY = 2;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> instructionStrings = new ArrayList<>(5);

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        getBathroomCode();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                instructionStrings.add(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void getBathroomCode() {
        StringBuilder codeBuilder = new StringBuilder();
        int k = 5; // for key
        for (String instructionString : instructionStrings) {
            for (int i = 0; i < instructionString.length(); i++) {
                switch (instructionString.charAt(i)) {
                    case 'U' -> {
                        switch (k) {
                            case 1, 2, 3 -> {}
                            default -> k -= 3;
                        }
                    }
                    case 'D' -> {
                        switch (k) {
                            case 7, 8, 9 -> {}
                            default -> k += 3;
                        }
                    }
                    case 'L' -> {
                        switch (k) {
                            case 1, 4, 7 -> {}
                            default -> k -= 1;
                        }
                    }
                    case 'R' -> {
                        switch (k) {
                            case 3, 6, 9 -> {}
                            default -> k += 1;
                        }
                    }
                    default -> throw new IllegalArgumentException("Invalid string character: "+instructionString.charAt(i));
                }
            }
            codeBuilder.append(k);
        }
        System.out.println("\nBathroom code: "+codeBuilder);
    }

}
