package day7.puzzle1;

import java.util.ArrayList;

public class Hand {
    private final char[] cards;
    private final int bid;
    private HandType type;

    Hand(String[] strings) {
        this.cards = strings[0].toCharArray();
        this.bid = Integer.parseInt(strings[1]);
        evaluateHandType();
    }



    private void evaluateHandType(){
        ArrayList<CharacterCounter> characterCounters = new ArrayList<>();
        characterCounters.add(new CharacterCounter(cards[0]));
        for (int i = 1; i < cards.length; i++) {
            boolean needToAddNewCharacterCounter = true;
            for (CharacterCounter characterCounter : characterCounters) {
                if (cards[i] == characterCounter.character) {
                    characterCounter.incrementCount();
                    needToAddNewCharacterCounter = false;
                    break;
                }
            }
            if (needToAddNewCharacterCounter) characterCounters.add(new CharacterCounter(cards[i]));
        }

        if (characterCounters.size() == 5) {type = HandType.HIGH_CARD;}
        else if (characterCounters.size() == 4) {type = HandType.ONE_PAIR;}
        else if (characterCounters.size() == 1) {type = HandType.FIVE_OF_A_KIND;}
        else if (characterCounters.size() == 2) {
            if (Math.max((characterCounters.get(0).count),(characterCounters.get(1).count)) == 4)
                type = HandType.FOUR_OF_A_KIND;
            else type = HandType.FULL_HOUSE;
        } else {
            if (characterCounters.get(0).count == 3 || characterCounters.get(1).count == 3 || characterCounters.get(2).count == 3)
                type = HandType.THREE_OF_A_KIND;
            else type = HandType.TWO_PAIR;
        }
    }

    private class CharacterCounter {
        char character;
        int count;

        CharacterCounter(char character) {
            this.character = character;
            count = 1;
        }

        void incrementCount() {this.count++;}

    }

    @Override
    public String toString() {
        return "\nCards: "+String.valueOf(cards)+"\nBid: "+bid+"\nType: "+type;
    }

}
