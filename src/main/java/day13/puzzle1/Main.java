package day13.puzzle1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    static private final String INPUT_FILE_PATH = "input-files/day13input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    static private final ArrayList<Pattern> patterns = new ArrayList<>();


    public static void main(String[] args) {
        getInputData();
        getPatternValues();
    }

    private static void getPatternValues() {
        for (int i = 0; i < patterns.size(); i++) {
            System.out.println("\nPattern "+(i+1)+" (value: "+patterns.get(i).getValue()+"):\n"+patterns.get(i));
        }
        System.out.println("\nSum of all patterns' values:\n\n"+Pattern.getSumOfAllValues());
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
