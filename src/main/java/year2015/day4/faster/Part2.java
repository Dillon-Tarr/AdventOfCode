package year2015.day4.faster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Part2 {
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
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            bytes = messageDigest.digest((inputString+decimalNumber).getBytes());
            if (bytes[0] == 0 && bytes[1] == 0 && bytes[2] == 0) break;
            decimalNumber++;
        }
    }

}
