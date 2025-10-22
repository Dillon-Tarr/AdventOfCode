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

    static void main(String[] args) {
        long startTime = System.nanoTime();

      //printFirstHundredForFunctionDerivation(args.length != 0);
        getInputData();
        evaluate(args.length != 0); // 0 args for part 1, >0 args for part 2;

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void printFirstHundredForFunctionDerivation(boolean part2Mode) {
        if (part2Mode) for (int i = 1; i <= 100; i++) System.out.println(i+","+naiveEvaluateForPart2FunctionDerivation(i));
        else for (int i = 1; i <= 100; i++) System.out.println(i+","+naiveEvaluateForPart1FunctionDerivation(i));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            numberOfElves = Integer.parseInt(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void evaluate(boolean part2Mode) {
        int answer = part2Mode ? evaluatePart2WithDerivedFunction(numberOfElves) : evaluatePart1WithDerivedFunction(numberOfElves);
        System.out.println("\nIf number of elves is "+numberOfElves+", number of elf who gets all the presents: "+answer);
    }

    private static int evaluatePart1WithDerivedFunction(int numberOfElves) {
        int twoPower = 1, next;
        while (true) {
            next = twoPower*2;
            if (next > numberOfElves) break;
            twoPower = next;
        }
        return 1+((numberOfElves-twoPower)*2);
    }

    private static int evaluatePart2WithDerivedFunction(int numberOfElves) {
        int threePower = 1, tripleThreePower;
        while (true) {
            tripleThreePower = threePower*3;
            if (tripleThreePower > numberOfElves) break;
            threePower = tripleThreePower;
        }
        if (numberOfElves == threePower) return threePower;
        else if (numberOfElves <= threePower*2) return numberOfElves-threePower;
        else return tripleThreePower-(2*(tripleThreePower-numberOfElves));
    }

    private static int naiveEvaluateForPart1FunctionDerivation(int numberOfElves) {
        LinkedList<Integer> elfNumbers = new LinkedList<>();
        for (int i = 1; i <= numberOfElves; i++) elfNumbers.add(i);
        int i = 1;
        while (numberOfElves > 1) {
            if (i == numberOfElves) elfNumbers.removeFirst();
            else elfNumbers.remove(i);
            if (++i > --numberOfElves) i = 1;
        }
        return elfNumbers.element();
    }

    private static int naiveEvaluateForPart2FunctionDerivation(int numberOfElves) {
        LinkedList<Integer> elfNumbers = new LinkedList<>();
        for (int i = 1; i <= numberOfElves; i++) elfNumbers.add(i);
        int i = 0, oppositeIndex;
        while (numberOfElves > 1) {
            oppositeIndex = (numberOfElves/2)+i;
            if (oppositeIndex >= numberOfElves) {
                oppositeIndex -= numberOfElves;
                i--;
            }
            elfNumbers.remove(oppositeIndex);
            if (++i >= --numberOfElves) i = 0;
        }
        return elfNumbers.element();
    }

}
