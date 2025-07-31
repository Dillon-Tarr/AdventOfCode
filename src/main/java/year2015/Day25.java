package year2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day25 {
    static private final int DAY = 25;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private int goalRow, goalColumn;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInput();
        getAnswer();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void processInput() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            int i1, i2;
            i2 = inputString.lastIndexOf(',');
            i1 = inputString.substring(0, i2).lastIndexOf(' ');
            goalRow = Integer.parseInt(inputString.substring(i1+1, i2));
            i1 = 1+inputString.lastIndexOf(' ');
            goalColumn = Integer.parseInt(inputString.substring(i1, inputString.length()-1));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void getAnswer() {
        int diagonalSourceRow = goalRow+goalColumn-1;
        int codeIndex = 0;
        long currentNumber = 20151125;
        for (int i = 1; i < diagonalSourceRow; i++) codeIndex += i;
        codeIndex+=goalColumn;
        for (int i = 2; i <= codeIndex; i++) currentNumber = currentNumber * 252533 % 33554393;
        System.out.println("\nNumber number "+codeIndex+" at row "+goalRow+", column "+goalColumn+": "+currentNumber);
    }

}
