package day15.puzzle1;

import java.io.*;

public class Main {
    static private final File INPUT_FILE = new File("input-files/day15input.txt");
    static private String[] steps;

    public static void main(String[] args) {
        getInputData();
        getValues();
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            StringBuilder sb = new StringBuilder();
            String currentLine = br.readLine();
            while(!(currentLine == null)) {
                sb.append(currentLine);
                currentLine = br.readLine();
            }
            steps = sb.toString().split(",");
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void getValues() {
        int sumOfValues = 0;
        for (String step : steps) {
            int value = 0;
            for (int i = 0; i < step.length(); i++) {
                value = (value+step.charAt(i))*17%256;
            }
            sumOfValues += value;
        }
        System.out.println("\nSum of values:\n\n"+sumOfValues);
    }

}
