package year2018;

import shared.BitwiseOperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Day19Part1 {
    static private final int DAY = 19;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final int[] registers = new int[6];
    static private int instructionPointerNumber, instructionCount;
    static private final ArrayList<int[]> instructions = new ArrayList<>();
    static private final ArrayList<String> instructionNames = new ArrayList<>(List.of("addr", "addi", "mulr",
            "muli", "banr", "bani", "borr", "bori", "setr", "seti", "gtir", "gtri", "gtrr", "eqir", "eqri", "eqrr"));

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        ArrayList<String> instructionStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            instructionPointerNumber = Character.getNumericValue(s.charAt(s.length()-1));
            s = br.readLine();
            while (s != null) {
                instructionStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (String s : instructionStrings) {
            String[] parts = s.split(" ");
            int[] instruction = new int[4];
            instruction[0] = -1;
            for (int i = 0; i < instructionNames.size(); i++) if (parts[0].equals(instructionNames.get(i))) { instruction[0] = i; break; }
            if (instruction[0] == -1) throw new RuntimeException("Invalid instruction name: "+parts[0]);
            instruction[1] = Integer.parseInt(parts[1]);
            instruction[2] = Integer.parseInt(parts[2]);
            instruction[3] = Integer.parseInt(parts[3]);
            instructions.add(instruction);
        }
        instructionCount = instructions.size();
    }

    private static void solve() {
        int instructionNumber; int[] instruction;
        while (true) {
            instructionNumber = registers[instructionPointerNumber];
            if (instructionNumber < 0 || instructionNumber >= instructionCount) break;
            instruction = instructions.get(registers[instructionPointerNumber]);
            executeInstruction(instruction);
        }
        System.out.println("\nValue left in register 0 after running instructions (part 1 answer): "+registers[0]);
    }

    private static void executeInstruction(int[] instruction) {
        switch (instruction[0]) {
            case 0 -> registers[instruction[3]] = registers[instruction[1]]+registers[instruction[2]]; // addr
            case 1 -> registers[instruction[3]] = registers[instruction[1]]+instruction[2]; // addi
            case 2 -> registers[instruction[3]] = registers[instruction[1]]*registers[instruction[2]]; // mulr
            case 3 -> registers[instruction[3]] = registers[instruction[1]]*instruction[2]; // muli
            case 4 -> registers[instruction[3]] = BitwiseOperations.and((char) registers[instruction[1]], (char) registers[instruction[2]]); // banr
            case 5 -> registers[instruction[3]] = BitwiseOperations.and((char) registers[instruction[1]], (char) instruction[2]); //bani
            case 6 -> registers[instruction[3]] = BitwiseOperations.or((char) registers[instruction[1]], (char) registers[instruction[2]]); // borr
            case 7 -> registers[instruction[3]] = BitwiseOperations.or((char) registers[instruction[1]], (char) instruction[2]); // bori
            case 8 -> registers[instruction[3]] = registers[instruction[1]]; // setr
            case 9 -> registers[instruction[3]] = instruction[1]; // seti
            case 10 -> registers[instruction[3]] = instruction[1] > registers[instruction[2]] ? 1 : 0; // gtir
            case 11 -> registers[instruction[3]] = registers[instruction[1]] > instruction[2] ? 1 : 0; // gtri
            case 12 -> registers[instruction[3]] = registers[instruction[1]] > registers[instruction[2]] ? 1 : 0; // gtrr
            case 13 -> registers[instruction[3]] = instruction[1] == registers[instruction[2]] ? 1 : 0; // eqir
            case 14 -> registers[instruction[3]] = registers[instruction[1]] == instruction[2] ? 1 : 0; // eqri
            case 15 -> registers[instruction[3]] = registers[instruction[1]] == registers[instruction[2]] ? 1 : 0; // eqrr
            default -> throw new IllegalArgumentException("Illegal instruction type number: "+instruction[0]);
        }
        registers[instructionPointerNumber]++;
    }

}
