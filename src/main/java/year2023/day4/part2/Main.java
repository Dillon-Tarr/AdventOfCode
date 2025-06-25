package year2023.day4.part2;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    static private final int DAY = 4;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private final String[] cardStrings = new String[198];
    static private final int[] matchingNumberCounts = new int[198];
    static private final int[] cardNumberCounts = new int[198];

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getCardsData();
        getMatchingNumberCounts();
        addInitialCardNumberCounts();
        processCards();
        sumCardCounts();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getCardsData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            int i = 0;
            while (currentLine != null) {
                cardStrings[i] = currentLine;
                currentLine = br.readLine();
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getMatchingNumberCounts() {
        for (int i = 0; i < 198; i++) {
            String cardString = cardStrings[i];
            int dividerIndex = cardString.indexOf("|");
            int[] winningNumbers = getWinningNumbers(cardString, dividerIndex);
            int[] myNumbers = getMyNumbers(cardString, dividerIndex);
            matchingNumberCounts[i] = (int) Arrays.stream(myNumbers)
                    .filter(myNumber -> Arrays.stream(winningNumbers).anyMatch(winningNumber -> myNumber == winningNumber))
                    .count();
        }
    }

        private static int[] getWinningNumbers(String cardString, int dividerIndex) {
            int colonIndex = cardString.indexOf(":");
            String winningNumbersString = cardString.substring(colonIndex+2, dividerIndex-1);
            ArrayList<String> winningNumberStrings = new ArrayList<>(10);
            while (winningNumbersString.length() >= 5) {
                winningNumberStrings.add(winningNumbersString.substring(0, 2));
                winningNumbersString = winningNumbersString.substring(3);
            }
            winningNumberStrings.add(winningNumbersString);

            int[] winningNumbers = new int[winningNumberStrings.size()];
            for (int i = 0; i < winningNumberStrings.size(); i++) winningNumbers[i] = Integer.parseInt(winningNumberStrings.get(i).trim());
            return winningNumbers;
        }

        private static int[] getMyNumbers(String cardString, int dividerIndex) {
            String myNumbersString = cardString.substring(dividerIndex+2);
            ArrayList<String> myNumberStrings = new ArrayList<>(10);
            while (myNumbersString.length() >= 5) {
                myNumberStrings.add(myNumbersString.substring(0, 2));
                myNumbersString = myNumbersString.substring(3);
            }
            myNumberStrings.add(myNumbersString);

            int[] myNumbers = new int[myNumberStrings.size()];
            for (int i = 0; i < myNumberStrings.size(); i++) myNumbers[i] = Integer.parseInt(myNumberStrings.get(i).trim());
            return myNumbers;
        }

    private static void addInitialCardNumberCounts() {
        Arrays.fill(cardNumberCounts, 1);
    }

    private static void processCards() {
        for (int cardNumber = 1; cardNumber <= 198; cardNumber++) {
            int cardNumberArrayIndex = cardNumber - 1;
            int matchingNumberCount = matchingNumberCounts[cardNumberArrayIndex];
            int countOfThisCard = cardNumberCounts[cardNumberArrayIndex];
            System.out.println("Count of card #"+cardNumber+": "+countOfThisCard+". Matches on these cards: "+matchingNumberCount);

            for (int i = 1; (i <= matchingNumberCount && cardNumber+i <= 198); i++) {
                cardNumberCounts[cardNumberArrayIndex+i] += countOfThisCard;
            }
        }
    }

    private static void sumCardCounts() {
        int sumOfAllCardCounts = 0;
        for (int count: cardNumberCounts) sumOfAllCardCounts += count;

        System.out.println("\nAll original cards and copies processed.\n\nTotal count of processed cards:\n\n"+sumOfAllCardCounts);
    }

}