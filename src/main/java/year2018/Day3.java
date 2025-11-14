package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

class Day3 {
    static private final int DAY = 3;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final ArrayList<Claim> claims = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        processInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processInputData() {
        String[] subs, xy, wh;
        for (String s : inputStrings) {
            subs = s.substring(s.indexOf('@')+2).split(": ");
            xy = subs[0].split(",");
            wh = subs[1].split("x");
            int lowX = Integer.parseInt(xy[0]);
            int highX = lowX - 1 + Integer.parseInt(wh[0]);
            int lowY = Integer.parseInt(xy[1]);
            int highY = lowY - 1 + Integer.parseInt(wh[1]);
            claims.add(new Claim(lowX, highX, lowY, highY));
        }
    }

    private static void solve() {
        int count = 0;
        HashSet<String> claimedOnceSet = new HashSet<>(), claimedTwiceSet = new HashSet<>();
        String coordinatesString;
        for (Claim claim : claims) for (int x = claim.lowX; x <= claim.highX; x++) for (int y = claim.lowY; y <= claim.highY; y++) {
            coordinatesString = x+","+y;
            if (!claimedOnceSet.add(coordinatesString) && claimedTwiceSet.add(coordinatesString)) count++;
        }
        System.out.println("\nCount of square inches claimed more than once (part 1 answer): "+count);
        Claim claim;
        claimLoop: for (int i = 0; i < claims.size(); i++) {
            claim = claims.get(i);
            for (int x = claim.lowX; x <= claim.highX; x++) for (int y = claim.lowY; y <= claim.highY; y++)
                if (claimedTwiceSet.contains(x+","+y)) continue claimLoop;
            System.out.println("\nID of the only claim that doesn't overlap (part 2 answer): "+(i+1));
            return;
        }
        throw new RuntimeException("Part 2 answer could not be found. There is an error in either input or program logic.");
    }

    private record Claim(int lowX, int highX, int lowY, int highY){}

}
