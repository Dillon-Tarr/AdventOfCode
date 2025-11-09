package year2017.day18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import static year2017.day18.Part2.Type.*;

class Part2 {
    static private final int DAY = 18;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private int instructionCount;
    static private Instruction[] zerosInstructions, onesInstructions;
    static private final HashMap<Character, Register> zerosRegisters = new HashMap<>();
    static private final HashMap<Character, Register> onesRegisters = new HashMap<>();
    static private final ArrayDeque<Long> zerosQueue = new ArrayDeque<>();
    static private final ArrayDeque<Long> onesQueue = new ArrayDeque<>();
    static private boolean zerosTurn = false, zeroIsWaiting = false, oneIsWaiting = false,
            zeroRanOutOfBounds = false, oneRanOutOfBounds = false;
    static private int programOneSendCount;
    static private long zerosInstructionNumber = 0, onesInstructionNumber = 0;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        prepareInstructionsAndRegisters();
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

    private static void prepareInstructionsAndRegisters() {
        instructionCount = inputStrings.size();
        zerosInstructions = new Instruction[instructionCount];
        onesInstructions = new Instruction[instructionCount];
        String s;
        Type type;
        Object arg1ForProgramZero, arg2ForProgramZero, arg1ForProgramOne, arg2ForProgramOne;
        int lastSpaceIndex;
        for (int i = 0; i < instructionCount; i++) {
            s = inputStrings.get(i);
            lastSpaceIndex = s.lastIndexOf(' ');
            type = switch (s.charAt(1)) {
                case 'n' -> SND; case 'e' -> SET; case 'd' -> ADD; case 'u' -> MUL;
                case 'o' -> MOD; case 'c' -> RCV; case 'g' -> JGZ;
                default -> throw new RuntimeException("Invalid instruction: \""+s+"\"");
            };
            char c1 = s.charAt(4);
            if (Character.isDigit(c1)) {
                long value = lastSpaceIndex == 3 ?
                        Long.parseLong(s.substring(4)) :
                        Long.parseLong(s.substring(4, lastSpaceIndex));
                arg1ForProgramZero = value;
                arg1ForProgramOne = value;
            } else {
                if (!zerosRegisters.containsKey(c1)) {
                    zerosRegisters.put(c1, new Register());
                    onesRegisters.put(c1, new Register());
                }
                arg1ForProgramZero = zerosRegisters.get(c1);
                arg1ForProgramOne = onesRegisters.get(c1);
            }
            if (lastSpaceIndex == 3) {
                arg2ForProgramZero = null;
                arg2ForProgramOne = null;
            } else {
                char c2 = s.charAt(s.length()-1);
                if (Character.isDigit(c2)) {
                    long value = Long.parseLong(s.substring(lastSpaceIndex+1));
                    arg2ForProgramZero = value;
                    arg2ForProgramOne = value;
                }
                else {
                    if (!zerosRegisters.containsKey(c2)) {
                        zerosRegisters.put(c2, new Register());
                        onesRegisters.put(c2, new Register());
                    }
                    arg2ForProgramZero = zerosRegisters.get(c2);
                    arg2ForProgramOne = onesRegisters.get(c2);
                }
            }
            zerosInstructions[i] = new Instruction(type, arg1ForProgramZero, arg2ForProgramZero);
            onesInstructions[i] = new Instruction(type, arg1ForProgramOne, arg2ForProgramOne);
        }
        onesRegisters.get('p').value = 1;
    }

    private static void executeInstructions() {
        while (setNextTurnAndReturnWhetherCanContinue()) {
            if (zerosTurn) {
                zerosInstructionNumber += zerosInstructions[(int)zerosInstructionNumber].execute();
                if (zerosInstructionNumber < 0 || zerosInstructionNumber >= instructionCount) zeroRanOutOfBounds = true;
            } else {
                onesInstructionNumber += onesInstructions[(int)onesInstructionNumber].execute();
                if (onesInstructionNumber < 0 || onesInstructionNumber >= instructionCount) oneRanOutOfBounds = true;
            }
        }
        System.out.println("\nNumber of sends from program one: "+programOneSendCount);
    }

    private static boolean setNextTurnAndReturnWhetherCanContinue() {
        if ((zeroRanOutOfBounds || zeroIsWaiting) && (oneRanOutOfBounds || oneIsWaiting)) return false;
        if (zeroRanOutOfBounds) zerosTurn = false;
        else if (oneRanOutOfBounds) zerosTurn = true;
        else zerosTurn = !zerosTurn;
        return true;
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
                case SND -> {
                    long value = arg1 instanceof Register r1 ? r1.value : (long) arg1;
                    if (zerosTurn) {
                        onesQueue.add(value);
                        oneIsWaiting = false;
                    }
                    else {
                        zerosQueue.add(value);
                        zeroIsWaiting = false;
                        programOneSendCount++;
                    }
                }
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
                    if (!(arg1 instanceof Register r1)) throw new RuntimeException("Uh oh!!");
                    if (zerosTurn) {
                        if (zerosQueue.isEmpty()) { zeroIsWaiting = true; return 0; }
                        else r1.value = zerosQueue.remove();
                    } else { // one's turn
                        if (onesQueue.isEmpty()) { oneIsWaiting = true; return 0; }
                        else r1.value = onesQueue.remove();
                    }
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
