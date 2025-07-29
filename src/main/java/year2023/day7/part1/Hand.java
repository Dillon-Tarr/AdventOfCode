package year2023.day7.part1;

import java.util.ArrayList;

class Hand {
    private final char[] cardCharacters = new char[5];
    private final CardType[] cardTypes = new CardType[5];
    private HandType type;
    private long score;
    private final int bid;

    Hand(String[] strings) {
        char[] cards = strings[0].toCharArray();
        for (int i = 0; i < cards.length; i++) {
            cardCharacters[i] = cards[i];
            switch (cards[i]) {
                case '2' -> this.cardTypes[i] = CardType.TWO;
                case '3' -> this.cardTypes[i] = CardType.THREE;
                case '4' -> this.cardTypes[i] = CardType.FOUR;
                case '5' -> this.cardTypes[i] = CardType.FIVE;
                case '6' -> this.cardTypes[i] = CardType.SIX;
                case '7' -> this.cardTypes[i] = CardType.SEVEN;
                case '8' -> this.cardTypes[i] = CardType.EIGHT;
                case '9' -> this.cardTypes[i] = CardType.NINE;
                case 'T' -> this.cardTypes[i] = CardType.TEN;
                case 'J' -> this.cardTypes[i] = CardType.JACK;
                case 'Q' -> this.cardTypes[i] = CardType.QUEEN;
                case 'K' -> this.cardTypes[i] = CardType.KING;
                case 'A' -> this.cardTypes[i] = CardType.ACE;
            }
        }
        evaluateHandType();
        evaluateScore();
        this.bid = Integer.parseInt(strings[1]);
    }

    long getScore() {return score;}
    int getBid() {return bid;}

    private void evaluateHandType(){
        ArrayList<CardTypeCounter> cardTypeCounters = new ArrayList<>();
        cardTypeCounters.add(new CardTypeCounter(cardTypes[0]));
        for (int i = 1; i < cardTypes.length; i++) {
            boolean needToAddNewCharacterCounter = true;
            for (CardTypeCounter cardTypeCounter : cardTypeCounters) {
                if (cardTypes[i] == cardTypeCounter.cardType) {
                    cardTypeCounter.incrementCount();
                    needToAddNewCharacterCounter = false;
                    break;
                }
            }
            if (needToAddNewCharacterCounter) cardTypeCounters.add(new CardTypeCounter(cardTypes[i]));
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

    private void evaluateScore() {
        StringBuilder scoreStringBuilder = new StringBuilder();
        scoreStringBuilder.append(type.ordinal()+1);
        for (CardType type : cardTypes){
            if (type.ordinal() < 9) scoreStringBuilder.append("0");
            scoreStringBuilder.append(type.ordinal()+1);
            }
        this.score = Long.parseLong(scoreStringBuilder.toString());
    }

    @Override
    public String toString() {
        return "\nCards: "+String.valueOf(cardCharacters)+"\nCard types score: "+ score +"\nBid: "+bid+"\nType: "+type;
    }

}
