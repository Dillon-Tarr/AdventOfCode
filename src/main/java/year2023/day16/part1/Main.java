package year2023.day16.part1;

import java.io.*;

public class Main {
    static private final int DAY = 16;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private Tile[][] grid;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        sendBeam('r', 0, 0);
        countEnergizedTiles();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(INPUT_FILE))) {
            lnr.skip(Long.MAX_VALUE);
            int rowCount = lnr.getLineNumber();
            grid = new Tile[rowCount][];
        } catch (IOException e) {throw new RuntimeException();}
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            for (int y = 0; y < grid.length; y++) {
                grid[y] = new Tile[grid.length];
                String line = br.readLine();
                for (int x = 0; x < grid[0].length; x++) {
                    grid[y][x] = new Tile(line.charAt(x));
                }
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void sendBeam(char direction, int nextY, int nextX) {
        while (nextY >= 0 && nextY < grid.length && nextX >= 0 && nextX < grid[0].length && grid[nextY][nextX].firstPassInThisDirection(direction)) {
            char nextTileCharacter = grid[nextY][nextX].character;
            switch (nextTileCharacter) {
                case '.' -> {
                    switch (direction) {
                        case 'u' -> nextY--;
                        case 'd' -> nextY++;
                        case 'l' -> nextX--;
                        case 'r' -> nextX++;
                    }
                }
                case '-' -> {
                    switch (direction) {
                        case 'u', 'd' -> {
                            sendBeam('r', nextY, nextX+1);
                            direction = 'l'; nextX--;
                        }
                        case 'l' -> nextX--;
                        case 'r' -> nextX++;
                    }
                }
                case '|' -> {
                    switch (direction) {
                        case 'u' -> nextY--;
                        case 'd' -> nextY++;
                        case 'l', 'r' -> {
                            sendBeam('d', nextY+1, nextX);
                            direction = 'u'; nextY--;
                        }
                    }
                }
                case '\\' -> {
                    switch (direction) {
                        case 'u' -> {direction = 'l'; nextX--;}
                        case 'd' -> {direction = 'r'; nextX++;}
                        case 'l' -> {direction = 'u'; nextY--;}
                        case 'r' -> {direction = 'd'; nextY++;}
                    }
                }
                case '/' -> {
                    switch (direction) {
                        case 'u' -> {direction = 'r'; nextX++;}
                        case 'd' -> {direction = 'l'; nextX--;}
                        case 'l' -> {direction = 'd'; nextY++;}
                        case 'r' -> {direction = 'u'; nextY--;}
                    }
                }
            }
        }
    }

    private static void countEnergizedTiles() {
        int count = 0;
        for (int y = 0; y < grid.length; y++) for (int x = 0; x < grid[0].length; x++) if (grid[y][x].isEnergized) count++;
        System.out.println("\nEnergized tile count:\n\n"+count);
    }

    private static class Tile {
        char character;
        boolean isEnergized;
        boolean hasBeenApproachedWithUpwardMotion;
        boolean hasBeenApproachedWithDownwardMotion;
        boolean hasBeenApproachedWithLeftwardMotion;
        boolean hasBeenApproachedWithRightwardMotion;

        private Tile (char character) {
            this.character = character;
        }

        private boolean firstPassInThisDirection(char directionOfMotion) {
            isEnergized = true;
            switch (directionOfMotion) {
                case 'u' -> {if (hasBeenApproachedWithUpwardMotion) return false;
                    hasBeenApproachedWithUpwardMotion = true; return true;}
                case 'd' -> {
                    if (hasBeenApproachedWithDownwardMotion) return false;
                    hasBeenApproachedWithDownwardMotion = true; return true;}
                case 'l' -> {
                    if (hasBeenApproachedWithLeftwardMotion) return false;
                    hasBeenApproachedWithLeftwardMotion = true; return true;}
                case 'r' -> {
                    if (hasBeenApproachedWithRightwardMotion) return false;
                    hasBeenApproachedWithRightwardMotion = true; return true;}
            }
            return false;
        }
    }

}
