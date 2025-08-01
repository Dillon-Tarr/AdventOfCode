package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Day6 {
    static private final int DAY = 6;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> messageStrings = new ArrayList<>();
    static private boolean leastCommonLetterMode;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        leastCommonLetterMode = args.length != 0; // Run with any args for part 2 (least common letter) mode.
        getInputData();
        getErrorCorrectedMessage();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                messageStrings.add(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void getErrorCorrectedMessage() {
        int messageLength = messageStrings.get(0).length();
        char[] errorCorrectedMessageChars = new char[messageLength];
        char currentMessageCharacter, monitoredCommonalityCharacter;
        int monitoredCharCount;
        Integer existingCount;
        HashMap<Character, Integer> charToFrequencyMap;
        for (int charIndex = 0; charIndex < messageLength; charIndex++) {
            charToFrequencyMap = new HashMap<>();
            monitoredCharCount = leastCommonLetterMode ? Integer.MAX_VALUE : -1;
            monitoredCommonalityCharacter = '#';
            for (int messageIndex = 0; messageIndex < messageStrings.size(); messageIndex++) {
                currentMessageCharacter = messageStrings.get(messageIndex).charAt(charIndex);
                existingCount = charToFrequencyMap.get(currentMessageCharacter);
                charToFrequencyMap.put(currentMessageCharacter, existingCount == null ? 1 : existingCount+1);
            }
            for (Map.Entry<Character, Integer> entry : charToFrequencyMap.entrySet()) {
                char character = entry.getKey();
                int count = entry.getValue();
                if (leastCommonLetterMode ? count < monitoredCharCount : count > monitoredCharCount) {
                    monitoredCommonalityCharacter = character;
                    monitoredCharCount = count;
                }
            }
            errorCorrectedMessageChars[charIndex] = monitoredCommonalityCharacter;
        }
        System.out.println("Error-corrected message: "+String.valueOf(errorCorrectedMessageChars));
    }

}
