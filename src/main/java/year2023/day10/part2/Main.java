package year2023.day10.part2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Main {
    static private final int DAY = 10;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private final ArrayList<char[]> inputLines = new ArrayList<>();
    static private char[][] grid;
    static private final ArrayList<LoopTile> loopTiles = new ArrayList<>();
    static private char[][] metaData; // u: unchecked, p: part of loop pipe, l: lavender, r: red
    static private int gridHeight;
    static private int gridWidth;
    static private int startY; // Coordinates referenced in y-x order since values are always obtained in line-character order.
    static private int startX;
    static private char startExitStepDirection;
    static private char startEntryStepDirection;
    static private char insideTilesColor; // lavender or red

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        setUpImportantVariables();
        traceLoop();
        convertStartTileToProperCharacter();
        paintLeftLavenderAndRightRed();
        spreadPaint();
        identifyInsideTilesColor();
        countInsideTiles();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            while (currentLine != null) {
                inputLines.add(currentLine.toCharArray());
                currentLine = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void setUpImportantVariables() {
        gridWidth = inputLines.get(0).length; gridHeight = inputLines.size();
        grid = new char[gridHeight][]; metaData = new char[gridHeight][];
        for (int y = 0; y < gridHeight; y++) {
            grid[y] = new char[gridWidth]; metaData[y] = new char[gridWidth];
            for (int x = 0; x < gridWidth; x++) {
                char value = inputLines.get(y)[x];
                grid[y][x] = value;
                metaData[y][x] = 'u';
                if (value == 'S') {startY = y; startX = x;}
            }
        }
    }

    private static void traceLoop() {
        int y = startY;
        int x = startX;
        char lastStepDirection;
        char currentCharacter;

        if (grid[y-1][x] == '7' || grid[y -1][x] == '|' || grid[y -1][x] == 'F') {
            lastStepDirection = 'n'; y--;
        } else if (grid[y][x+1] == 'J' || grid[y][x +1] == '-' || grid[y][x +1] == '7') {
            lastStepDirection = 'e'; x++;
        } else if (grid[y+1][x] == 'L' || grid[y +1][x] == '|' || grid[y +1][x] == 'J') {
            lastStepDirection = 's'; y++;
        } else {
            lastStepDirection = 'w'; x--;
        }
        currentCharacter = grid[y][x];
        loopTiles.add(new LoopTile(y, x, currentCharacter, lastStepDirection));
        metaData[y][x] = 'p';
        startExitStepDirection = lastStepDirection;
        do {
            switch (currentCharacter) {
                case '7' -> {
                    if (lastStepDirection == 'n') {lastStepDirection = 'w'; x--;}
                    else {lastStepDirection = 's'; y++;}}
                case 'F' -> {
                    if (lastStepDirection == 'n') {lastStepDirection = 'e'; x++;}
                    else {lastStepDirection = 's'; y++;}}
                case 'J' -> {
                    if (lastStepDirection == 's') {lastStepDirection = 'w'; x--;}
                    else {lastStepDirection = 'n'; y--;}}
                case 'L' -> {
                    if (lastStepDirection == 's') {lastStepDirection = 'e'; x++;}
                    else {lastStepDirection = 'n'; y--;}}
                case '|' -> {
                    if (lastStepDirection == 'n') y--;
                    else y++;}
                case '-' -> {
                    if (lastStepDirection == 'e') x++;
                    else x--;}
            }
            currentCharacter = grid[y][x];
            loopTiles.add(new LoopTile(y, x, currentCharacter, lastStepDirection));
            metaData[y][x] = 'p';
        } while (y != startY || x != startX);
        startEntryStepDirection = lastStepDirection;
    }

    private static void convertStartTileToProperCharacter() {
        switch (startEntryStepDirection) {
            case 'n' -> {
                if (startExitStepDirection == 'n') grid[startY][startX] = '|';
                else if (startExitStepDirection == 'w') grid[startY][startX] = '7';
                else grid[startY][startX] = 'F';}
            case 'e' -> {
                if (startExitStepDirection == 'e') grid[startY][startX] = '-';
                else if (startExitStepDirection == 'n') grid[startY][startX] = 'J';
                else grid[startY][startX] = '7';}
            case 's' -> {
                if (startExitStepDirection == 's') grid[startY][startX] = '|';
                else if (startExitStepDirection == 'e') grid[startY][startX] = 'L';
                else grid[startY][startX] = 'J';}
            case 'w' -> {
                if (startExitStepDirection == 'w') grid[startY][startX] = '-';
                else if (startExitStepDirection == 's') grid[startY][startX] = 'F';
                else grid[startY][startX] = 'L';}
        }
    }

    private static void paintLeftLavenderAndRightRed() {
        for (LoopTile loopTile : loopTiles) {
            int y = loopTile.y();
            int x = loopTile.x();
            char character = loopTile.character();
            switch (loopTile.orientationWhenSteppingOnto()) {
                case 'n' -> {
                    if (character == '7') {
                        tryToPaint(y, x, 'e', 'r');
                        tryToPaint(y, x, 'n', 'r');
                    } else if (character == '|') {
                        tryToPaint(y, x, 'e', 'r');
                        tryToPaint(y, x, 'w', 'l');
                    } else { // character == 'F'
                        tryToPaint(y, x, 'w', 'l');
                        tryToPaint(y, x, 'n', 'l');}}
                case 'e' -> {
                    if (character == 'J') {
                        tryToPaint(y, x, 's', 'r');
                        tryToPaint(y, x, 'e', 'r');
                    } else if (character == '-') {
                        tryToPaint(y, x, 's', 'r');
                        tryToPaint(y, x, 'n', 'l');
                    }
                    else { //character == '7'
                        tryToPaint(y, x, 'n', 'l');
                        tryToPaint(y, x, 'e', 'l');}}
                case 's' -> {
                    if (character == 'L') {
                        tryToPaint(y, x, 'w', 'r');
                        tryToPaint(y, x, 's', 'r');
                    } else if (character == '|') {
                        tryToPaint(y, x, 'w', 'r');
                        tryToPaint(y, x, 'e', 'l');
                    }
                    else { //character == 'J'
                        tryToPaint(y, x, 'e', 'l');
                        tryToPaint(y, x, 's', 'l');}}
                case 'w' -> {
                    if (character == 'F') {
                        tryToPaint(y, x, 'n', 'r');
                        tryToPaint(y, x, 'w', 'r');
                    } else if (character == '-') {
                        tryToPaint(y, x, 'n', 'r');
                        tryToPaint(y, x, 's', 'l');
                    } else { //character == 'L'
                        tryToPaint(y, x, 's', 'l');
                        tryToPaint(y, x, 'w', 'l');}}
            }
        }
    }

    private static void tryToPaint(int fromY, int fromX, char direction, char color){
        if (direction == 'n' && fromY > 0 && metaData[fromY-1][fromX] != 'p') metaData[fromY-1][fromX] = color;
        else if (direction == 'e' && fromX < gridWidth-1 && metaData[fromY][fromX+1] != 'p') metaData[fromY][fromX+1] = color;
        else if (direction == 's' && fromY < gridHeight-1 && metaData[fromY+1][fromX] != 'p') metaData[fromY+1][fromX] = color;
        else if (direction == 'w' && fromX > 0 && metaData[fromY][fromX-1] != 'p') metaData[fromY][fromX-1] = color;
    }

    private static void spreadPaint() {
        for (int y = 0; y < gridHeight; y++) for (int x = 0; x <gridWidth; x++) {
            char paintColor = metaData[y][x];
            if (paintColor == 'l' || paintColor == 'r') {
                if (y > 0 && metaData[y-1][x] == 'u') spreadPaint(y-1, x, paintColor);
                if (x < gridWidth-1 && metaData[y][x+1] == 'u') spreadPaint(y, x+1, paintColor);
                if (y < gridHeight-1 && metaData[y+1][x] == 'u') spreadPaint(y+1, x, paintColor);
                if (x > 0 && metaData[y][x-1] == 'u') spreadPaint(y, x-1, paintColor);
            }
        }
    }

    private static void spreadPaint(int y, int x, char paintColor) {
        metaData[y][x] = paintColor;
        if (y > 0 && metaData[y-1][x] == 'u') spreadPaint(y-1, x, paintColor);
        if (x < gridWidth-1 && metaData[y][x+1] == 'u') spreadPaint(y, x+1, paintColor);
        if (y < gridHeight-1 && metaData[y+1][x] == 'u') spreadPaint(y+1, x, paintColor);
        if (x > 0 && metaData[y][x-1] == 'u') spreadPaint(y, x-1, paintColor);
    }

    private static void identifyInsideTilesColor() {
        for (int y = 0; y < gridHeight; y++) for (int x = 0; x <gridWidth; x++) {
            if (metaData[y][x] == 'l' ) {
                insideTilesColor = 'r'; return;
            }
            if (metaData[y][x] == 'r' ) {
                insideTilesColor = 'l'; return;
            }
        }
    }

    private static void countInsideTiles() {
        int insideTilesCount = 0;
        for (int y = 0; y < gridHeight; y++) for (int x = 0; x <gridWidth; x++) {
            if (metaData[y][x] == insideTilesColor) insideTilesCount++;
        }
        System.out.println("\nCount of tiles inside the pipe loop:\n\n"+insideTilesCount);
    }

}
