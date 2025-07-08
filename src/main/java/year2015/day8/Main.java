package year2015.day8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Main {
    static private final int DAY = 8;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final Pattern HEXADECIMAL_REGEX = Pattern.compile("\\p{XDigit}");
    static private long nonWhiteSpaceCharacterCount = 0;
    static private int escapedCharacterAndQuoteCount = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        processStrings();

        System.out.println("\nNon-whitespace number of chars from String literals: "+ nonWhiteSpaceCharacterCount);
        System.out.println("\nNumber of escape characters and surrounding quote marks (answer): "+escapedCharacterAndQuoteCount);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    // File must end without a new line for new line
    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            File file = new File(INPUT_FILE.toURI());
            nonWhiteSpaceCharacterCount = file.length();
            String inputString = br.readLine();
            while (inputString != null) {
                inputStrings.add(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processStrings() {
        StringBuilder sb;
        char currentChar, nextChar;
        int inputStringsSize = inputStrings.size();
        escapedCharacterAndQuoteCount += inputStringsSize*2; // for quote marks
        nonWhiteSpaceCharacterCount -= (inputStringsSize-1)*2L; // for new lines, one less than number of lines
        for (int i = 0; i < inputStringsSize; i++) {
            sb = new StringBuilder(inputStrings.get(i));
            for (int j = 0; j < sb.length()-1; j++) { // -1 because last char is always '"'
                currentChar = sb.charAt(j);
                if (currentChar == '\\') {
                    nextChar = sb.charAt(j+1);
                    if (nextChar == '\\' || nextChar == '"') {
                        sb.replace(j, j+2, "");
                        escapedCharacterAndQuoteCount+=1;
                        j--;
                    } else if (nextChar == 'x' && j <= sb.length()-5 && HEXADECIMAL_REGEX.matcher(sb.substring(j+2, j+3)).matches()) { // -5 because escaped \x00 can only start at 5th to last or earlier spot.
                        sb.replace(j, j+4, "");
                        escapedCharacterAndQuoteCount+=3;
                        j--;
                    }
                }
            }
        }
    }

}
