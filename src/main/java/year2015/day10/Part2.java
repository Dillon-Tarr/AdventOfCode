package year2015.day10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Part2 {
    static private final int DAY = 10;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private String inputString;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputString();
        repeatedlyLookAndSay();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputString() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void repeatedlyLookAndSay() {
        String currentString = inputString;
        System.out.println("\nInitial string: "+inputString+"\n\nInitial string length: "+inputString.length());
        for (int i = 1; i <= 50; i++) {
            currentString = lookAndSay(currentString);
            System.out.println("\nString length after "+i+" lookAndSay() calls: "+currentString.length());
        }
    }

    private static String lookAndSay(String s) {
        if (s.isEmpty()) return s;
        char currentChar = s.charAt(0), charBeingCounted = currentChar;
        int count = 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < s.length(); i++) {
            currentChar = s.charAt(i);
            if (currentChar == charBeingCounted) count++;
            else {
                sb.append(count).append(charBeingCounted);
                charBeingCounted = currentChar;
                count = 1;
            }
        }
        sb.append(count).append(charBeingCounted);
        return sb.toString();
    }

}
