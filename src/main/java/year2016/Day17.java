package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.PriorityQueue;

class Day17 {
    static private final int DAY = 17;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private String passcode;
    static private MessageDigest messageDigest;

    static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        prepMessageDigest();
        if (args.length == 0) // 0 args for part 1, >0 for part 2;
            hashOutShortestPathToVault();
        else hashOutLongestPathToVault();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            passcode = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void prepMessageDigest() {
        try {messageDigest = MessageDigest.getInstance("MD5");} catch (NoSuchAlgorithmException e) {throw new RuntimeException(e);}
    }

    private static void hashOutShortestPathToVault() {
        PriorityQueue<QueuedState> stateQueue = new PriorityQueue<>();
        stateQueue.add(new QueuedState(0, 0, 0, ""));
        QueuedState state;
        int x, y, newDistance;
        String path;
        StringBuilder sb = new StringBuilder();
        while (!stateQueue.isEmpty()) {
            state = stateQueue.remove();
            x = state.x;
            y = state.y;
            path = state.path;
            if (x == 3 && y == 3) {
                System.out.println("\nPath travelled on shortest path: "+path);
                break;
            }
            newDistance = state.distanceTravelled+1;
            byte[] bytes = messageDigest.digest((passcode+path).getBytes());
            sb.setLength(0);
            sb.append(String.format("%02x", bytes[0]));
            sb.append(String.format("%02x", bytes[1]));
            if (y > 0) switch(sb.charAt(0)) {
                case 'b', 'c', 'd', 'e', 'f' -> stateQueue.add(new QueuedState(x, y-1, newDistance, path+'U'));
            }
            if (y < 3) switch(sb.charAt(1)) {
                case 'b', 'c', 'd', 'e', 'f' -> stateQueue.add(new QueuedState(x, y+1, newDistance, path+'D'));
            }
            if (x > 0) switch(sb.charAt(2)) {
                case 'b', 'c', 'd', 'e', 'f' -> stateQueue.add(new QueuedState(x-1, y, newDistance, path+'L'));
            }
            if (x < 3) switch(sb.charAt(3)) {
                case 'b', 'c', 'd', 'e', 'f' -> stateQueue.add(new QueuedState(x+1, y, newDistance, path+'R'));
            }
        }
    }

    private static void hashOutLongestPathToVault() {
        int longestPathToVault = -1;
        ArrayList<QueuedState> stateList = new ArrayList<>();
        stateList.add(new QueuedState(0, 0, 0, ""));
        QueuedState state;
        int x, y, d, newDistance;
        String path;
        StringBuilder sb = new StringBuilder();
        while (!stateList.isEmpty()) {
            state = stateList.removeLast();
            x = state.x;
            y = state.y;
            path = state.path;
            if (x == 3 && y == 3) {
                d = state.distanceTravelled;
                if (d > longestPathToVault) longestPathToVault = d;
                continue;
            }
            newDistance = state.distanceTravelled+1;
            byte[] bytes = messageDigest.digest((passcode+path).getBytes());
            sb.setLength(0);
            sb.append(String.format("%02x", bytes[0]));
            sb.append(String.format("%02x", bytes[1]));
            if (y > 0) switch(sb.charAt(0)) {
                case 'b', 'c', 'd', 'e', 'f' -> stateList.add(new QueuedState(x, y-1, newDistance, path+'U'));
            }
            if (y < 3) switch(sb.charAt(1)) {
                case 'b', 'c', 'd', 'e', 'f' -> stateList.add(new QueuedState(x, y+1, newDistance, path+'D'));
            }
            if (x > 0) switch(sb.charAt(2)) {
                case 'b', 'c', 'd', 'e', 'f' -> stateList.add(new QueuedState(x-1, y, newDistance, path+'L'));
            }
            if (x < 3) switch(sb.charAt(3)) {
                case 'b', 'c', 'd', 'e', 'f' -> stateList.add(new QueuedState(x+1, y, newDistance, path+'R'));
            }
        }
        System.out.println("Longest path to vault: "+ longestPathToVault);
    }

    private record QueuedState (int x, int y, int distanceTravelled, String path) implements Comparable <QueuedState> {

        @Override
        public int compareTo(QueuedState o) {
            return Integer.compare(this.distanceTravelled, o.distanceTravelled);
        }

    }

}
