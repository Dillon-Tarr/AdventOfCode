package year2023.day4;

import java.io.*;
import java.util.ArrayList;

class Part1 {
    static private final int DAY = 4;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private final String[] cardStrings = new String[198];
    static private final ArrayList<Card> cards = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getCardsData();
        evaluateCards();
        sumCardPointValues();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
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

    private static void evaluateCards() {
        for (String cardString : cardStrings) {
            int dividerIndex = cardString.indexOf("|");
            int[] winningNumbers = getWinningNumbers(cardString, dividerIndex);
            int[] myNumbers = getMyNumbers(cardString, dividerIndex);
            cards.add(new Card(winningNumbers, myNumbers));
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

    private static void sumCardPointValues() {
        int sumOfAllCardPointValues = 0;
        System.out.println("\nAll cards evaluated. Point values of each card:\n");
        for (int i = 0; i < cards.size(); i++) {
            int points = cards.get(i).getPointsThisCardIsWorth();
            System.out.println("day4.puzzle1.Card "+(i+1)+": "+points);
            sumOfAllCardPointValues += points;
        }
        System.out.println("\nSUM OF POINT VALUES OF ALL CARDS:\n\n"+sumOfAllCardPointValues);
    }

}
