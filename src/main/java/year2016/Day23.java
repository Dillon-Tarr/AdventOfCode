package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static year2016.Day23.Type.*;

class Day23 {
    static private final int DAY = 23;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final ArrayList<Instruction> instructions = new ArrayList<>();
    static private int instructionCount;
    static private final Register a = new Register(7);
    static private final Register b = new Register();
    static private final Register c = new Register();
    static private final Register d = new Register();

    static void main(String[] args) {
        long startTime = System.nanoTime();

        if (args.length > 0) a.value = 12; // For part 2
        System.out.println("\nInitial value in register 'a': "+a.value);
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
        String s;
        Type type;
        Object arg1, arg2;
        int lastSpaceIndex;
        for (int i = 0; i < instructionCount; i++) {
            s = inputStrings.get(i);
            lastSpaceIndex = s.lastIndexOf(' ');
            type = switch (s.charAt(0)) {
                case 'i' -> INC; case 'd' -> DEC; case 'c' -> CPY; case 'j' -> JNZ; case 't' -> TGL;
                default -> throw new RuntimeException("Invalid instruction: \""+s+"\"");
            };
            arg1 = switch (s.charAt(4)) {
                case 'a' -> a; case 'b' -> b; case 'c' -> c; case 'd' -> d;
                default -> Integer.parseInt(s.substring(4, lastSpaceIndex));
            };
            arg2 = lastSpaceIndex == 3 ? null : switch (s.charAt(lastSpaceIndex+1)) {
                    case 'a' -> a; case 'b' -> b; case 'c' -> c; case 'd' -> d;
                    default -> Integer.parseInt(s.substring(lastSpaceIndex+1));
            };
            instructions.add(new Instruction(type, i, arg1, arg2));
        }
    }

    private static void executeInstructions() {
        int instructionNumber = 0;
        while (instructionNumber >= 0 && instructionNumber < instructionCount) instructionNumber += instructions.get(instructionNumber).execute();
        System.out.println("\nValue in register 'a' after executing instructions: "+ a.value);
    }

    enum Type {
        INC, DEC, CPY, JNZ, TGL
    }

    static private class Instruction {
        Type type;
        int instructionNumber;
        Object arg1, arg2;

        Instruction(Type type, int instructionNumber, Object arg1, Object arg2) {
            this.type = type;
            this.instructionNumber = instructionNumber;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }

        int execute() {
            switch (type) {
                case INC -> { if (arg1 instanceof Register register) register.value++; }
                case DEC -> { if (arg1 instanceof Register register) register.value--; }
                case CPY -> { if (arg2 instanceof Register destination) destination.value = arg1 instanceof Register source ? source.value : (int) arg1; }
                case JNZ -> {
                    int val1 = arg1 instanceof Register register ? register.value : (int) arg1;
                    if (val1 != 0) return arg2 instanceof Register register ? register.value : (int) arg2;
                }
                case TGL -> {
                    int targetIndex = instructionNumber+((Register)arg1).value;
                    if (targetIndex < 0 || targetIndex >= instructionCount) return 1;
                    Instruction targetInstruction = instructions.get(targetIndex);
                    targetInstruction.type = switch (targetInstruction.type) {
                        case INC -> DEC;
                        case DEC, TGL -> INC;
                        case CPY -> JNZ;
                        case JNZ -> CPY;
                    };
                }
            }
            return 1;
        }
    }

    private static class Register {
        int value;

        Register(){
            value = 0;
        }
        Register(int initialValue) {
            value = initialValue;
        }
    }

}
