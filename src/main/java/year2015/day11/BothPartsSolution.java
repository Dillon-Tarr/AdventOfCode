package year2015.day11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BothPartsSolution {
    static private final int DAY = 11;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private String inputString;
    static private char[] charArray;
    static private String newPassword;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputString();
        convertToCharArray();
        validateInitialCharsAndSkipILO();
        incrementUntilCompliant();

        System.out.println("\nSanta's original password: "+inputString);
        System.out.println("\nSanta's NEW password should be: "+ newPassword);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputString() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void convertToCharArray() {
        charArray = inputString.toCharArray();
    }

    private static void validateInitialCharsAndSkipILO() {
        char c;
        for (int i = 0; i < charArray.length; i++) {
            c = charArray[i];
            switch(c) {
                case 'i', 'l', 'o' -> {
                    charArray[i] = (char)(c+1);
                    resetCharsToTheRight(i);
                }
                case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' -> {}
                default -> throw new RuntimeException(new IllegalArgumentException("Non-lowercase letter char not allowed: "+charArray[i]));
            }
        }
    }

    private static void resetCharsToTheRight(int ILOIndex) {
        for (int i = ILOIndex+1; i < charArray.length; i++) {
            charArray[i] = 'a';
        }
    }

    private static void incrementUntilCompliant() {
        do incrementCharArray();
        while (!checkForTwoDistinctDoubles() || !checkForIncreasingStraight());
        newPassword = String.valueOf(charArray);
    }

    private static void incrementCharArray(){
        for (int i = charArray.length-1; i >= 0; i--) {
            incrementCharAndReturnNewChar(i);
            if (charArray[i] != 'a') return;
        }
    }

    private static void incrementCharAndReturnNewChar(int i) {
        char c = charArray[i];
        charArray[i] = switch (c) {
            case 'z' -> 'a';
            case 'h' -> 'j';
            case 'k' -> 'm';
            case 'n' -> 'p';
            case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'i', 'j', 'l', 'm', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y' -> (char)(c+1);
            default -> throw new RuntimeException(new IllegalArgumentException("Non-lowercase letter char not allowed: "+c));
        };
    }

    private static boolean checkForTwoDistinctDoubles() {
        char firstDoubleChar = 0, c1, c2 = charArray[0];
        boolean firstDoubleFound = false;
        int i = 1;
        while (i < charArray.length-2) {
            c1 = c2;
            c2 = charArray[i];
            if (c2 == c1) {
                firstDoubleFound = true;
                firstDoubleChar = c2;
                c2 = charArray[++i];
                i++;
                break;
            } else i++;
        }
        if (!firstDoubleFound) return false;
        while (i <charArray.length) {
            c1 = c2;
            c2 = charArray[i];
            if (c2 != firstDoubleChar && c2 == c1) return true;
            else i++;
        }
        return false;
    }

    private static boolean checkForIncreasingStraight() {
        char c1, c2 = charArray[0], c3 = charArray[1];
        for (int i = 2; i < charArray.length; i++) {
            c1 = c2;
            c2 = c3;
            c3 = charArray[i];
            if (c1 == (char)(c2-1) && c2 == (char)(c3-1)) return true;
        }
        return false;
    }

}
