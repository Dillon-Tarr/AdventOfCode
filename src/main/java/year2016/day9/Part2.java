package year2016.day9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Part2 {
    static private final int DAY = 9;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private String compressedData;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        System.out.println("\nLength of decompressed data: "+getDecompressedLengthOfString(compressedData));

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            compressedData = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static long getDecompressedLengthOfString(String string) {
        long length = 0;
        String sub, chars;
        char currentChar;
        int i = 0;
        do {
            currentChar = string.charAt(i);
            if (currentChar != '(') { length++; i++; }
            else {
                sub = string.substring(i);
                int xIndex = sub.indexOf('x');
                int charCount = Integer.parseInt(sub.substring(1, xIndex));
                int markerClosingIndex = sub.indexOf(')');
                int repetitions = Integer.parseInt(sub.substring(xIndex+1, markerClosingIndex));
                chars = sub.substring(markerClosingIndex+1, markerClosingIndex+1+charCount);
                length += repetitions*getDecompressedLengthOfString(chars);
                i += markerClosingIndex+1+charCount;
            }
        } while (i < string.length());
        return length;
    }

}
