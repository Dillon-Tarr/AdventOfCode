package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

class Day14 {
    static private final int DAY = 14;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private String salt;
    static private MessageDigest messageDigest;
    static private boolean part2Mode;

    static void main(String[] args) {
        long startTime = System.nanoTime();

        part2Mode = args.length != 0;
        getSalt();
        prepMessageDigest();
        getIndexOf64thKey();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getSalt() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            salt = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void prepMessageDigest() {
        try {messageDigest = MessageDigest.getInstance("MD5");} catch (NoSuchAlgorithmException e) {throw new RuntimeException(e);}
    }

    private static void getIndexOf64thKey() {
        ArrayList<String> strings = new ArrayList<>(10000);
        ArrayList<RefChar> firstTriples = new ArrayList<>(10000);
        String iString, jString;
        for (int i = 0; i <= 999; i++) {
            strings.add(hashAndGetHexString(i));
            firstTriples.add(new RefChar('u'));
        }
        int i = -1, j, k = 999, findCount = 0;
        char ic1, ic2, ic3, jc1, jc2, jc3, jc4, jc5;
        RefChar iTriple, jTriple;
        mainLoop: while (findCount < 64) {
            i++; k++;
            strings.add(hashAndGetHexString(k));
            firstTriples.add(new RefChar('u'));
            iString = strings.get(i);
            iTriple = firstTriples.get(i);
            if (iTriple.value == 'u') { // Check for triples if not already done in j loop. If none at this point, ignore.
                ic1 = iString.charAt(0);
                ic2 = iString.charAt(1);
                for (int c = 2; c < iString.length(); c++) {
                    ic3 = iString.charAt(c);
                    if (ic1 == ic2 && ic2 == ic3) {
                        iTriple.value = ic1;
                        break; // "found first" mode, must leave if no match in next 1000
                    }
                    ic1 = ic2;
                    ic2 = ic3;
                }
            }
            switch (iTriple.value) {
                case 'u', 'n' -> {} // Effectively a continue statement if iString has no triple.
                default -> { for (int a = 1; a <= 1000; a++) {
                    j = i+a;
                    jTriple = firstTriples.get(j);
                    if (jTriple.value == 'n') continue;
                    jString = strings.get(j);
                    jc1 = jString.charAt(0);
                    jc2 = jString.charAt(1);
                    jc3 = jString.charAt(2);
                    jc4 = jString.charAt(3);
                    for (int c = 4; c < jString.length(); c++) {
                        jc5 = jString.charAt(c);
                        if (jc1 == jc2 && jc2 == jc3) {
                            if (jTriple.value == 'u') jTriple.value = jc3; // Record first triple in jString if unknown.
                            if (jc3 == iTriple.value && jc3 == jc4 && jc4 == jc5) {
                                findCount++;
                                System.out.println("Key #"+findCount+" found at index: "+i);
                                continue mainLoop;
                            }
                        }
                        jc1 = jc2;
                        jc2 = jc3;
                        jc3 = jc4;
                        jc4 = jc5;
                    }
                    // Reaching this point means a key was not found, but doesn't mean a triple isn't present in jString.
                    // There are two unchecked possible triples, if one hasn't already been found. After j loop,
                    // c is 1 index past end of jString; jc4 is last char. jc3, jc2, and jc1 are the preceding characters.
                    // Possible thus far unchecked triples are: jc1, jc2, and jc3 OR jc2, jc3, and jc4;
                    // In both possible cases, jc2 and jc3 match.
                    // If still no triple found, save doing anything else with this string by marking it as having 'n'o triples.
                    if (jTriple.value == 'u') jTriple.value = jc2 == jc3 && (jc1 == jc2 || jc2 == jc4) ? jc3 : 'n';
                } } // End of jString loop and default branch.
            } // End of switch;
        } // End of mainLoop;
        System.out.println("Index of 64th key: "+i);
    }

    private static String hashAndGetHexString(int n) {
        byte[] bytes = messageDigest.digest((salt+n).getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) sb.append(String.format("%02x", bytes[i]));
        return part2Mode ? hash2016MoreTimes(sb.toString()): sb.toString();
    }

    private static String hash2016MoreTimes(String s) {
        byte[] bytes;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2016; i++) {
            bytes = messageDigest.digest(s.getBytes());
            for (int j = 0; j < bytes.length; j++) sb.append(String.format("%02x", bytes[j]));
            s = sb.toString();
            sb.setLength(0);
        }
        return s;
    }

    private static class RefChar {
        char value;

        RefChar (char value) { this.value = value; }
    }

}
