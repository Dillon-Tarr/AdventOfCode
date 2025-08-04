package year2016.day9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Part1 {
    static private final int DAY = 9;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private String compressedData;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        decompressData();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            compressedData = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void decompressData() {
        StringBuilder decompressedData = new StringBuilder();
        String sub, chars;
        char currentChar;
        int i = 0;
        do {
            currentChar = compressedData.charAt(i);
            if (currentChar != '(') {
                decompressedData.append(currentChar);
                i++;
            }
            else {
                sub = compressedData.substring(i);
                int xIndex = sub.indexOf('x');
                int charCount = Integer.parseInt(sub.substring(1, xIndex));
                int markerClosingIndex = sub.indexOf(')');
                int repetitions = Integer.parseInt(sub.substring(xIndex+1, markerClosingIndex));
                chars = sub.substring(markerClosingIndex+1, markerClosingIndex+1+charCount);
                decompressedData.append(chars.repeat(repetitions));
                i += markerClosingIndex+1+charCount;
            }
        } while (i < compressedData.length());
        System.out.println("\nLength of decompressed data: "+decompressedData.length());
    }

}
