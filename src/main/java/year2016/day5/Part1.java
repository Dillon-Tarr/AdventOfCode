package year2016.day5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Part1 {
    static private final int DAY = 5;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private String doorIDString;
    static private int decimalInteger = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        generatePassword();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            doorIDString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void generatePassword() {
        MessageDigest messageDigest;
        byte[] bytes;
        try {messageDigest = MessageDigest.getInstance("MD5");} catch (NoSuchAlgorithmException e) {throw new RuntimeException(e);}
        char[] passwordArray = new char[8];
        for (int i = 0; i < 8; i++) {
            do bytes = messageDigest.digest((doorIDString+decimalInteger++).getBytes());
            while (bytes[0] != 0 || bytes[1] != 0 || (char)bytes[2] >= 16); // (char) cast makes the byte unsigned.
            System.out.println("Index of find: "+(decimalInteger-1));
            passwordArray[i] = String.format("%02X", bytes[2]).charAt(1);
        }
        System.out.println("\nPassword: "+String.valueOf(passwordArray));
    }

}
