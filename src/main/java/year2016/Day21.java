package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day21 {
    static private final int DAY = 21;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final String password = "abcdefgh"; // for part 1
    static private final String scrambled = "fbgdceah"; // for part 2
    static private int length;
    static private StringBuilder sb;

    static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        if (args.length == 0) scramblePassword();
        else unscramblePassword();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void scramblePassword() {
        sb = new StringBuilder(password);
        length = password.length();
        for (String s : inputStrings) {
            switch (s.charAt(1)) {
                case 'w' -> {
                    if (s.charAt(5) == 'p') swapPositions(
                            Integer.parseInt(s.substring(14, s.indexOf(' ', 15))),
                            Integer.parseInt(s.substring(s.lastIndexOf(' ')+1)));
                    else swapLetters(s.charAt(12), s.charAt(26));
                }
                case 'o' -> {
                    switch (s.charAt(7)) {
                        case 'l' -> rotateLeft(Integer.parseInt(s.substring(12, s.indexOf(' ', 13))));
                        case 'r' -> rotateRight(Integer.parseInt(s.substring(13, s.indexOf(' ', 14))));
                        case 'b' -> rotateBasedOnIndexOfCharacter(s.charAt(s.length()-1), false);
                        case 's' -> moveChar(
                                Integer.parseInt(s.substring(14, s.indexOf(' ', 15))),
                                Integer.parseInt(s.substring(s.lastIndexOf(' ')+1)));
                    }
                }
                case 'e' -> reverseRange(
                        Integer.parseInt(s.substring(18, s.indexOf(' ', 19))),
                        Integer.parseInt(s.substring(s.lastIndexOf(' ')+1)));
            }
        }
        System.out.println("\nScrambled password: "+sb);
    }

    private static void unscramblePassword() {
        sb = new StringBuilder(scrambled);
        length = scrambled.length();
        for (int i = inputStrings.size()-1; i >= 0; i--) {
            String s = inputStrings.get(i);
            switch (s.charAt(1)) {
                case 'w' -> {
                    if (s.charAt(5) == 'p') swapPositions(
                            Integer.parseInt(s.substring(14, s.indexOf(' ', 15))),
                            Integer.parseInt(s.substring(s.lastIndexOf(' ')+1)));
                    else swapLetters(s.charAt(12), s.charAt(26));
                }
                case 'o' -> {
                    switch (s.charAt(7)) {
                        case 'l' -> rotateRight(Integer.parseInt(s.substring(12, s.indexOf(' ', 13))));
                        case 'r' -> rotateLeft(Integer.parseInt(s.substring(13, s.indexOf(' ', 14))));
                        case 'b' -> rotateBasedOnIndexOfCharacter(s.charAt(s.length()-1), true);
                        case 's' -> moveChar(
                                Integer.parseInt(s.substring(s.lastIndexOf(' ')+1)),
                                Integer.parseInt(s.substring(14, s.indexOf(' ', 15))));
                    }
                }
                case 'e' -> reverseRange(
                        Integer.parseInt(s.substring(18, s.indexOf(' ', 19))),
                        Integer.parseInt(s.substring(s.lastIndexOf(' ')+1)));
            }
        }
        System.out.println("\nUnscrambled password: "+sb);
    }

    private static void swapPositions(int x, int y) {
        char cx = sb.charAt(x), cy = sb.charAt(y);
        sb.setCharAt(x, cy);
        sb.setCharAt(y, cx);
    }

    private static void swapLetters(char a, char b) {
        for (int i = 0; i < length; i++) {
            if (sb.charAt(i) == a) sb.setCharAt(i, b);
            else if (sb.charAt(i) == b) sb.setCharAt(i, a);
        }
    }

    private static void rotateLeft(int n) {
        String leftChars = sb.substring(0, n);
        sb.delete(0, n);
        sb.append(leftChars);
    }

    private static void rotateRight(int n) {
        String rightChars = sb.substring(length-n);
        sb.delete(length-n, length);
        sb.insert(0, rightChars);
    }

    private static void rotateBasedOnIndexOfCharacter(char c, boolean part2Mode) {
        int n = sb.indexOf(""+c);
        if (!part2Mode) {
            if (n >= 4) n++;
            n++;
            while (n > length) n -= length;
            if (n == length) return;
            if (n < length/2) rotateRight(n);
            else rotateLeft(length-n);
        } else {
            switch (n) { // Assumes 8 characters. Reversing outputs isn't certain with all lengths.
                case 1, 0 -> rotateLeft(1);
                case 3 -> rotateLeft(2);
                case 5 -> rotateLeft(3);
                case 7 -> rotateLeft(4);
                case 2 -> rotateRight(2);
                case 4 -> rotateRight(1);
                case 6 -> {}
            }
        }
    }

    private static void reverseRange(int x, int y) {
        StringBuilder reversedChars = new StringBuilder();
        for (int i = y; i >= x; i--) reversedChars.append(sb.charAt(i));
        sb.replace(x, y+1, reversedChars.toString());
    }

    private static void moveChar(int sourceIndex, int destinationIndex) {
        char c = sb.charAt(sourceIndex);
        sb.deleteCharAt(sourceIndex);
        sb.insert(destinationIndex, c);
    }

}
