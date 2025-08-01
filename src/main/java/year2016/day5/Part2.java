package year2016.day5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Part2 {
    static private final int DAY = 5;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private String doorIDString;
    static private int decimalInteger = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        generatePassword();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            doorIDString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void generatePassword() {
        MessageDigest messageDigest;
        try {messageDigest = MessageDigest.getInstance("MD5");} catch (NoSuchAlgorithmException e) {throw new RuntimeException(e);}
        byte[] bytes;
        char[] passwordArray = {'_', '_', '_', '_', '_', '_', '_', '_'};
        char potentialIndexChar;
        int potentialIndexInt, charsFound = 0;
        while (charsFound < 8) {
            bytes = messageDigest.digest((doorIDString+decimalInteger++).getBytes());
            if (bytes[0] == 0 && bytes[1] == 0 && (char) bytes[2] < 16) { // (char) cast makes the byte unsigned.
                System.out.println("Index of potential find: "+(decimalInteger-1));
                potentialIndexChar = String.format("%02X", bytes[2]).charAt(1);
                if (potentialIndexChar < '0' || potentialIndexChar > '7') continue;
                potentialIndexInt = Integer.parseInt(potentialIndexChar+"");
                if (passwordArray[potentialIndexInt] == '_') {
                    System.out.print("                         ^Valid find! Found char for index "+potentialIndexChar);
                    passwordArray[potentialIndexInt] = String.format("%02X", bytes[3]).charAt(0);
                    System.out.println("; Password pieces found: "+String.valueOf(passwordArray));
                    charsFound++;
                }
            }
        }
        System.out.println("Done!\n\nPassword: "+String.valueOf(passwordArray));
    }

}
