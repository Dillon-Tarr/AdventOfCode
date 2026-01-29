package year2015.day1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Part2 {
    static private final int DAY = 1;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");

    static private String inputString;
    static private int floorChangeCount = 0;
    static private int currentFloor = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        traverse();

        System.out.println("Floor change count upon reaching basement (floor -1): "+floorChangeCount);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            StringBuilder sb = new StringBuilder();
            String currentLine = br.readLine();
            while(!(currentLine == null)) {
                sb.append(currentLine);
                currentLine = br.readLine();
            }
            inputString = sb.toString();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void traverse(){
        for (int i = 0; i < inputString.length(); i++) {
            floorChangeCount++;
            switch (inputString.charAt(i)) {
                case '(' -> currentFloor++;
                case ')' -> currentFloor--;
            }
            if (currentFloor == -1) break;
        }
    }

}
