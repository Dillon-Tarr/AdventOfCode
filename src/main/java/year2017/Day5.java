package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day5 {
    static private final int DAY = 5;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private ArrayList<Instruction> instructions;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        instantiateInstructions();
        countStepsForPart1();
        instantiateInstructions();
        countStepsForPart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void instantiateInstructions() {
        instructions = new ArrayList<>();
        for (String s : inputStrings) instructions.add(new Instruction(Integer.parseInt(s)));
    }

    private static void countStepsForPart1() {
        int instructionNumber = 0, stepCount = 0;
        while (instructionNumber >= 0 && instructionNumber < instructions.size()) {
            stepCount++;
            instructionNumber += instructions.get(instructionNumber).execute();
        }
        System.out.println("\nSteps taken to escape instruction set with part 1 rules: "+stepCount);
    }

    private static void countStepsForPart2() {
        int instructionNumber = 0, stepCount = 0;
        while (instructionNumber >= 0 && instructionNumber < instructions.size()) {
            stepCount++;
            instructionNumber += instructions.get(instructionNumber).part2Execute();
        }
        System.out.println("\nSteps taken to escape instruction set with part 2 rules: "+stepCount);
    }

    private static class Instruction {
        int d;

        Instruction(int initialJumpDistanceValue) {
            d = initialJumpDistanceValue;
        }

        int execute() {
            return d++;
        }

        int part2Execute() {
            return d < 3 ? d++ : d--;
        }
    }

}
