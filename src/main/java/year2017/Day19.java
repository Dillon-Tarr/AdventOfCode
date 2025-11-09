package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static shared.CardinalDirection.*;

class Day19 {
    static private final int DAY = 19;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private char[][] diagram;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        int length, longest = 0;
        ArrayList<String> diagramStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                length = s.length();
                if (length > longest) {
                    if (s.charAt(length-1) != ' ') {
                        s += ' ';
                        longest = length+1;
                    } else longest = length;
                }
                diagramStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        diagram = new char[diagramStrings.size()][];
        for (int y = 0; y < diagramStrings.size(); y++) {
            String string = diagramStrings.get(y);
            length = string.length();
            if (length < longest) {
                string += " ".repeat(longest-length);
            }
            diagram[y] = string.toCharArray();
        }
    }

    private static void solve() {
        int x = -1, y = 0;
        var row = diagram[0];
        for (int i = 0; i < row.length; i++) if (row[i] != ' ') { x = i; break; }
        if (x == -1) throw new RuntimeException("Text input is incorrect.");
        int westX = x-1, northY = y-1,
                stepCount = 0; // +1 step onto grid, but -1 for step off end of path to empty space.
        char currentChar;
        var currentDirection = SOUTH;
        var pathLetters = new StringBuilder();
        loop: while (++stepCount < Integer.MAX_VALUE) {
            switch (currentDirection) { // Move.
                case SOUTH -> { y++; northY++; }
                case NORTH -> { y--; northY--; }
                case WEST -> { x--; westX--; }
                case EAST -> { x++; westX++; }
            }
            currentChar = diagram[y][x];
            switch (currentChar) { // Check char and do appropriate thing.
                case ' ' -> { break loop; }
                case '+' -> currentDirection = switch (currentDirection) {
                    case SOUTH, NORTH -> switch (diagram[y][westX]) {
                        case ' ', '|' -> EAST;
                        default -> WEST;
                    };
                    case WEST, EAST -> switch (diagram[northY][x]) {
                        case ' ', '-' -> SOUTH;
                        default -> NORTH;
                    };
                    default -> throw new RuntimeException("This should never happen.");
                };
                default -> { if (Character.isAlphabetic(currentChar)) pathLetters.append(currentChar); }
            }
        }
        System.out.println("\nLetters encountered (in order) on path (part 1 answer): "+pathLetters);
        System.out.println("\nSteps taken (part 2 answer): "+stepCount);
    }

}
