package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Day9 {
    static private final int DAY = 9;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private int playerCount, finalScoringMarbleNumber;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        String s;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            s = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
        playerCount = Integer.parseInt(s.substring(0, s.indexOf(' ')));
        finalScoringMarbleNumber = Integer.parseInt(s.substring(s.indexOf("h")+2, s.lastIndexOf(' ')))/23*23;
    }

    private static void solve() {
        ArrayList<Integer> circle = new ArrayList<>(); circle.add(0); circle.add(2); circle.add(1);
        int marbleNumber = 2, playerNumber = 2, twentyThreeCounter = 2, currentIndex = 1;
        HashMap<Integer, Integer> playerToScoreMap = new HashMap<>();
        while (++marbleNumber <= finalScoringMarbleNumber) {
            if (++playerNumber > playerCount) playerNumber = 1;
            if (++twentyThreeCounter == 23) {
                twentyThreeCounter = 0;
                currentIndex -= 7;
                if (currentIndex < 0) currentIndex += circle.size();
                playerToScoreMap.put(playerNumber, playerToScoreMap.getOrDefault(playerNumber, 0)+marbleNumber+circle.remove(currentIndex));
            } else {
                currentIndex +=2;
                if (currentIndex > circle.size()) currentIndex -= circle.size();
                circle.add(currentIndex, marbleNumber);
            }
        }
        int highestScore = -1;
        for (var entry : playerToScoreMap.entrySet()) {
            int score = entry.getValue();
            if (score > highestScore) highestScore = score;
        }
        System.out.println("\nHighest score (part 1 answer): "+highestScore);
    }

}
