package year2023.day4;

import java.util.Arrays;

class Card {
    private final int pointsThisCardIsWorth;

    Card(int[] winningNumbers, int[] myNumbers) {
        int numberOfWinningNumbersIHave = (int) Arrays.stream(myNumbers)
                .filter(myNumber -> Arrays.stream(winningNumbers).anyMatch(winningNumber -> myNumber == winningNumber))
                .count();
        if (numberOfWinningNumbersIHave >= 3) pointsThisCardIsWorth = (int) Math.pow(2, numberOfWinningNumbersIHave -1);
        else pointsThisCardIsWorth = numberOfWinningNumbersIHave;
    }

    public int getPointsThisCardIsWorth() {return pointsThisCardIsWorth;}
}
