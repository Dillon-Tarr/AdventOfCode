package day10.puzzle2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WouldNeedALotMoreWorkToWork { // Does not work. I would need to send snakes from
    // each tile to check all possible directions to find the edge of the grid outside the pipe.
    static private final String INPUT_FILE_PATH = "input-files/day10input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    static private char[][] grid;
    static private char[][] metaData; // x: irrelevant, p: part of loop pipe, other values: direction to check from for the purpose of evaluating whether in the loop (up, right, down, left)
    static private int gridHeight;
    static private int gridWidth;
    static private int startY; // Coordinates referenced in y-x order since values are always obtained in line-character order.
    static private int startX;
    static private char startExitStepDirection;
    static private char startEntryStepDirection;

    public static void main(String[] args) {
        getInputDataAndStartCoordinates();
        setClosestBoundaryDirectionValuesForCheckingIfInsideLoop();
        getLoopTiles();
        convertStartTileToProperCharacter();
        countTilesInsideTheLoop();
    }

    private static void getInputDataAndStartCoordinates() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            ArrayList<char[]> inputLines = new ArrayList<>();
            while (currentLine != null) {
                inputLines.add(currentLine.toCharArray());
                currentLine = br.readLine();
            }
            gridWidth = inputLines.get(0).length;
            gridHeight = inputLines.size();
            grid = new char[gridHeight][];
            metaData = new char[gridHeight][];
            for (int y = 0; y < gridHeight; y++) {
                grid[y] = new char[gridWidth];
                metaData[y] = new char[gridWidth];
                for (int x = 0; x < gridWidth; x++) {
                    char value = inputLines.get(y)[x];
                    grid[y][x] = value;
                    if (value == 'S') {startY = y; startX = x;}
                }
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void setClosestBoundaryDirectionValuesForCheckingIfInsideLoop() {
        for (int x = 0; x < gridWidth; x++) {
            metaData[0][x] = 'x';
            metaData[gridHeight-1][x] = 'x';
        }
        for (int y = 1; y < gridHeight-1; y++) {
            metaData[y][0] = 'x';
            metaData[y][gridWidth-1] = 'x';
        }
        for (int y = 1; y < gridHeight-1; y++) for (int x = 1; x<gridWidth-1; x++){
            char closestBoundaryForTile = 'u'; int closestBoundaryDistance = y;
            if (gridWidth-x <= closestBoundaryDistance) {
                closestBoundaryForTile = 'r'; closestBoundaryDistance = gridWidth-x;}
            if (x < closestBoundaryDistance) {
                closestBoundaryForTile = 'l'; closestBoundaryDistance = x;}
            if (gridHeight-y <= closestBoundaryDistance) {
                closestBoundaryForTile = 'd';}
            metaData[y][x] = closestBoundaryForTile;
        }
    }

    private static void getLoopTiles() {
        char currentCharacter;
        int y = startY;
        int x = startX;
        char lastStepDirection;

        if (grid[y -1][x] == '7' || grid[y -1][x] == '|' || grid[y -1][x] == 'F') {
            lastStepDirection = 'u'; y--;
        } else if (grid[y][x +1] == 'J' || grid[y][x +1] == '-' || grid[y][x +1] == '7') {
            lastStepDirection = 'r'; x++;
        } else if (grid[y +1][x] == 'L' || grid[y +1][x] == '|' || grid[y +1][x] == 'J') {
            lastStepDirection = 'd'; y++;
        } else {
            lastStepDirection = 'l'; x--;
        }
        currentCharacter = grid[y][x];

        metaData[y][x] = 'p';
        startExitStepDirection = lastStepDirection;
        do {
            switch (currentCharacter) {
                case '7' -> {
                    if (lastStepDirection == 'u') {lastStepDirection = 'l'; x--;}
                    else {lastStepDirection = 'd'; y++;}}
                case 'F' -> {
                    if (lastStepDirection == 'u') {lastStepDirection = 'r'; x++;}
                    else {lastStepDirection = 'd'; y++;}}
                case 'J' -> {
                    if (lastStepDirection == 'd') {lastStepDirection = 'l'; x--;}
                    else {lastStepDirection = 'u'; y--;}}
                case 'L' -> {
                    if (lastStepDirection == 'd') {lastStepDirection = 'r'; x++;}
                    else {lastStepDirection = 'u'; y--;}}
                case '|' -> {
                    if (lastStepDirection == 'u') y--;
                    else y++;}
                case '-' -> {
                    if (lastStepDirection == 'r') x++;
                    else x--;}
            }
            currentCharacter = grid[y][x];
            metaData[y][x] = 'p';
        } while (y != startY || x != startX);
        startEntryStepDirection = lastStepDirection;
    }

    private static void convertStartTileToProperCharacter() {
        switch (startEntryStepDirection) {
            case 'u' -> {
                if (startExitStepDirection == 'u') grid[startY][startX] = '|';
                else if (startExitStepDirection == 'l') grid[startY][startX] = '7';
                else grid[startY][startX] = 'F';}
            case 'r' -> {
                if (startExitStepDirection == 'r') grid[startY][startX] = '-';
                else if (startExitStepDirection == 'u') grid[startY][startX] = 'J';
                else grid[startY][startX] = '7';}
            case 'd' -> {
                if (startExitStepDirection == 'd') grid[startY][startX] = '|';
                else if (startExitStepDirection == 'r') grid[startY][startX] = 'L';
                else grid[startY][startX] = 'J';}
            case 'l' -> {
                if (startExitStepDirection == 'l') grid[startY][startX] = '-';
                else if (startExitStepDirection == 'd') grid[startY][startX] = 'F';
                else grid[startY][startX] = 'L';}
        }
    }

    private static void countTilesInsideTheLoop() {
        int tilesInsideLoopCount = 0;
        for (int y = 1; y < gridHeight-1; y++) for (int x = 1; x < gridWidth-1; x++) {
            if (trueIfThisTileIsInsideTheLoop(y, x)) tilesInsideLoopCount++;
        }
        System.out.println("\nNumber of tiles inside the loop:\n\n"+tilesInsideLoopCount);
    }

    private static boolean trueIfThisTileIsInsideTheLoop(int y, int x) {
        if (metaData[y][x] == 'x' || metaData[y][x] == 'p') return false;
        int loopCrossingCount = 0;
        boolean currentCharacterIsPartOfLoopPipe;
        char currentCharacter;

        switch (metaData[y][x]) {
            case 'u' -> {
                for (int checkY = y-1; checkY >= 0; checkY--) {
                    currentCharacterIsPartOfLoopPipe = metaData[checkY][x] == 'p';
                    currentCharacter = grid[checkY][x];
                    if (currentCharacterIsPartOfLoopPipe &&
                            (currentCharacter == '-' || currentCharacter == 'J' || currentCharacter == 'L'))
                        loopCrossingCount++;
                }
            }
            case 'r' -> {
                for (int checkX = x+1; checkX < gridWidth; checkX++) {
                    currentCharacterIsPartOfLoopPipe = metaData[y][checkX] == 'p';
                    currentCharacter = grid[y][checkX];
                    if (currentCharacterIsPartOfLoopPipe &&
                            (currentCharacter == '|' || currentCharacter == 'L' || currentCharacter == 'F'))
                        loopCrossingCount++;
                }
            }
            case 'd' -> {
                for (int checkY = y+1; checkY < gridHeight; checkY++) {
                    currentCharacterIsPartOfLoopPipe = metaData[checkY][x] == 'p';
                    currentCharacter = grid[checkY][x];
                    if (currentCharacterIsPartOfLoopPipe &&
                            (currentCharacter == '-' || currentCharacter == 'F' || currentCharacter == '7'))
                        loopCrossingCount++;
                }
            }
            case 'l' -> {
                for (int checkX = x-1; checkX >= 0; checkX--) {
                    currentCharacterIsPartOfLoopPipe = metaData[y][checkX] == 'p';
                    currentCharacter = grid[y][checkX];
                    if (currentCharacterIsPartOfLoopPipe &&
                            (currentCharacter == '|' || currentCharacter == '7' || currentCharacter == 'J'))
                        loopCrossingCount++;
                }
            }
        }

        boolean isInsideLoop = loopCrossingCount % 2 == 1;
        if (isInsideLoop) System.out.println("y: "+y+", x: "+x);
        return isInsideLoop;
    }

}