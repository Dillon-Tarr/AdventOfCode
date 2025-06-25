package year2023.day21.part2;

import java.io.*;

public class Main {
    static private final int DAY = 21;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static int gridWidth = 131; // Only one value needed here, since the grid is square.
    static char[][] inputCharacters = new char[gridWidth][gridWidth];
    static private final int requiredSteps = 26501365;
    static private final int gridWidthStepCounts = requiredSteps /gridWidth;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        createGrids();
        System.out.println("\nSum of all reachable garden plots in "+requiredSteps+" steps: "+Grid.getSumOfPlotsReachedOnAllGrids());

        System.out.println("\nExecution time in seconds: " + ((double) (System.nanoTime() - startTime) / 1000000000));
    }

    private static void processInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line = br.readLine();
            char character;
            for (int y = 0; y < gridWidth; y++) {
                for (int x = 0; x < gridWidth; x++) {
                    character = line.charAt(x);
                    if (character == 'S') inputCharacters[y][x] = '.';
                    else inputCharacters[y][x] = character;
                }
                line = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void createGrids() {
        new Grid("Grid filled completely from center with odd parity", (long) Math.pow(((Math.floorDiv(gridWidthStepCounts-1, 2)*2)+1), 2),
                inputCharacters, 65, 65, 1001);
        new Grid("Grid filled completely from center with even parity", (long) Math.pow((Math.floorDiv(gridWidthStepCounts, 2)*2), 2),
                inputCharacters, 65, 65, 1000);

        new Grid("Eastmost grid partly filled from the west center", 1,
                inputCharacters, 65, 0, 130);
        new Grid("Southmost grid partly filled from the north center", 1,
                inputCharacters, 0, 65, 130);
        new Grid("Westmost grid partly filled from the east center", 1,
                inputCharacters, 65, 130, 130);
        new Grid("Northmost grid partly filled from the south center", 1,
                inputCharacters, 130, 65, 130);

        new Grid("North-east edge grid slightly filled from SW corner", gridWidthStepCounts,
                inputCharacters, 130, 0, 64);
        new Grid("South-east edge grid slightly filled from NW corner", gridWidthStepCounts,
                inputCharacters, 0, 0, 64);
        new Grid("South-west edge grid slightly filled from NE corner", gridWidthStepCounts,
                inputCharacters, 0, 130, 64);
        new Grid("North-west edge grid slightly filled from SE corner", gridWidthStepCounts,
                inputCharacters, 130, 130, 64);

        new Grid("North-east near-edge grid mostly filled from SW corner", gridWidthStepCounts-1,
                inputCharacters, 130, 0, 195);
        new Grid("South-east near-edge grid mostly filled from NW corner", gridWidthStepCounts-1,
                inputCharacters, 0, 0, 195);
        new Grid("South-west near-edge grid mostly filled from NE corner", gridWidthStepCounts-1,
                inputCharacters, 0, 130, 195);
        new Grid("North-west near-edge grid mostly filled from SE corner", gridWidthStepCounts-1,
                inputCharacters, 130, 130, 195);
    }

}
