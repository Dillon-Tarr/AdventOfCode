package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day16 {
    static private final int DAY = 16;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private String inputString;
    static private int diskLength;
    static private String randomDataString;

    static void main(String[] args) {
        long startTime = System.nanoTime();

        diskLength = args.length == 0 ? 272 : 35651584; // part1 (0 args) : part2 (otherwise)
        getInputData();
        generateRandomLookingData();
        createChecksum();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void generateRandomLookingData() {
        StringBuilder a = new StringBuilder(), b = new StringBuilder(), s = new StringBuilder(inputString);
        while (s.length() < diskLength) {
            a.setLength(0); b.setLength(0);
            a.append(s); b.append(s);
            b.reverse();
            for (int i = 0; i < b.length(); i++) b.setCharAt(i, b.charAt(i) == '0'?'1':'0');
            s.setLength(0);
            s.append(a).append('0').append(b);
        }
        s.setLength(diskLength);
        randomDataString = s.toString();
    }

    private static void createChecksum() {
        StringBuilder referenceString = new StringBuilder(randomDataString);
        StringBuilder checksum = new StringBuilder();
        while (checksum.length() % 2 == 0) {
            checksum.setLength(0);
            for (int i = 1; i <= referenceString.length(); i+=2)
                checksum.append(referenceString.charAt(i) == referenceString.charAt(i-1) ? '1' : '0');
            referenceString.setLength(0);
            referenceString.append(checksum);
        }
        System.out.println("\nCorrect checksum: "+checksum);
    }

}
