package year2015.day2.part2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Main {
    static private final int DAY = 2;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");

    static private int totalRibbonNeeded = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();

        System.out.println("Total ribbon needed: "+ totalRibbonNeeded);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void processInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            while(!(currentLine == null)) {
                Box box = new Box(currentLine);
                totalRibbonNeeded += box.ribbonNeeded;
                currentLine = br.readLine();
            }

        } catch (IOException e) {throw new RuntimeException(e);}
    }

}
