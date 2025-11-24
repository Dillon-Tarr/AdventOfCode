package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

class Day17 {
    static private final int DAY = 17;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private int lowY = Integer.MAX_VALUE, highY = Integer.MIN_VALUE,
            lowX = Integer.MAX_VALUE, highX = Integer.MIN_VALUE;
    static private char[][] tiles;
    static private final ArrayDeque<DropPoint> queuedDropPoints = new ArrayDeque<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        simulate();
        printAndCountWetTiles();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        ArrayList<String> inputStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        ArrayList<InputRowData> inputRowData = new ArrayList<>();
        for (String s : inputStrings) {
            int n1 = Integer.parseInt(s.substring(2, s.indexOf(',')));
            String[] subs = s.substring(s.lastIndexOf('=')+1).split("\\.\\.");
            int n2 = Integer.parseInt(subs[0]);
            int n3 = Integer.parseInt(subs[1]);
            char firstChar = s.charAt(0);
            inputRowData.add(new InputRowData(firstChar, n1, n2, n3));
            if (firstChar == 'x') {
                if (n1 < lowX) lowX = n1; if (n1 > highX) highX = n1;
                if (n2 < lowY) lowY = n2; if (n3 > highY) highY = n3;
            } else {
                if (n1 < lowY) lowY = n1; if (n1 > highY) highY = n1;
                if (n2 < lowX) lowX = n2; if (n3 > highX) highX = n3;
            }
        }
        lowX--; highX++;
        tiles = new char[highY+2][];
        for (int y = 0; y <= highY; y++) {
            tiles[y] = new char[highX+1];
            for (int x = lowX; x <= highX; x++) tiles[y][x] = '.';
        }
        for (var data : inputRowData) {
            if (data.firstChar == 'x') for (int y = data.n2; y <= data.n3; y++) tiles[y][data.n1] = '#';
            else for (int x = data.n2; x <= data.n3; x++) tiles[data.n1][x] = '#';
        }
    }
    
    private static void simulate() {
        DropPoint point; int y = lowY, x = 500, leftDropX, rightDropX, dropY;
        tiles[y][x] = '|';
        queuedDropPoints.add(new DropPoint(y, x));
        while (!queuedDropPoints.isEmpty()) {
            point = queuedDropPoints.remove();
            y = point.y; x = point.x; dropY = drop(y, x);
            if (dropY == y) continue; else y = dropY;
            while (true) { // fill
                if (y < lowY) break;
                tiles[y][x] = '|'; // critically important for when filling upward at a different x than original drop into a "container"
                leftDropX = getLeftDropX(y, x);
                rightDropX = getRightDropX(y, x);
                if (leftDropX == -1 && rightDropX == -1) markRowAsStanding(y--, x);
                else {
                    if (leftDropX != -1) queuedDropPoints.add(new DropPoint(y, leftDropX));
                    if (rightDropX != -1) queuedDropPoints.add(new DropPoint(y, rightDropX));
                    break;
                }
            }
        }
    }

    private static int drop(int y, int x) {
        loop: while (++y <= highY) {
            switch (tiles[y][x]) {
                case '#', '~' -> { break loop; }
                default -> tiles[y][x] = '|';
            }
        }
        return y <= highY ? --y : -1;
    }

    private static int getLeftDropX(int y, int x) {
        char[] row = tiles[y], underRow = tiles[y+1];
        while (--x >= lowX && row[x] != '#') {
            row[x] = '|';
            switch (underRow[x]) {
                case '#', '~' -> {}
                default -> { return x; }
            }
        }
        return -1;
    }

    private static int getRightDropX(int y, int x) {
        char[] row = tiles[y], underRow = tiles[y+1];
        while (++x <= highX && row[x] != '#') {
            row[x] = '|';
            switch (underRow[x]) {
                case '#', '~' -> {}
                default -> { return x; }
            }
        }
        return -1;
    }

    private static void markRowAsStanding(int y, int centerX) {
        int x = centerX;
        char[] row = tiles[y], underRow = tiles[y+1];
        row[x] = '~';
        leftLoop: while (row[--x] != '#') {
            switch (underRow[x]) {
                case '#', '~' -> row[x] = '~';
                default -> { break leftLoop; }
            }
        }
        x = centerX;
        rightLoop: while (row[++x] != '#') {
            switch (underRow[x]) {
                case '#', '~' -> row[x] = '~';
                default -> { break rightLoop; }
            }
        }
    }

    private static void printAndCountWetTiles() {
        int runningCount = 0, standingCount = 0;
        System.out.println();
        StringBuilder sb = new StringBuilder();
        for (int y = lowY; y <= highY; y++) {
            sb.setLength(0);
            for (int x = lowX; x <= highX; x++) {
                char ch = tiles[y][x];
                sb.append(ch);
                switch (tiles[y][x]) {
                    case '~'-> standingCount++;
                    case '|' -> runningCount++;
                }
            }
            System.out.println(sb+" y = "+y);
        }
        System.out.println("\nCount of wet tiles (part 1 answer): "+(standingCount+runningCount));
        System.out.println("\nCount of standing water tiles (part 2 answer): "+standingCount);
    }

    private record InputRowData(char firstChar, int n1, int n2, int n3){}

    private record DropPoint(int y, int x) {

    }

}
