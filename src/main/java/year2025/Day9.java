package year2025;

import shared.Coordinates;
import shared.InclusiveNumberRange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

class Day9 {
    static private final int DAY = 9;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final ArrayList<Coordinates> points = new ArrayList<>();
    static private int highY = Integer.MIN_VALUE, lowY = Integer.MAX_VALUE, highX = Integer.MIN_VALUE, lowX = Integer.MAX_VALUE;
    static private final ArrayList<VerticalEdge> verticalEdges = new ArrayList<>();
    static private final ArrayList<HorizontalEdge> HorizontalEdges = new ArrayList<>();
    static private final ArrayList<Rectangle> rectangles = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
        getEdges();
        solvePart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        var inputStrings = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (var s : inputStrings) {
            var split = s.split(",");
            points.add(new Coordinates(Integer.parseInt(split[1]), Integer.parseInt(split[0])));
        }
    }

    private static void solvePart1() {
        long area, height, width; int smallY, bigY, smallX, bigX;
        Coordinates p1, p2;
        for (int i = 0; i < points.size()-1; i++) {
            p1 = points.get(i); int y = p1.y, x = p1.x;
            if (y < lowY) lowY = y; if (y > highY) highY = y;
            if (x < lowX) lowX = x; if (x > highX) highX = x;
            for (int j = i; j < points.size(); j++) {
                p2 = points.get(j);
                if (p1.y < p2.y) { smallY = p1.y; bigY = p2.y; } else { smallY = p2.y; bigY = p1.y; }
                if (p1.x < p2.x) { smallX = p1.x; bigX = p2.x; } else { smallX = p2.x; bigX = p1.x; }
                height = 1+bigY-smallY;
                width = 1+bigX-smallX;
                area = height*width;
                rectangles.add(new Rectangle(p1, p2, smallY, bigY, smallX, bigX, area));
            }
        }
        rectangles.sort(Comparator.comparingLong(Rectangle::area).reversed());
        System.out.println("\nPart 1 largest area: "+rectangles.getFirst().area);
    }

    private static void getEdges() {}

    private static void solvePart2() {}

    private record Rectangle(Coordinates p1, Coordinates p2, int smallY, int bigY, int smallX, int bigX, long area) {}

    private record VerticalEdge(int x, InclusiveNumberRange YRange) {}

    private record HorizontalEdge(int y, InclusiveNumberRange xRange) {}

}
