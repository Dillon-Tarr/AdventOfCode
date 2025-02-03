package day7.part2;

import java.util.ArrayList;
import java.util.Comparator;

public class Hand {
    private final char[] cardCharacters = new char[5];
    private final CardType[] cardTypes = new CardType[5];
    private final ArrayList<CardTypeCounter> cardTypeCounters = new ArrayList<>();
    private final HandType type;
    private final long score;
    private final int bid;

    Hand(String[] strings) {
        char[] cards = strings[0].toCharArray();
        for (int i = 0; i < cards.length; i++) {
            cardCharacters[i] = cards[i];
            switch (cards[i]) {
                case 'J' -> this.cardTypes[i] = CardType.JOKER;
                case '2' -> this.cardTypes[i] = CardType.TWO;
                case '3' -> this.cardTypes[i] = CardType.THREE;
                case '4' -> this.cardTypes[i] = CardType.FOUR;
                case '5' -> this.cardTypes[i] = CardType.FIVE;
                case '6' -> this.cardTypes[i] = CardType.SIX;
                case '7' -> this.cardTypes[i] = CardType.SEVEN;
                case '8' -> this.cardTypes[i] = CardType.EIGHT;
                case '9' -> this.cardTypes[i] = CardType.NINE;
                case 'T' -> this.cardTypes[i] = CardType.TEN;
                case 'Q' -> this.cardTypes[i] = CardType.QUEEN;
                case 'K' -> this.cardTypes[i] = CardType.KING;
                case 'A' -> this.cardTypes[i] = CardType.ACE;
            }
        }
        createCardTypeCounters();
        this.type = returnHandType();
        this.score = evaluateScore();
        this.bid = Integer.parseInt(strings[1]);
    }

    public long getScore() {return score;}
    public int getBid() {return bid;}

    private void createCardTypeCounters() {
        cardTypeCounters.add(new CardTypeCounter(cardTypes[0]));
        for (int i = 1; i < cardTypes.length; i++) {
            boolean needToAddNewCharacterCounter = true;
            for (CardTypeCounter cardTypeCounter : cardTypeCounters) {
                if (cardTypes[i] == cardTypeCounter.getCardType()) {
                    cardTypeCounter.incrementCount();
                    needToAddNewCharacterCounter = false;
                    break;
                }
            }
            if (needToAddNewCharacterCounter) cardTypeCounters.add(new CardTypeCounter(cardTypes[i]));
        }
    }

    private HandType returnHandType(){
        if (cardTypeCounters.size() == 1) return HandType.FIVE_OF_A_KIND;

        cardTypeCounters.sort(Comparator.comparing(CardTypeCounter::getCardType));
        CardTypeCounter maybeJokers = cardTypeCounters.get(0);
        int jokerCount = 0;
        if (maybeJokers.cardType == CardType.JOKER) jokerCount = maybeJokers.count;

        if (jokerCount == 0) {
            if (cardTypeCounters.size() == 2) {
                if (Math.max((cardTypeCounters.get(0).getCount()), (cardTypeCounters.get(1).getCount())) == 4) return HandType.FOUR_OF_A_KIND;
                else return HandType.FULL_HOUSE;
            }
            if (cardTypeCounters.size() == 3) {
                if (cardTypeCounters.get(0).getCount() == 3 || cardTypeCounters.get(1).getCount() == 3 || cardTypeCounters.get(2).getCount() == 3)
                    return HandType.THREE_OF_A_KIND;
                else return HandType.TWO_PAIR;
            }
            if (cardTypeCounters.size() == 4) return HandType.ONE_PAIR;
            else return HandType.HIGH_CARD;
        } else if (jokerCount == 1) {
            if (cardTypeCounters.size() == 2) return HandType.FIVE_OF_A_KIND;
            if (cardTypeCounters.size() == 3) return cardTypeCounters.get(1).count == 2 ? HandType.FULL_HOUSE : HandType.FOUR_OF_A_KIND;
            if (cardTypeCounters.size() == 4) return HandType.THREE_OF_A_KIND;
            else return HandType.ONE_PAIR;
        } else if (jokerCount == 2) {
            if (cardTypeCounters.size() == 2) return HandType.FIVE_OF_A_KIND;
            if (cardTypeCounters.size() == 3) return HandType.FOUR_OF_A_KIND;
            else return HandType.THREE_OF_A_KIND;
        } else if (jokerCount == 3) return cardTypeCounters.size() == 2 ? HandType.FIVE_OF_A_KIND : HandType.FOUR_OF_A_KIND;
          else return HandType.FIVE_OF_A_KIND; // jokerCount == 4
    }

    private class CardTypeCounter {
        private final CardType cardType;
        private int count;

        CardTypeCounter(CardType cardType) {
            this.cardType = cardType;
            count = 1;
        }

        void incrementCount() {this.count++;}

        CardType getCardType() {return cardType;}
        int getCount() {return count;}

    }

    private long evaluateScore() {
        StringBuilder scoreStringBuilder = new StringBuilder();
        scoreStringBuilder.append(type.ordinal()+1);
        for (CardType type : cardTypes){
            if (type.ordinal() < 9) scoreStringBuilder.append("0");
            scoreStringBuilder.append(type.ordinal()+1);
            }
        return Long.parseLong(scoreStringBuilder.toString());
    }

    @Override
    public String toString() {
        return "\nCards: "+String.valueOf(cardCharacters)+"\nCard types score: "+ score +"\nBid: "+bid+"\nType: "+type;
    }

}
