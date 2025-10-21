package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Day18 {
    static private final int DAY = 18;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private String inputString;

    static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        countSafeTiles(args.length == 0 ? 40 : 400000); // 0 args for part 1, >0 for part 2;

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void countSafeTiles(int mapHeight) {
        int mapWidth = inputString.length();
        boolean[][] tiles = new boolean[mapHeight][mapWidth];
        boolean[] aboveRow, currentRow;
        for (int r = 0; r < tiles.length; r++) tiles[r] = new boolean[mapWidth];
        for (int c = 0; c < mapWidth; c++) if (inputString.charAt(c) == '.') tiles[0][c] = true;
        currentRow = tiles[0];
        for (int r = 1; r < mapHeight; r++) {
            aboveRow = currentRow;
            currentRow = tiles[r];
            currentRow[0] = aboveRow[1];
            for (int c = 1; c < mapWidth-1; c++) {
                if (aboveRow[c-1] == aboveRow[c]) {
                    if (aboveRow[c] == aboveRow[c+1]) currentRow[c] = true;
                } else if (aboveRow[c-1] == aboveRow[c+1]) currentRow[c] = true;
            }
            currentRow[mapWidth-1] = aboveRow[mapWidth-2];
        }
        int count = 0;
        for (int r = 0; r < mapHeight; r++) for (int c = 0; c < mapWidth; c++) if (tiles[r][c]) count++;
        System.out.println("Number of safe tiles in "+mapHeight+" rows: "+ count);
    }

}
