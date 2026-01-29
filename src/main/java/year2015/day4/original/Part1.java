package year2015.day4.original;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Part1 {
    static private final int DAY = 4;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");

    static private String inputString;
    static private int decimalNumber = 1;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        getSolution();

        System.out.println("Solution: "+ decimalNumber);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void getSolution() {
        MessageDigest messageDigest;
        byte[] bytes;
        StringBuilder sb = new StringBuilder();
        String hs;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            messageDigest.reset();
            bytes = messageDigest.digest((inputString+decimalNumber).getBytes());
            sb.setLength(0);
            for (byte b : bytes) {
                sb.append(String.format("%02X", b));
            }
            hs = sb.toString();
            if (hs.charAt(0) == '0' && hs.charAt(1) == '0' && hs.charAt(2) == '0' && hs.charAt(3) == '0' && hs.charAt(4) == '0')
                break;
            decimalNumber++;
        }
    }

}
