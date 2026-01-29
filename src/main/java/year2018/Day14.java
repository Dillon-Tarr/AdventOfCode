package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day14 {
    static private final int DAY = 14;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private int[] specialInts;
    static private int recipeCount = 2, neededRecipeCount, elf1Position = 0, elf2Position = 1, specialIntCount;
    static private ArrayList<Integer> recipeScores;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            specialInts = new int[s.length()];
            specialIntCount = specialInts.length;
            for (int i = 0; i < s.length(); i++) specialInts[i] = Character.getNumericValue(s.charAt(i));
            neededRecipeCount = 10+Integer.parseInt(s);
        } catch (IOException e) {throw new RuntimeException(e);}
        recipeScores = new ArrayList<>(neededRecipeCount+1);
        recipeScores.add(3); recipeScores.add(7);
    }

    private static void solve() {
        int score1 = 3, score2 = 7, combinedScore, indexOfSpecialFind = -1, bigI;
        boolean matchFound = false;
        while (!matchFound || recipeCount < neededRecipeCount) {
            elf1Position += 1+score1; while (elf1Position >= recipeCount) elf1Position -= recipeCount;
            elf2Position += 1+score2; while (elf2Position >= recipeCount) elf2Position -= recipeCount;
            score1 = recipeScores.get(elf1Position); score2 = recipeScores.get(elf2Position); combinedScore = score1+score2;
            if (combinedScore < 10) { recipeScores.add(combinedScore); recipeCount++; }
            else {
                recipeScores.add(1); recipeScores.add(combinedScore-10); recipeCount+=2;
                if (!matchFound && recipeCount > specialIntCount) { // check last recipes -1 because TWO were added
                    bigI = recipeCount-1; matchFound = true;
                    for (int i = specialIntCount-1; i >= 0; i--) if (specialInts[i] != recipeScores.get(--bigI)) { matchFound = false; break; }
                    if (matchFound) indexOfSpecialFind = bigI;
                }
            }
            if (!matchFound && recipeCount >= specialIntCount) { // check last recipes either way
                bigI = recipeCount; matchFound = true;
                for (int i = specialIntCount-1; i >= 0; i--) if (specialInts[i] != recipeScores.get(--bigI)) { matchFound = false; break; }
                if (matchFound) indexOfSpecialFind = bigI;
            }
        }
        var sb = new StringBuilder();
        for (int i = neededRecipeCount-10; i < neededRecipeCount; i++) sb.append(recipeScores.get(i));
        System.out.println("\n10 recipe scores after expected improvement (part 1 answer): "+sb);
        System.out.println("\nNumber of recipes before special score sequence (part 2 answer): "+indexOfSpecialFind);
    }

}
