package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Day12 {
    static private final int DAY = 12;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final ArrayList<Instruction> instructions = new ArrayList<>();
    static Register a = new Register(), b = new Register(), c = new Register(), d = new Register();

    static void main(String[] args) { // Run with more than 0 arguments for part 2 mode.
        long startTime = System.nanoTime();

        if (args.length != 0) c.value = 1; // Part 2 mode initializes register c to have the value 1 rather than 0.
        getInputData();
        processInputData();
        executeInstructions();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString;
            inputString = br.readLine();
            while (inputString != null) {
                inputStrings.add(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processInputData() {
        Register r1, r2;
        int lastSpaceIndex, n1, n2;
        for (String s : inputStrings) {
            r1 = switch (s.charAt(4)) { case 'a' -> a; case 'b' -> b; case 'c' -> c; case 'd' -> d; default -> null; };
            lastSpaceIndex = s.lastIndexOf(' ');
            if (lastSpaceIndex == 3 && r1 == null) throw new IllegalStateException("Unexpected value: " + s.charAt(4));
            switch (s.charAt(0)) {
                case 'i' -> instructions.add(new Inc(r1));
                case 'd' -> instructions.add(new Dec(r1));
                case 'c' -> {
                    r2 = switch (s.charAt(lastSpaceIndex+1)) { case 'a' -> a; case 'b' -> b; case 'c' -> c; case 'd' -> d;
                        default -> throw new RuntimeException("Invalid write register for cpy instruction");};
                    if (r1 == null) {
                        instructions.add(new StaticCpy(Integer.parseInt(s.substring(4, lastSpaceIndex)), r2));
                    } else instructions.add(new DynamicCpy(r1, r2));
                }
                case 'j' -> {
                    n2 = Integer.parseInt(s.substring(lastSpaceIndex+1));
                    if (r1 == null) {
                        n1 = Integer.parseInt(s.substring(4, lastSpaceIndex));
                        if (n1 == 0) instructions.add(new NeverJump());
                        else instructions.add(new AlwaysJump(n2));
                    } else instructions.add(new DynamicJnz(r1, n2));
                }
                default -> throw new RuntimeException("Invalid instruction: \""+s+"\"");
            }
        }
    }

    private static void executeInstructions() {
        int instructionNumber = 0;
        while (instructionNumber < instructions.size()) instructionNumber += instructions.get(instructionNumber).execute();
        System.out.println("Value in register 'a' after executing instructions: "+ a.value);
    }

    static private abstract class Instruction {
        abstract int execute();
    }

    static private class StaticCpy extends Instruction {
        int value;
        Register destination;

        StaticCpy(int value, Register destination) {
            this.value = value;
            this.destination = destination;
        }

        int execute() { destination.value = value; return 1;}
    }

    static private class DynamicCpy extends Instruction {
        Register source, destination;

        DynamicCpy(Register source, Register destination) {
            this.source = source;
            this.destination = destination;
        }

        int execute() { destination.value = source.value; return 1;}
    }

    static private class Inc extends Instruction {
        Register register;

        Inc(Register register) {
            this.register = register;
        }

        int execute() { register.value++; return 1; }
    }

    static private class Dec extends Instruction {
        Register register;

        Dec(Register register) {
            this.register = register;
        }

        int execute() { register.value--; return 1; }
    }

    static private class AlwaysJump extends Instruction {
        int jumpDistance;

        AlwaysJump(int jumpDistance) {
            this.jumpDistance = jumpDistance;
        }

        int execute() { return jumpDistance; }
    }

    static private class NeverJump extends Instruction {
        int execute() { return 1;}
    }

    static private class DynamicJnz extends Instruction {
        Register register;
        int jumpDistance;

        DynamicJnz(Register register, int jumpDistance) {
            this.register = register;
            this.jumpDistance = jumpDistance;
        }

        int execute() { return register.value == 0 ? 1 : jumpDistance; }
    }

    private static class Register {
        int value = 0;
    }

}
