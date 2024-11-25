package day18.puzzle1;

import shared.Coordinates;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    static private final File INPUT_FILE = new File("input-files/day18input.txt");
    static private Instruction[] instructions;
    static private final ArrayList<Square> lagoonBoundarySquares = new ArrayList<>();
    static private Square[][] map;
    static private int mapLength;
    static private int mapWidth;
    static private final Queue<Coordinates> digQueue = new LinkedList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        executeInstructions();
        drawMapWithLagoonBoundaries();
        digOutRestOfLagoon();
        countDugOutSquares();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void countDugOutSquares() {
        int count = 0;
        for (int y = 0; y < mapLength; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (map[y][x].hasBeenDugOut) count++;
            }
        }
        System.out.println("Number of dug out squares: "+count);
    }

    private static void getInputData() {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(INPUT_FILE))) {
            lnr.skip(Long.MAX_VALUE);
            int rowCount = lnr.getLineNumber();
            instructions = new Instruction[rowCount];
        } catch (IOException e) {throw new RuntimeException();}
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            for (int i = 0; i < instructions.length; i++) instructions[i] = new Instruction(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void executeInstructions() {
        int y = 0;
        int x = 0;
        for (Instruction instruction : instructions) switch (instruction.direction) {
            case NORTH -> {for (int i = 0; i < instruction.steps; i++) digAndAddLagoonBoundarySquare(--y, x);}
            case SOUTH -> {for (int i = 0; i < instruction.steps; i++) digAndAddLagoonBoundarySquare(++y, x);}
            case WEST ->  {for (int i = 0; i < instruction.steps; i++) digAndAddLagoonBoundarySquare(y, --x);}
            case EAST ->  {for (int i = 0; i < instruction.steps; i++) digAndAddLagoonBoundarySquare(y, ++x);}
        }
    }

    private static void digAndAddLagoonBoundarySquare(int y, int x) {
        Square square = new Square(y, x);
        square.dig();
        lagoonBoundarySquares.add(square);
    }

    private static void drawMapWithLagoonBoundaries() {
        int highY = Integer.MIN_VALUE, lowY = Integer.MAX_VALUE, highX = Integer.MIN_VALUE, lowX = Integer.MAX_VALUE;
        for (Square square : lagoonBoundarySquares) {
            int y = square.coordinates.y; if (y > highY) highY = y; if (y < lowY) lowY = y;
            int x = square.coordinates.x; if (x > highX) highX = x; if (x < lowX) lowX = x;
        }
        mapLength = 1+highY-lowY;
        mapWidth = 1+highX-lowX;
        map = new Square[mapLength][mapWidth];
        if (lowY < 0) for (Square square : lagoonBoundarySquares) square.coordinates.y -= lowY;
        if (lowX < 0) for (Square square : lagoonBoundarySquares) square.coordinates.x -= lowX;
        for (Square square : lagoonBoundarySquares) {
            int y = square.coordinates.y;
            int x = square.coordinates.x;
            map[y][x] = square;
        }
        for (int y = 0; y < mapLength; y++) for (int x = 0; x < mapWidth; x++)
            if (map[y][x] == null) map[y][x] = new Square(y, x);
    }

    private static void digOutRestOfLagoon() {
        digAround(findAnInteriorSquare());
        while (!digQueue.isEmpty()) {
            digAround(digQueue.poll());
        }
    }

    private static Coordinates findAnInteriorSquare() {
        for (int y = 1; y < mapLength-1; y++) for (int x = 1; x < mapWidth-1; x++) {
            if (map[y][x].hasBeenDugOut) continue;
            int boundariesFound = 0;

            int n = y;
            while (n > 0) if (map[--n][x].hasBeenDugOut) {boundariesFound++; break;}
            if (boundariesFound != 1) continue;

            int s = y;
            while (s < mapLength-1) if (map[++s][x].hasBeenDugOut) {boundariesFound++; break;}
            if (boundariesFound != 2) continue;

            int w = x;
            while (w > 0) if (map[y][--w].hasBeenDugOut) {boundariesFound++; break;}
            if (boundariesFound != 3) continue;

            int e = x;
            while (e < mapWidth-1) if (map[y][++e].hasBeenDugOut) {boundariesFound++; break;}
            if (boundariesFound != 4) continue;

            return new Coordinates(y, x);
        }
        return new Coordinates(-1, -1);
    }

    private static void digAround(Coordinates coordinates) {
        int y = coordinates.y, x = coordinates.x;
        if (y < 0 || x < 0 || y >= mapLength || x >= mapWidth) throw new IllegalStateException("Unexpected y or x value.");

        if (y > 0 && !map[y-1][x].hasBeenDugOut) {
            map[y-1][x].dig(); digQueue.offer(new Coordinates(y-1, x));
        }
        if (y < mapLength-1 && !map[y+1][x].hasBeenDugOut) {
            map[y+1][x].dig(); digQueue.offer(new Coordinates(y+1, x));
        }
        if (x > 0 && !map[y][x-1].hasBeenDugOut) {
            map[y][x-1].dig(); digQueue.offer(new Coordinates(y, x-1));
        }
        if (x < mapWidth-1 && !map[y][x+1].hasBeenDugOut) {
            map[y][x+1].dig(); digQueue.offer(new Coordinates(y, x+1));
        }
    }

}
