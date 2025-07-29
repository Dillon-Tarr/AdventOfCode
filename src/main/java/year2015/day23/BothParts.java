package year2015.day23;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BothParts {
    static private final int DAY = 23;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final List<Instruction> instructions = new ArrayList<>();
    static private final Register registerA = new Register(), registerB = new Register();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        setInstructions();
        // Enter any argument when running main for part 2 mode.
        // Part 2 mode starts register a with a value of 1 by incrementing it from 0 to 1 before following instructions.
        if (args.length != 0) registerA.inc();
        followInstructions();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void setInstructions() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            Instruction.Type type;
            Character register;
            int spaceIndex;
            Integer jumpValue;
            while (inputString != null) {
                spaceIndex = inputString.indexOf(' ');
                String instructionSubstring = inputString.substring(0, spaceIndex);
                type = switch (instructionSubstring) {
                    case "hlf" -> Instruction.Type.hlf;
                    case "tpl" -> Instruction.Type.tpl;
                    case "inc" -> Instruction.Type.inc;
                    case "jmp" -> Instruction.Type.jmp;
                    case "jie" -> Instruction.Type.jie;
                    case "jio" -> Instruction.Type.jio;
                    default -> throw new IllegalArgumentException("Illegal instruction type.");
                };
                register = type == Instruction.Type.jmp ? null : inputString.charAt(4);
                jumpValue = switch (type) {
                    case jmp, jie, jio -> Integer.parseInt(inputString.substring(inputString.lastIndexOf(' ')+1));
                    default -> null;
                };
                instructions.add(new Instruction(type, register, jumpValue));
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void followInstructions() {
        int i = 0;
        Instruction instruction;
        Instruction.Type type;
        Register register;
        Integer jumpValue;
        while (i >= 0 && i < instructions.size()) {
            instruction = instructions.get(i);
            type = instruction.type();
            jumpValue = instruction.jumpValue();
            if (type == Instruction.Type.jmp) {
                i += jumpValue;
                continue;
            }
            register = switch (instruction.registerName()) {
                case 'a' -> registerA;
                case 'b' -> registerB;
                default -> throw new IllegalStateException("Unexpected value: " + instruction.registerName());
            };
            switch (type) {
                case jio -> {
                    if (register.value == 1) {
                        i += jumpValue;
                        continue;
                    }
                }
                case jie -> {
                    if (register.value%2 == 0) {
                        i+= jumpValue;
                        continue;
                    }
                }
                case hlf -> register.hlf();
                case tpl -> register.tpl();
                case inc -> register.inc();
            }
            i++;
        }
        System.out.println("Value in register b after finishing execution of instructions: "+ registerB.value);
    }

}
