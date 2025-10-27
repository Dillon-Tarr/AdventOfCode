package year2016.day22;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part2 {
    static private final int DAY = 22;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final int gridHeight = 25, gridWidth = 37;
    static private final Node[][] nodes = new Node[gridHeight][gridWidth];

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        instantiateNodes();
        printNodesForHandSolve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            br.readLine(); br.readLine(); // Skip first 2 lines
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void instantiateNodes() {
        int used, avail, preSpace, x = 0, y = 0;
        for (String s : inputStrings) {
            if (y >= gridHeight) {
                if (++x >= gridWidth) break;
                y = 0;
            }
            preSpace = s.lastIndexOf(' ', 32);
            used = Integer.parseInt(s.substring(preSpace+1, 33));
            preSpace = s.lastIndexOf(' ', 39);
            avail = Integer.parseInt(s.substring(preSpace+1, 40));
            nodes[y][x] = new Node(used, avail);
            y++;
        }
    }

    private static void printNodesForHandSolve() {
        Node currentNode, topRight = nodes[0][gridWidth-1];
        System.out.print("\nNodes: (^-immovable; .-normal; 0-empty; G-goalData; x-destination)\nx");
        for (int x = 1; x < gridWidth-1; x++) {
            currentNode = nodes[0][x];
            if (currentNode.used == 0) System.out.print('0');
            else if (currentNode.size > 400) System.out.print('^');
            else System.out.print(currentNode.size < topRight.used ? '+' : '.');
        }
        System.out.println("G");
        for (int y = 1; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                currentNode = nodes[y][x];
                if (currentNode.used == 0) System.out.print('0');
                else if (currentNode.size > 400) System.out.print('^');
                else System.out.print(currentNode.size < topRight.used ? '+' : '.');
            }
            System.out.println();
        }
        // Hand solve results (correctly assuming that there wasn't a rogue node that just was too small):
        // 52 steps to move G left once
        // + 5 per further left move of G to get it to 0,0
        // (5*35 = 175)+52 = 227
        // Mr. Topaz himself indicated print and hand-solve was intended... Good to remember for future puzzles.
    }

    private static class Node {
        int used, avail, size;

        Node(int used, int avail) {
            this.used = used;
            this.avail = avail;
            size = used + avail;
        }
    }

}
