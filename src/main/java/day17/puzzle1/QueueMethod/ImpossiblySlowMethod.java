package day17.puzzle1.QueueMethod;

import java.io.*;
import java.util.ArrayList;

public class ImpossiblySlowMethod {
    static private final File INPUT_FILE = new File("input-files/day17input.txt");
    static private Integer[][] grid;
    static private int heatLostOnBestPath = Integer.MAX_VALUE;

    public static void main(String[] args) {
        getInputData();
        tryPaths(0, 1, 0, new ArrayList<>());
        tryPaths(1, 0, 0, new ArrayList<>());
        System.out.println("\nHeat lost on best path:\n\n"+heatLostOnBestPath);
    }

    private static void getInputData() {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(INPUT_FILE))) {
            lnr.skip(Long.MAX_VALUE);
            int rowCount = lnr.getLineNumber();
            grid = new Integer[rowCount][];
        } catch (IOException e) {throw new RuntimeException();}
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            for (int y = 0; y < grid.length; y++) {
                String line = br.readLine();
                grid[y] = new Integer[line.length()];
                for (int x = 0; x < line.length(); x++) grid[y][x] = Character.getNumericValue(line.charAt(x));
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void tryPaths(int y, int x, int heatLost, ArrayList<String> pastPastBlocks) {
        heatLost += grid[y][x];
        if (y == grid.length-1 && x == grid[0].length-1) {
            if (heatLost < heatLostOnBestPath) heatLostOnBestPath = heatLost;
            return;
        }
        String currentBlock = y+","+x;
        for (String pastPastBlock : pastPastBlocks) if (currentBlock.equals(pastPastBlock)) return;

        char aboutFaceBlockedDirection = 'x';
        String[] lastBlockYAndX = new String[0];
        if (!pastPastBlocks.isEmpty()) {
        lastBlockYAndX = pastPastBlocks.get(pastPastBlocks.size()-1).split(",");
        if (lastBlockYAndX[1].equals(""+(x-1))) aboutFaceBlockedDirection = 'w';
        else if (lastBlockYAndX[1].equals(""+(x+1))) aboutFaceBlockedDirection = 'e';
        else if (lastBlockYAndX[0].equals(""+(y-1))) aboutFaceBlockedDirection = 'n';
        else aboutFaceBlockedDirection = 's';
        }

        char threeStepBlockedDirection = 'x';
        if (pastPastBlocks.size() >= 3) {
            String[] secondToLastBlockYAndX = pastPastBlocks.get(pastPastBlocks.size()-2).split(",");
            String[] thirdToLastBlockYAndX = pastPastBlocks.get(pastPastBlocks.size()-3).split(",");

            if (lastBlockYAndX[0].equals(""+y) &&
                    secondToLastBlockYAndX[0].equals(lastBlockYAndX[0]) &&
                    thirdToLastBlockYAndX[0].equals(secondToLastBlockYAndX[0])) {
                if (lastBlockYAndX[1].equals(""+(x-1))) {
                    threeStepBlockedDirection = 'e';
                } else threeStepBlockedDirection = 'w';
            }
            if (lastBlockYAndX[1].equals(""+x) &&
                    secondToLastBlockYAndX[1].equals(lastBlockYAndX[1]) &&
                    thirdToLastBlockYAndX[1].equals(secondToLastBlockYAndX[1])) {
                if (lastBlockYAndX[0].equals(""+(y-1))) {
                    threeStepBlockedDirection = 's';
                } else threeStepBlockedDirection = 'n';
            }
        }

        ArrayList<String> pastBlocks = new ArrayList<>(pastPastBlocks);
        pastBlocks.add(currentBlock);

        if (aboutFaceBlockedDirection != 'e' && threeStepBlockedDirection != 'e' && x < grid[0].length-1) tryPaths(y, x+1, heatLost, pastBlocks);
        if (aboutFaceBlockedDirection != 'w' && threeStepBlockedDirection != 'w' && x > 0) tryPaths(y, x-1, heatLost, pastBlocks);
        if (aboutFaceBlockedDirection != 's' && threeStepBlockedDirection != 's' && y < grid.length-1) tryPaths(y+1, x, heatLost, pastBlocks);
        if (aboutFaceBlockedDirection != 'n' && threeStepBlockedDirection != 'n' && y > 0) tryPaths(y-1, x, heatLost, pastBlocks);
    }

}
