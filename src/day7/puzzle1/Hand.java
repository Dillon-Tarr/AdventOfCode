package day7.puzzle1;

import java.util.ArrayList;

public class Hand {
    private final char[] cardCharacters = new char[5];
    private final CardType[] cards = new CardType[5];
    private final int bid;
    private HandType type;

    Hand(String[] strings) {
        char[] cards = strings[0].toCharArray();
        for (int i = 0; i < cards.length; i++) {
            cardCharacters[i] = cards[i];
            switch (cards[i]) {
                case '2' -> this.cards[i] = CardType.TWO;
                case '3' -> this.cards[i] = CardType.THREE;
                case '4' -> this.cards[i] = CardType.FOUR;
                case '5' -> this.cards[i] = CardType.FIVE;
                case '6' -> this.cards[i] = CardType.SIX;
                case '7' -> this.cards[i] = CardType.SEVEN;
                case '8' -> this.cards[i] = CardType.EIGHT;
                case '9' -> this.cards[i] = CardType.NINE;
                case 'T' -> this.cards[i] = CardType.TEN;
                case 'J' -> this.cards[i] = CardType.JACK;
                case 'Q' -> this.cards[i] = CardType.QUEEN;
                case 'K' -> this.cards[i] = CardType.KING;
                case 'A' -> this.cards[i] = CardType.ACE;
            }
        }
        this.bid = Integer.parseInt(strings[1]);
        evaluateHandType();
    }

    private void evaluateHandType(){
        ArrayList<CardTypeCounter> cardTypeCounters = new ArrayList<>();
        cardTypeCounters.add(new CardTypeCounter(cards[0]));
        for (int i = 1; i < cards.length; i++) {
            boolean needToAddNewCharacterCounter = true;
            for (CardTypeCounter cardTypeCounter : cardTypeCounters) {
                if (cards[i] == cardTypeCounter.cardType) {
                    cardTypeCounter.incrementCount();
                    needToAddNewCharacterCounter = false;
                    break;
                }
            }
            if (needToAddNewCharacterCounter) cardTypeCounters.add(new CardTypeCounter(cards[i]));
        }

        if (cardTypeCounters.size() == 5) {type = HandType.HIGH_CARD;}
        else if (cardTypeCounters.size() == 4) {type = HandType.ONE_PAIR;}
        else if (cardTypeCounters.size() == 1) {type = HandType.FIVE_OF_A_KIND;}
        else if (cardTypeCounters.size() == 2) {
            if (Math.max((cardTypeCounters.get(0).count),(cardTypeCounters.get(1).count)) == 4)
                type = HandType.FOUR_OF_A_KIND;
            else type = HandType.FULL_HOUSE;
        } else {
            if (cardTypeCounters.get(0).count == 3 || cardTypeCounters.get(1).count == 3 || cardTypeCounters.get(2).count == 3)
                type = HandType.THREE_OF_A_KIND;
            else type = HandType.TWO_PAIR;
        }
    }

    private class CardTypeCounter {
        CardType cardType;
        int count;

        CardTypeCounter(CardType cardType) {
            this.cardType = cardType;
            count = 1;
        }

        void incrementCount() {this.count++;}
    }

    @Override
    public String toString() {
        return "\nCards: "+String.valueOf(cardCharacters)+"\nBid: "+bid+"\nType: "+type;
    }

}
