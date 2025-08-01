package year2016.day4.part1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Room {
    String encryptedName;
    int sectorID;
    String checksum;
    private final StringBuilder calculatedChecksum = new StringBuilder();

    boolean checkIfReal() {return checksum.contentEquals(calculatedChecksum);}

    Room(String s) {
        int lastDashIndex = s.lastIndexOf('-');
        encryptedName = s.substring(0, lastDashIndex);
        int openingBracketIndex = s.indexOf('[');
        sectorID = Integer.parseInt(s.substring(lastDashIndex+1, openingBracketIndex));
        checksum = s.substring(openingBracketIndex+1, s.length()-1);

        HashMap<Integer, String> frequencyToCharsMap = mapFrequencyToChars();

        ArrayList<Integer> lowestToHighestCounts = new ArrayList<>(frequencyToCharsMap.keySet());
        lowestToHighestCounts.sort(Integer::compareTo);
        for (int i = lowestToHighestCounts.size()-1; i >= 0; i--) {
            String chars = frequencyToCharsMap.get(lowestToHighestCounts.get(i));
            if (chars == null) continue;
            calculatedChecksum.append(chars);
            if (calculatedChecksum.length() >= 5) break;
        }
        calculatedChecksum.setLength(5);
    }

    private HashMap<Integer, String> mapFrequencyToChars() {
        char c;
        Integer currentCount;
        HashMap<Character, Integer> letterFrequencyMap = new HashMap<>();
        for (int i = 0; i < encryptedName.length(); i++) {
            c = encryptedName.charAt(i);
            if (c == '-') continue;
            currentCount = letterFrequencyMap.get(c);
            letterFrequencyMap.put(c, currentCount == null ? 1 : currentCount+1);
        }

        HashMap<Integer, String> frequencyToCharsMap = new HashMap<>();
        letterFrequencyMap.forEach((k, v) -> frequencyToCharsMap.merge(v, k+"", String::concat));
        frequencyToCharsMap.forEach((k, v) -> {
            char[] charArray = v.toCharArray();
            Arrays.sort(charArray);
            frequencyToCharsMap.put(k, String.valueOf(charArray));
        });
        return frequencyToCharsMap;
    }

}
