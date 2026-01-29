package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day5 {
    static private final int DAY = 5;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private String inputString;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solvePart1() {
        StringBuilder sb = new StringBuilder(inputString);
        int i = 0; char c1, c2;
        while (i < sb.length()-1) {
            c1 = sb.charAt(i);
            c2 = sb.charAt(i+1);
            if ((Character.isLowerCase(c1) && c1-32 == c2) || c1+32 == c2) {
                sb.delete(i, i+2);
                if (i > 0) i--;
            } else i++;
        }
        System.out.println("\nRemaining number of units after all reactions (part 1 answer): "+sb.length());
    }

    private static void solvePart2() {
        char a = 'a', A = 'A'; int shortestLength = Integer.MAX_VALUE, length;
        StringBuilder sb = new StringBuilder();
        for (int letter = 1; letter <= 26; letter++) {
            sb.setLength(0); sb.append(inputString);
            int i = 0; char c1, c2;
            while (i < sb.length()) {
                c1 = sb.charAt(i);
                if (c1 == a || c1 == A) sb.deleteCharAt(i);
                else i++;
            }
            i = 0;
            while (i < sb.length()-1) {
                c1 = sb.charAt(i);
                c2 = sb.charAt(i+1);
                if ((Character.isLowerCase(c1) && c1-32 == c2) || c1+32 == c2) {
                    sb.delete(i, i+2);
                    if (i > 0) i--;
                } else i++;
            }
            length = sb.length(); if (length < shortestLength) shortestLength = length;
            a++; A++;
        }

        System.out.println("\nShortest polymer chain possible if removing all units of one type before reacting (part 2 answer): "+shortestLength);
    }

}
