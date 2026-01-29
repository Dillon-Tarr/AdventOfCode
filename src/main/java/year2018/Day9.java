package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;

class Day9 {
    static private final int DAY = 9;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private int playerCount, part1FinalScoringMarbleNumber, part2FinalScoringMarbleNumber, scoringMarbleNumber = 0, playerNumber = 0;
    static private final ArrayDeque<Integer> circle = new ArrayDeque<>();
    static private final HashMap<Integer, Long> playerToScoreMap = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        String s;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            s = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
        playerCount = Integer.parseInt(s.substring(0, s.indexOf(' ')));
        int lastMarble = Integer.parseInt(s.substring(s.indexOf("h")+2, s.lastIndexOf(' ')));
        part1FinalScoringMarbleNumber = lastMarble*23/23;
        part2FinalScoringMarbleNumber = lastMarble*2300/23;
    }

    private static void solve() {
        circle.add(0);
        runGameUntil(part1FinalScoringMarbleNumber);
        System.out.println("\nHighest score (part 1 answer): "+ getCurrentHighestScore());
        runGameUntil(part2FinalScoringMarbleNumber);
        System.out.println("\nHighest score (part 2 answer): "+ getCurrentHighestScore());
    }

    private static void runGameUntil(int finalScoringMarbleNumber) {
        while ((scoringMarbleNumber+=23) <= finalScoringMarbleNumber) {
            for (int i = scoringMarbleNumber-22; i < scoringMarbleNumber; i++) { circle.add(circle.remove()); circle.add(i); }
            playerNumber+=23; while (playerNumber > playerCount) playerNumber -= playerCount;
            for (int i = 0; i < 7; i++) circle.addFirst(circle.removeLast());
            playerToScoreMap.put(playerNumber, playerToScoreMap.getOrDefault(playerNumber, 0L)+scoringMarbleNumber+circle.removeLast());
            circle.add(circle.remove());
        }
        scoringMarbleNumber-=23;
    }

    private static long getCurrentHighestScore() {
        long highestScore = -1, score;
        for (var entry : playerToScoreMap.entrySet()) { score = entry.getValue(); if (score > highestScore) highestScore = score; }
        return highestScore;
    }

}
