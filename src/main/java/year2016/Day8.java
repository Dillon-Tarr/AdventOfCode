package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day8 {
    static private final int DAY = 8;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> instructions = new ArrayList<>();
    static private final boolean[][] pixels = new boolean[6][50];

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        processInstructions();
        countAndPrintLitPixels();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                instructions.add(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processInstructions() {
        for (String instruction : instructions) {
            switch (instruction.charAt(7)) {
                case 'r' -> {
                    int bIndex = instruction.indexOf('b');
                    rotateRow(Integer.parseInt(instruction.substring(instruction.indexOf('=')+1, bIndex-1)),
                            Integer.parseInt(instruction.substring(bIndex+3)));
                }
                case 'c' -> {
                    int bIndex = instruction.indexOf('b');
                    rotateColumn(Integer.parseInt(instruction.substring(instruction.indexOf('=')+1, bIndex-1)),
                            Integer.parseInt(instruction.substring(bIndex+3)));
                }
                default -> {
                    int xIndex = instruction.indexOf('x');
                    rect(Integer.parseInt(instruction.substring(instruction.lastIndexOf(' ')+1, xIndex)),
                            Integer.parseInt(instruction.substring(xIndex+1)));
                }
            }
        }
    }

    private static void rotateRow(int rowNumber, int amount) {
        boolean[] newStates = new boolean[pixels[rowNumber].length];
        int referenceX;
        for (int x = 0; x < newStates.length; x++) {
            referenceX = x-amount;
            if (referenceX < 0) referenceX += newStates.length;
            newStates[x] = pixels[rowNumber][referenceX];
        }
        for (int x = 0; x < newStates.length; x++) pixels[rowNumber][x] = newStates[x];
    }

    private static void rotateColumn(int columnNumber, int amount) {
        boolean[] newStates = new boolean[pixels.length];
        int referenceY;
        for (int y = 0; y < newStates.length; y++) {
            referenceY = y-amount;
            if (referenceY < 0) referenceY += newStates.length;
            newStates[y] = pixels[referenceY][columnNumber];
        }
        for (int y = 0; y < newStates.length; y++) pixels[y][columnNumber] = newStates[y];
    }

    private static void rect(int xMax, int yMax) {
        for (int y = 0; y < yMax; y++) for (int x = 0; x < xMax; x++) pixels[y][x] = true;
    }

    private static void countAndPrintLitPixels() {
        int count = 0;
        System.out.println("\nCode attempting to be displayed:");
        for (int y = 0; y < pixels.length; y++) {
            System.out.println();
            for (int x = 0; x < pixels[0].length; x++) {
                if (pixels[y][x]) {
                    System.out.print("##");
                    count++;
                } else System.out.print("  ");
            }
            System.out.println();
            for (int x = 0; x < pixels[0].length; x++) {
                if (pixels[y][x]) System.out.print("##");
                else System.out.print("  ");
            }
        }
        System.out.println("\n\nNumber of lit pixels after following instructions: "+count);
    }

}
