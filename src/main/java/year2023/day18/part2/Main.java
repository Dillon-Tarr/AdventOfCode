package year2023.day18.part2;

import shared.LongCoordinates;

import java.io.*;
import java.util.ArrayList;

public class Main {
    static private final int DAY = 18;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private Instruction[] instructions;
    static private long perimeterSquareCount = 0;
    static private final ArrayList<LongCoordinates> vertices = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        getVertices();
        countDugSquares();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
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

    private static void getVertices() {
        long y = 0;
        long x = 0;
        for (Instruction instruction : instructions) {
            perimeterSquareCount += instruction.steps;
            switch (instruction.direction) {
                case NORTH -> y -= instruction.steps;
                case SOUTH -> y += instruction.steps;
                case WEST ->  x -= instruction.steps;
                case EAST ->  x += instruction.steps;
            }
            vertices.add(new LongCoordinates(y, x));
        }
    }

    private static void countDugSquares() {
        long basicArea = getBasicAreaWithShoelaceAlgorithm();
        System.out.println("Basic area: "+basicArea);
        long internalAreaViaPicksTheorem = 1 - perimeterSquareCount/2 + basicArea;
        long numberOfDugSquares = internalAreaViaPicksTheorem + perimeterSquareCount;

        System.out.println("Internal area: "+ internalAreaViaPicksTheorem);
        System.out.println("Perimeter square count: "+perimeterSquareCount);
        System.out.println("\nDug square count: "+(numberOfDugSquares));
    }

    private static long getBasicAreaWithShoelaceAlgorithm() {
        long basicArea = 0;
        LongCoordinates firstVertex = vertices.get(0);
        LongCoordinates vertex = firstVertex;
        LongCoordinates previousVertex;
        for (int i = 1; i < vertices.size(); i++) {
            previousVertex = vertex;
            vertex = vertices.get(i);
            basicArea += vertex.y*previousVertex.x - vertex.x*previousVertex.y;
        }
        LongCoordinates finalVertex = vertex;
        basicArea += firstVertex.y*finalVertex.x - firstVertex.x*finalVertex.y;
        basicArea = Math.abs((basicArea)/2);
        return basicArea;
    }

}
