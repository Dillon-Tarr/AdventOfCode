package year2023.day11.part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static private final int DAY = 11;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private final ArrayList<ArrayList<Character>> universeMap = new ArrayList<>();
    static private final ArrayList<Galaxy> galaxies = new ArrayList<>();
    static private final ArrayList<Integer> galaxyPairDistances = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        accountForUniverseExpansion();
        findAllGalaxiesInTheUniverse();
        findAndSumGalaxyPairDistances();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void findAndSumGalaxyPairDistances() {
        findDistanceBetweenThisGalaxyAndEveryLaterGalaxy(0);
        int sumOfGalaxyPairDistances = 0;
        for (int distance : galaxyPairDistances) sumOfGalaxyPairDistances += distance;
        System.out.println("\nSum of the distances of every galaxy pair:\n\n"+sumOfGalaxyPairDistances);
    }

    private static void findDistanceBetweenThisGalaxyAndEveryLaterGalaxy(int index) {
        if (index >= galaxies.size()-1) return;
        for (int i = index+1; i < galaxies.size(); i++) {
            int yDiff = galaxies.get(i).getY() - galaxies.get(index).getY();
            int xDiff = galaxies.get(i).getX() - galaxies.get(index).getX();
            if (yDiff < 0) yDiff *= -1;
            if (xDiff < 0) xDiff *= -1;
            galaxyPairDistances.add(yDiff+xDiff);
        }
        findDistanceBetweenThisGalaxyAndEveryLaterGalaxy(index+1);
    }


    private static void findAllGalaxiesInTheUniverse() {
        for (int r = 0; r < universeMap.size(); r++) for (int c = 0; c < universeMap.get(0).size(); c++) {
            if (universeMap.get(r).get(c) == '#') galaxies.add(new Galaxy(r, c));
        }
    }

    private static void accountForUniverseExpansion() {
        for (int r = universeMap.size()-1; r >= 0; r--) {
            ArrayList<Character> row = universeMap.get(r);
            boolean foundGalaxy = false;
            for (int c = universeMap.get(0).size()-1; c >= 0; c--){
                if (row.get(c) == '#') {foundGalaxy = true; break;}
            }
            if (!foundGalaxy) {
                universeMap.add(r, new ArrayList<>(row));
            }
        }
        for (int c = universeMap.get(0).size()-1; c >= 0; c--) {
            boolean foundGalaxy = false;
            for (int r = universeMap.size()-1; r >=0 ; r--) {
                if (universeMap.get(r).get(c) == '#') {foundGalaxy = true; break;}
            }
            if (!foundGalaxy) {
                for (int r = universeMap.size()-1; r >=0 ; r--) {
                    universeMap.get(r).add(c, '.');
                }
            }
        }
        System.out.println("\nUniverse map, accounting for expansion:\n");
        for (ArrayList<Character> row : universeMap) System.out.println(row);
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            while (currentLine != null) {
                universeMap.add(new ArrayList<>());
                char[] currentLineArray = currentLine.toCharArray();
                for (char c : currentLineArray) {
                    universeMap.get(universeMap.size() - 1).add(c);
                }
                currentLine = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        System.out.println("\nUniverse map:\n");
        for (ArrayList<Character> row : universeMap) System.out.println(row);
    }
}