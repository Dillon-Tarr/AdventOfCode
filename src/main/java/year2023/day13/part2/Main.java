package year2023.day13.part2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static private final int DAY = 13;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private final ArrayList<Pattern> patterns = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        getPatternValues();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getPatternValues() {
        for (int i = 0; i < patterns.size(); i++) {
            System.out.println("\nPattern "+(i+1)+" value: "+patterns.get(i).getValue());
        }
        System.out.println("\nSum of all patterns' values:\n\n"+ Pattern.getSumOfAllValues());
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            ArrayList<String> patternRows = new ArrayList<>();
            while (currentLine != null) {
                if (!currentLine.isEmpty()) patternRows.add(currentLine);
                else {
                    patterns.add(new Pattern(patternRows));
                    patternRows.clear();
                }
                currentLine = br.readLine();
            }
            patterns.add(new Pattern(patternRows));
            patternRows.clear();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

}
