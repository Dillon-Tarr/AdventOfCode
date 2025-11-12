package year2017.day23;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static year2017.day23.Part1.Type.*;

class Part1 {
    static private final int DAY = 23;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private Instruction[] instructions;
    static private int instructionCount;
    static private int mulCount = 0;
    static private final Register a = new Register(), b = new Register(), c = new Register(), d = new Register(),
            e = new Register(), f = new Register(), g = new Register(), h = new Register();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        processInputData();
        executeInstructions();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
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
        instructionCount = inputStrings.size();
        instructions = new Instruction[instructionCount];
        String s;
        char charAt4, lastChar;
        int lastSpaceIndex;
        Type type;
        Object arg1, arg2;
        for (int i = 0; i < instructionCount; i++) {
            s = inputStrings.get(i);
            charAt4 = s.charAt(4);
            lastChar = s.charAt(s.length()-1);
            lastSpaceIndex = s.lastIndexOf(' ');
            type = switch (s.charAt(2)) {
                case 't' -> SET; case 'b' -> SUB; case 'l' -> MUL; case 'z' -> JNZ;
                default -> throw new RuntimeException("Invalid instruction: \""+s+"\"");
            };
            if (lastSpaceIndex == 3) {
                arg1 = Character.isDigit(charAt4) ?
                        Long.parseLong(s.substring(4)) :
                        getCorrespondingRegister(charAt4);
                arg2 = null;
            } else {
                arg1 = Character.isDigit(charAt4) ?
                        Long.parseLong(s.substring(4, lastSpaceIndex)) :
                        getCorrespondingRegister(charAt4);
                arg2 = Character.isDigit(lastChar) ?
                        Long.parseLong(s.substring(lastSpaceIndex+1)) :
                        getCorrespondingRegister(lastChar);
            }
            instructions[i] = new Instruction(type, arg1, arg2);
        }
    }

    static private Register getCorrespondingRegister(char ch) {
        return switch (ch) {
            case 'a' -> a; case 'b' -> b; case 'c' -> c; case 'd' -> d; case 'e' -> e; case 'f' -> f; case 'g' -> g; case 'h' -> h;
            default -> throw new IllegalArgumentException("Illegal register letter: "+ch);
        };
    }

    private static void executeInstructions() {
        long instructionNumber = 0;
        while (instructionNumber >= 0 && instructionNumber < instructionCount) instructionNumber += instructions[(int)instructionNumber].execute();
        System.out.println("\nNumber of MUL instruction invocations (part 1 answer): "+mulCount);
    }

    enum Type {
        SET, SUB, MUL, JNZ
    }

    static private class Instruction {
        Type type;
        Object arg1, arg2;

        Instruction(Type type, Object arg1, Object arg2) {
            this.type = type;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }

        long execute() {
            switch (type) {
                case SET -> {
                    if (arg1 instanceof Register r1) r1.value = arg2 instanceof Register r2 ? r2.value : (long) arg2;
                } case SUB -> {
                    if (arg1 instanceof Register r1) r1.value -= arg2 instanceof Register r2 ? r2.value : (long) arg2;
                } case MUL -> {
                    mulCount++;
                    if (arg1 instanceof Register r1) r1.value *= arg2 instanceof Register r2 ? r2.value : (long) arg2;
                } case JNZ -> {
                    long value = arg1 instanceof Register r1 ? r1.value : (long) arg1;
                    if (value != 0) return arg2 instanceof Register r2 ? r2.value : (long) arg2;
                }
            }
            return 1;
        }
    }

    private static class Register {
        long value;

        Register(){ value = 0; }

    }

}
