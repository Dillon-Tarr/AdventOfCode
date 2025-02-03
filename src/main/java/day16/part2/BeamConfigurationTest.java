package day16.part2;

class BeamConfigurationTest {
    private final char startDirection;
    private final int startY;
    private final int startX;
    private final Tile[][] instancedGrid;

    BeamConfigurationTest (char startDirection, int startY, int startX, char[][] grid) {
        this.startDirection = startDirection;
        this.startY = startY;
        this.startX = startX;
        instancedGrid = new Tile[grid.length][];
        for (int y = 0; y < grid.length; y++) {instancedGrid[y] = new Tile[grid[0].length];
            for (int x = 0; x < grid[0].length; x++) {
                instancedGrid[y][x] = new Tile(grid[y][x]);
            }
        }
    }

    int run() {
        sendBeam(startDirection, startY, startX);
        return countEnergizedTiles();
    }

    private void sendBeam(char direction, int nextY, int nextX) {
        while (nextY >= 0 && nextY < instancedGrid.length && nextX >= 0 && nextX < instancedGrid[0].length && instancedGrid[nextY][nextX].firstPassInThisDirection(direction)) {
            char nextTileCharacter = instancedGrid[nextY][nextX].character;
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

    private int countEnergizedTiles() {
        int count = 0;
        for (int y = 0; y < instancedGrid.length; y++) for (int x = 0; x < instancedGrid[0].length; x++) if (instancedGrid[y][x].isEnergized) count++;
        return count;
    }

}
