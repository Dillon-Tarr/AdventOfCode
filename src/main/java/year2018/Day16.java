package year2018;

import shared.BitwiseOperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class Day16 {
    static private final int DAY = 16;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<Sample> samples = new ArrayList<>();


    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        ArrayList<String> inputStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        int i = 0;
        String s;
        String[] beforeStrings, instructionStrings, afterStrings;
        int[] before, instruction, after;
        while (i < inputStrings.size()) {
            s = inputStrings.get(i++);
            if (s.isEmpty()) break;
            beforeStrings = s.substring(9, s.length()-1).split(", ");
            s = inputStrings.get(i++);
            instructionStrings = s.split(" ");
            s = inputStrings.get(i++);
            afterStrings = s.substring(9, s.length()-1).split(", ");
            before = new int[4]; instruction = new int[4]; after = new int[4];
            for (int j = 0; j < 4; j++) {
                before[j] = Integer.parseInt(beforeStrings[j]);
                instruction[j] = Integer.parseInt(instructionStrings[j]);
                after[j] = Integer.parseInt(afterStrings[j]);
            }
            samples.add(new Sample(before, instruction, after));
            i++;
        }
    }
    
    private static void solve() {
        int threeOrMoreCount = 0;
        int matchCount;
        for (var sample : samples) {
            matchCount = 0;
            int[] i = sample.before; int[] instruction = sample.instruction; int[] o = sample.after;
            if (Arrays.equals(o, addr(i, instruction))) matchCount++;
            if (Arrays.equals(o, addi(i, instruction))) matchCount++;
            if (Arrays.equals(o, mulr(i, instruction))) matchCount++;
            if (Arrays.equals(o, muli(i, instruction))) matchCount++;
            if (Arrays.equals(o, banr(i, instruction))) matchCount++;
            if (Arrays.equals(o, bani(i, instruction))) matchCount++;
            if (Arrays.equals(o, borr(i, instruction))) matchCount++;
            if (Arrays.equals(o, bori(i, instruction))) matchCount++;
            if (Arrays.equals(o, setr(i, instruction))) matchCount++;
            if (Arrays.equals(o, seti(i, instruction))) matchCount++;
            if (Arrays.equals(o, gtir(i, instruction))) matchCount++;
            if (Arrays.equals(o, gtri(i, instruction))) matchCount++;
            if (Arrays.equals(o, gtrr(i, instruction))) matchCount++;
            if (Arrays.equals(o, eqir(i, instruction))) matchCount++;
            if (Arrays.equals(o, eqri(i, instruction))) matchCount++;
            if (Arrays.equals(o, eqrr(i, instruction))) matchCount++;
            if (matchCount >= 3) threeOrMoreCount++;
        }
        System.out.println("\nNumber of samples which behave like 3 or more opcodes: "+threeOrMoreCount);
    }

    /**
     * addr (add register) stores into register C the result of adding register A and register B.
     * */
    private static int[] addr(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = o[args[0]]+o[args[1]];
        return o;
    }

    /**
     * addi (add immediate) stores into register C the result of adding register A and value B.
     * */
    private static int[] addi(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = o[args[0]]+args[1];
        return o;
    }

    /**
     * mulr (multiply register) stores into register C the result of multiplying register A and register B.
     * */
    private static int[] mulr(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = o[args[0]]*o[args[1]];
        return o;
    }

    /**
     * muli (multiply immediate) stores into register C the result of multiplying register A and value B.
     * */
    private static int[] muli(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = o[args[0]]*args[1];
        return o;
    }

    /**
     * banr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
     * */
    private static int[] banr(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = BitwiseOperations.and((char) o[args[0]], (char) o[args[1]]);
        return o;
    }

    /**
     * bani (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
     * */
    private static int[] bani(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = BitwiseOperations.and((char) o[args[0]], (char) args[1]);
        return o;
    }

    /**
     * borr (bitwise OR register) stores into register C the result of the bitwise OR of register A and register B.
     * */
    private static int[] borr(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = BitwiseOperations.or((char) o[args[0]], (char) o[args[1]]);
        return o;
    }

    /**
     * bori (bitwise OR immediate) stores into register C the result of the bitwise OR of register A and value B.
     * */
    private static int[] bori(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = BitwiseOperations.or((char) o[args[0]], (char) args[1]);
        return o;
    }

    /**
     * setr (set register) copies the contents of register A into register C. (Input B is ignored.)
     * */
    private static int[] setr(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = o[args[0]];
        return o;
    }

    /**
     * seti (set immediate) stores value A into register C. (Input B is ignored.)
     * */
    private static int[] seti(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = args[0];
        return o;
    }

    /**
     * gtir (greater-than immediate/register) sets register C to 1 if value A is greater than register B.
     * Otherwise, register C is set to 0.
     * */
    private static int[] gtir(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = args[0] > o[args[1]] ? 1 : 0;
        return o;
    }

    /**
     * gtri (greater-than register/immediate) sets register C to 1 if register A is greater than value B.
     * Otherwise, register C is set to 0.
     * */
    private static int[] gtri(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = o[args[0]] > args[1] ? 1 : 0;
        return o;
    }

    /**
     * gtrr (greater-than register/register) sets register C to 1 if register A is greater than register B.
     * Otherwise, register C is set to 0.
     * */
    private static int[] gtrr(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = o[args[0]] > o[args[1]] ? 1 : 0;
        return o;
    }

    /**
     * eqir (equal immediate/register) sets register C to 1 if value A is equal to register B.
     * Otherwise, register C is set to 0.
     * */
    private static int[] eqir(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = args[0] == o[args[1]] ? 1 : 0;
        return o;
    }

    /**
     * eqri (equal register/immediate) sets register C to 1 if register A is equal to value B.
     * Otherwise, register C is set to 0.
     * */
    private static int[] eqri(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = o[args[0]] == args[1] ? 1 : 0;
        return o;
    }

    /**
     * eqrr (equal register/register) sets register C to 1 if register A is equal to register B.
     * Otherwise, register C is set to 0.
     * */
    private static int[] eqrr(int[] i, int[] args) {
        int[] o = new int[4]; for (int j = 0; j < 4; j++) o[j] = i[j];
        o[args[2]] = o[args[0]] == o[args[1]] ? 1 : 0;
        return o;
    }

    private static class Sample {
        int[] before, instruction, after;

        public Sample(int[] before, int[] instructionWhole, int[] after) {
            instruction = new int[3]; instruction[0] = instructionWhole[1];
            instruction[1] = instructionWhole[2];instruction[2] = instructionWhole[3];
            this.before = before; this.after = after;
        }
    }

}
