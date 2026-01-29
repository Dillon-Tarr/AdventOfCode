package year2016.day2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part2 {
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
        char k = '5'; // for key
        for (String instructionString : instructionStrings) {
            for (int i = 0; i < instructionString.length(); i++) {
                switch (instructionString.charAt(i)) {
                    case 'U' -> {
                        switch (k) {
                            case '3' -> k = '1';
                            case '6' -> k = '2';
                            case '7' -> k = '3';
                            case '8' -> k = '4';
                            case 'A' -> k = '6';
                            case 'B' -> k = '7';
                            case 'C' -> k = '8';
                            case 'D' -> k = 'B';
                        }
                    }
                    case 'D' -> {
                        switch (k) {
                            case '1' -> k = '3';
                            case '2' -> k = '6';
                            case '3' -> k = '7';
                            case '4' -> k = '8';
                            case '6' -> k = 'A';
                            case '7' -> k = 'B';
                            case '8' -> k = 'C';
                            case 'B' -> k = 'D';
                        }
                    }
                    case 'L' -> {
                        switch (k) {
                            case '1', '2', '5', 'A', 'D' -> {}
                            default -> k -= 1;
                        }
                    }
                    case 'R' -> {
                        switch (k) {
                            case '1', '4', '9', 'C', 'D' -> {}
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
