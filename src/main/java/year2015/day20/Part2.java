package year2015.day20;

import shared.math.FactorFinders;
import shared.math.PrimeFinders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part2 {
    static private final int DAY = 20;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private int goalPresentCount, specialHouseNumber = -1;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        boolean usePrecalculatedPrimesMode = args.length == 0;

        getInputData();
        findSpecialHouseNumber();
        System.out.println("\nExecution time for finding the house number, in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
        getElfNumbersForFun(usePrecalculatedPrimesMode);

        System.out.println("\nTotal execution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            goalPresentCount = Integer.parseInt(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void findSpecialHouseNumber() {
        int[] presentsPerHouse = new int[goalPresentCount/11];
        int elfNumber = 0, deliveriesByElf, specialHousePresentCount = -1;
        while (specialHouseNumber == -1) {
            elfNumber++;
            deliveriesByElf = 0;
            for (int i = elfNumber; i <presentsPerHouse.length; i+=elfNumber) {
                presentsPerHouse[i] += elfNumber*11;
                if (++deliveriesByElf == 50) break;
                if (i == elfNumber && presentsPerHouse[i] >= goalPresentCount) {
                    specialHouseNumber = i;
                    specialHousePresentCount = presentsPerHouse[i];
                    break;
                }
            }
        }
        System.out.println("\nNumber of first house to receive at least "+ goalPresentCount +" presents: "+specialHouseNumber);
        System.out.println("Number of presents that house received: "+ specialHousePresentCount);
    }

    private static void getElfNumbersForFun(boolean usePrecalculatedPrimesMode) {
        System.out.println("\nNow investigating which elves delivered to that house...");
        if (!usePrecalculatedPrimesMode) System.out.println("This will take a second or two, since you've chosen to find prime numbers at runtime.");
        ArrayList<Integer> elfNumbers = usePrecalculatedPrimesMode
                ? FactorFinders.findAllNonOneFactors(specialHouseNumber, PrimeFinders.getPrimesFromFile(new File("src/main/java/shared/math/primes-to-one-million.txt"), true, Integer.MAX_VALUE))
                : FactorFinders.findAllNonOneFactors(specialHouseNumber);
        elfNumbers.add(0, 1);
        ArrayList<Integer> elvesWhoQuitBeforeReachingTheSpecialHouse = new ArrayList<>();
        for (int i = 0; i < elfNumbers.size(); i++) {
            int elfNumber = elfNumbers.get(i);
            if (elfNumber*50 >= specialHouseNumber) break;
            elvesWhoQuitBeforeReachingTheSpecialHouse.add(elfNumber);
        }
        elfNumbers.removeAll(elvesWhoQuitBeforeReachingTheSpecialHouse);
        System.out.println("\nElf numbers of those who delivered presents to that house: "+elfNumbers);
        System.out.println("\nElves who delivered to 50 houses and quit before reaching that house:");
        for (int i = 0; i < elvesWhoQuitBeforeReachingTheSpecialHouse.size(); i++) {
            int quitterNumber = elvesWhoQuitBeforeReachingTheSpecialHouse.get(i);
            System.out.println("<*~Elf #"+quitterNumber+", at house "+(quitterNumber*50)+"~*>");
        }
    }

}
