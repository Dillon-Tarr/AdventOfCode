package year2015.day9.test;

import java.util.ArrayList;
import java.util.HashSet;


public class Main {
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        int numberOfLocations = 8;

        for (String s : PossibilityChecker.getAllPossibilities(numberOfLocations)) System.out.println(s);
        for (String s : PossibilityChecker.getMirrorlessPossibilities(numberOfLocations)) System.out.println(s);

        ArrayList<String> possibilities = PossibilityChecker.getAllPossibilities(numberOfLocations);
        HashSet<String> mirrorlessPossibilities = PossibilityChecker.getMirrorlessPossibilities(numberOfLocations);
        System.out.println("\nPossibilities: "+possibilities.size()+"\nMirrorless possibilities: "+mirrorlessPossibilities.size());

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }
}
