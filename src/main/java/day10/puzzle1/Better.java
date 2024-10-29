package day10.puzzle1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Better {
    static private final String INPUT_FILE_PATH = "input-files/day10input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    static private char[][] grid;
    static private int startY; // Coordinates referenced in y-x order since values are always obtained in line-character order.
    static private int startX;

    public static void main(String[] args) {
        getInputDataAndStartCoordinates();
        evaluateLoopLength();
    }

    private static void getInputDataAndStartCoordinates() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            ArrayList<char[]> inputLines = new ArrayList<>();
            while (currentLine != null) {
                inputLines.add(currentLine.toCharArray());
                currentLine = br.readLine();
            }
            int gridWidth = inputLines.get(0).length;
            int gridHeight = inputLines.size();
            grid = new char[gridHeight][];
            for (int y = 0; y < gridHeight; y++) {
                grid[y] = new char[gridWidth];
                for (int x = 0; x < gridWidth; x++) {
                    char value = inputLines.get(y)[x];
                    grid[y][x] = value;
                    if (value == 'S') {startY = y; startX = x;}
                }
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void evaluateLoopLength() {
        char currentCharacter;
        int y = startY;
        int x = startX;
        char lastStepDirection;

        if (grid[y -1][x] == '7' || grid[y -1][x] == '|' || grid[y -1][x] == 'F') {
            lastStepDirection = 'u'; y--; currentCharacter = grid[y][x];
        } else if (grid[y][x +1] == 'J' || grid[y][x +1] == '-' || grid[y][x +1] == '7') {
            lastStepDirection = 'r'; x++; currentCharacter = grid[y][x];
        } else if (grid[y +1][x] == 'L' || grid[y +1][x] == '|' || grid[y +1][x] == 'J') {
            lastStepDirection = 'd'; y++; currentCharacter = grid[y][x];
        } else {
            lastStepDirection = 'l'; x--; currentCharacter = grid[y][x];
        }

        int stepsTaken = 1;
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
            stepsTaken++;
        } while (y != startY || x != startX);
        int loopLength = stepsTaken;
        System.out.println("\nSteps needed to get to the furthest point in the loop:\n\n"+(loopLength/2));
    }

}