package year2015.day20.part1;

import shared.math.FactorFinders;
import shared.math.PrimeFinders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class NaiveApproach {
    static private final int DAY = 20;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private int specialPresentCount;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        findSpecialHouseNumber(args.length == 0);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            specialPresentCount = Integer.parseInt(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void findSpecialHouseNumber(boolean usePrecalculatedPrimesMode) {
        ArrayList<Integer> primes = usePrecalculatedPrimesMode
                ? PrimeFinders.getPrimesFromFile(new File("src/main/java/shared/math/primes-to-one-million.txt"), true, Integer.MAX_VALUE)
                : PrimeFinders.findPrimesUntilValue(5);
        int houseNumber = 0, presentCount = 0, presentRecord = 0;
        int[] particularFactors = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 60, 420};
        ArrayList<Integer> nonOneFactors = new ArrayList<>();
        while (presentCount < specialPresentCount) { // Factoring out 10 from all present counts.
            if (houseNumber >= 3120) houseNumber += 60; // All new present records starting at house #3120 (record: 104160) are divisible by 5 and therefore by 60.
            else if (houseNumber >= 660) houseNumber += 12; // All new present records starting at house #660 (record: 20160) are divisible by 4 (2x2) and therefore by 12.
            else if (houseNumber >= 24) houseNumber += 6; // All new present records starting at house #24 (record: 600) are divisible by 3 and therefore 6.
            else houseNumber+=2; // Changed from ++ after confirming odd numbers are irrelevant in testing.
            presentCount = 10;
            nonOneFactors = FactorFinders.findAllNonOneFactors(houseNumber, primes);
            for (Integer factor : nonOneFactors) presentCount += 10*factor;
            if (presentCount > presentRecord) {
                System.out.println("\nNew present record: "+presentCount+"; Reached at house #"+houseNumber+";\n"+
                        "Record increased by "+(presentCount-presentRecord)+" ("+String.format("%.3f",(((double)presentCount-(double)presentRecord)/(double)presentRecord)*100d)+"%)");
                presentRecord = presentCount;
                for (int i : particularFactors) {
                    if (houseNumber > i && houseNumber % i != 0) {
                        System.out.println("New record's house number is not divisible by: '"+i+"'");
                    }
                }
            }
        }
        nonOneFactors.add(0, 1);
        System.out.println("\nNumber of first house to receive at least "+specialPresentCount+" presents: "+houseNumber);
        System.out.println("Number of presents that house received: "+ presentCount);
        System.out.println("Elf numbers of those who delivered presents to that house: "+nonOneFactors);
    }

}
