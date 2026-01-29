package year2017.day18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static year2017.day18.Part1.Type.*;

class Part1 {
    static private final int DAY = 18;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private Instruction[] instructions;
    static private int instructionCount;
    static private Long lastSnd = null, lastRcv = null;
    static private final HashMap<Character, Register> characterRegisterHashMap = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

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
        instructionCount = inputStrings.size();
        instructions = new Instruction[instructionCount];
        String s;
        Type type;
        Object arg1, arg2;
        int lastSpaceIndex;
        for (int i = 0; i < instructionCount; i++) {
            s = inputStrings.get(i);
            lastSpaceIndex = s.lastIndexOf(' ');
            type = switch (s.charAt(1)) {
                case 'n' -> SND; case 'e' -> SET; case 'd' -> ADD; case 'u' -> MUL;
                case 'o' -> MOD; case 'c' -> RCV; case 'g' -> JGZ;
                default -> throw new RuntimeException("Invalid instruction: \""+s+"\"");
            };
            if (lastSpaceIndex == 3) {
                arg1 = Character.isDigit(s.charAt(4)) ?
                        Long.parseLong(s.substring(4)) :
                        characterRegisterHashMap.computeIfAbsent(s.charAt(4), _ -> new Register());
                arg2 = null;
            } else {
                arg1 = Character.isDigit(s.charAt(4)) ?
                        Long.parseLong(s.substring(4, lastSpaceIndex)) :
                        characterRegisterHashMap.computeIfAbsent(s.charAt(4), _ -> new Register());
                arg2 = Character.isDigit(s.charAt(s.length()-1)) ?
                        Long.parseLong(s.substring(lastSpaceIndex+1)) :
                        characterRegisterHashMap.computeIfAbsent(s.charAt(s.length()-1), _ -> new Register());
            }
            instructions[i] = new Instruction(type, arg1, arg2);
        }
    }

    private static void executeInstructions() {
        long instructionNumber = 0;
        while (instructionNumber >= 0 && instructionNumber < instructionCount) {
            instructionNumber += instructions[(int)instructionNumber].execute();
            if (lastRcv != null) break;
        }
        System.out.println("\nFirst receive value: "+lastRcv);
    }

    enum Type {
        SND, SET, ADD, MUL, MOD, RCV, JGZ
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
                case SND -> lastSnd = arg1 instanceof Register r1 ? r1.value : (long) arg1;
                case SET -> {
                    if (arg1 instanceof Register r1)
                        r1.value = arg2 instanceof Register r2 ? r2.value : (long) arg2;
                }
                case ADD -> {
                    if (arg1 instanceof Register r1)
                        r1.value += arg2 instanceof Register r2 ? r2.value : (long) arg2;
                }
                case MUL -> {
                    if (arg1 instanceof Register r1)
                        r1.value *= arg2 instanceof Register r2 ? r2.value : (long) arg2;
                }
                case MOD -> {
                    if (arg1 instanceof Register r1)
                        r1.value %= arg2 instanceof Register r2 ? r2.value : (long) arg2;
                }
                case RCV -> {
                    long value = arg1 instanceof Register r1 ? r1.value : (long) arg1;
                    if (value != 0 && lastSnd != null) lastRcv = lastSnd;
                }
                case JGZ -> {
                    long value = arg1 instanceof Register r1 ? r1.value : (long) arg1;
                    if (value > 0) return arg2 instanceof Register r2 ? r2.value : (long) arg2;
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
