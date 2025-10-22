package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

class Day19 {
    static private final int DAY = 19;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private int numberOfElves;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        quickEvaluate();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void quickEvaluate() {
        System.out.println("If number of elves is "+numberOfElves+", number of elf who gets all the presents: "+fancyEvaluate(numberOfElves));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            numberOfElves = Integer.parseInt(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static int fancyEvaluate(int numberOfElves) {
        int twoPower = 1, next;
        while (true) {
            next = twoPower*2;
            if (next > numberOfElves) break;
            else twoPower = next;
        }
        return 1+((numberOfElves-twoPower)*2);
    }

    private static int naiveEvaluateToDeriveFunction(int numberOfElves) {
        LinkedList<Integer> elfNumbers = new LinkedList<>();
        for (int i = 1; i <= numberOfElves; i++) elfNumbers.add(i);
        int i = 1;
        while (elfNumbers.size() > 1) {
            if (i == elfNumbers.size()) elfNumbers.removeFirst();
            else elfNumbers.remove(i);
            if (++i > elfNumbers.size()) i = 1;
        }
        return elfNumbers.element();
    }

}
