package year2015.day6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Part2 {
    static private final int DAY = 6;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final int[][] lights = new int[1000][1000];
    static private final ArrayList<String> instructionStrings = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        followInstructions();
        measureBrightness();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
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

    private static void followInstructions() {
        int xStart, yStart, xEnd, yEnd, startIndex, comma, space;
        String s;
        char type;
        for (int i = 0; i < instructionStrings.size(); i++) {
            s = instructionStrings.get(i);
            if (s.charAt(1) == 'o') type = 't'; // t for toggle
            else if (s.charAt(6) == 'n') type = '1'; //1 for on
            else type = '0'; // 0 for off
            startIndex = switch(type) {
                case 't' -> 7;
                case '1' -> 8;
                case '0' -> 9;
                default -> throw new IllegalStateException("Illegal type: "+type);
            };
            s = s.substring(startIndex);
            comma = s.indexOf(',');
            space = s.indexOf(" ");
            xStart = Integer.parseInt(s.substring(0, comma));
            yStart = Integer.parseInt(s.substring(comma+1, space));
            s = s.substring(s.lastIndexOf(" "));
            comma = s.indexOf(",");
            xEnd = Integer.parseInt(s.substring(1, comma));
            yEnd = Integer.parseInt(s.substring(comma+1));
            switch (type) {
                case ('t') -> {
                    for (int x = xStart; x <= xEnd; x++) for (int y = yStart; y <= yEnd; y++) lights[x][y] += 2;
                }
                case '1' -> {
                    for (int x = xStart; x <= xEnd; x++) for (int y = yStart; y <= yEnd; y++) lights[x][y] += 1 ;
                }
                case '0' -> {
                    for (int x = xStart; x <= xEnd; x++) for (int y = yStart; y <= yEnd; y++) if (lights[x][y] != 0) lights[x][y] -= 1;
                }
            }
        }
    }

    private static void measureBrightness() {
        int brightness = 0;
        for (int x = 0; x < 1000; x++) {
            for (int y = 0; y < 1000; y++) {
                brightness += lights[x][y];
            }
        }

        System.out.println("Total brightness after all instructions: "+ brightness);
    }

}
