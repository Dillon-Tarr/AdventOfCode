package day11.part2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static private final String INPUT_FILE_PATH = "input-files/day11input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    static private final ArrayList<ArrayList<Character>> universeMap = new ArrayList<>();
    static private final ArrayList<Galaxy> galaxies = new ArrayList<>();
    static private final ArrayList<Long> galaxyPairDistances = new ArrayList<>();
    static private final ArrayList<Long> yValuesWhichWhenTraversedAddOneLessThanOneMillionSteps = new ArrayList<>();
    static private final ArrayList<Long> xValuesWhichWhenTraversedAddOneLessThanOneMillionSteps = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        accountForUniverseExpansion();
        findAllGalaxiesInTheUniverse();
        findAndSumGalaxyPairDistances();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            while (currentLine != null) {
                universeMap.add(new ArrayList<>());
                char[] currentLineArray = currentLine.toCharArray();
                for (char c : currentLineArray) {
                    universeMap.get(universeMap.size()-1).add(c);
                }
                currentLine = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        System.out.println("\nUniverse map:\n");
        for (ArrayList<Character> row : universeMap) System.out.println(row);
    }

    private static void accountForUniverseExpansion() {
        for (int r = 0; r < universeMap.size(); r++) {
            ArrayList<Character> row = universeMap.get(r);
            boolean foundGalaxy = false;
            for (int c = 0; c < universeMap.get(0).size(); c++) {
                if (row.get(c) == '#') {foundGalaxy = true; break;}
            }
            if (!foundGalaxy) {
                yValuesWhichWhenTraversedAddOneLessThanOneMillionSteps.add((long)r);
            }
        }
        for (int c = 0; c < universeMap.get(0).size(); c++) {
            boolean foundGalaxy = false;
            for (int r = 0; r < universeMap.size(); r++) {
                if (universeMap.get(r).get(c) == '#') {foundGalaxy = true; break;}
            }
            if (!foundGalaxy) {
                xValuesWhichWhenTraversedAddOneLessThanOneMillionSteps.add((long)c);
            }
        }
    }

    private static void findAllGalaxiesInTheUniverse() {
        for (int r = 0; r < universeMap.size(); r++) for (int c = 0; c < universeMap.get(0).size(); c++) {
            if (universeMap.get(r).get(c) == '#') galaxies.add(new Galaxy(r, c));
        }
    }

    private static void findAndSumGalaxyPairDistances() {
        findDistanceBetweenThisGalaxyAndEveryLaterGalaxy(0);
        long sumOfGalaxyPairDistances = 0;
        for (long distance : galaxyPairDistances) sumOfGalaxyPairDistances += distance;
        System.out.println("\nSum of the distances of every galaxy pair:\n\n"+sumOfGalaxyPairDistances);
    }

    private static void findDistanceBetweenThisGalaxyAndEveryLaterGalaxy(int index) {
        if (index >= galaxies.size()-1) return;
        for (int i = index+1; i < galaxies.size(); i++) {
            long thisY = galaxies.get(index).getY(); long thatY = galaxies.get(i).getY();
            long thisX = galaxies.get(index).getX(); long thatX = galaxies.get(i).getX();
            long greaterY = Math.max(thisY, thatY); long lesserY = Math.min(thisY, thatY);
            long greaterX = Math.max(thisX, thatX); long lesserX = Math.min(thisX, thatX);
            long yDiff = greaterY - lesserY;
            long xDiff = greaterX - lesserX;

            long vastDistances = 0;
            for (long y = lesserY+1; y <= greaterY-1; y++) {
                for (Long value : yValuesWhichWhenTraversedAddOneLessThanOneMillionSteps) {
                    if (y == value) vastDistances++;
                }
            }
            for (long x = lesserX+1; x <= greaterX-1; x++) {
                for (Long value : xValuesWhichWhenTraversedAddOneLessThanOneMillionSteps) {
                    if (x == value) vastDistances++;
                }
            }
            long distance = yDiff+xDiff+vastDistances*999999;
            System.out.println(distance);
            galaxyPairDistances.add(distance);
        }
        findDistanceBetweenThisGalaxyAndEveryLaterGalaxy(index+1);
    }

}