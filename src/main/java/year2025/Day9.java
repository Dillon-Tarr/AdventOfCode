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
    static private final ArrayList<Rectangle> rectangles = new ArrayList<>();
    static private final ArrayList<VerticalEdge> verticalEdges = new ArrayList<>();
    static private final ArrayList<HorizontalEdge> horizontalEdges = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
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
        var split = inputStrings.getLast().split(",");
        int x1 = Integer.parseInt(split[0]), y1 = Integer.parseInt(split[1]), x2, y2;
        for (var s : inputStrings) {
            split = s.split(",");
            x2 = Integer.parseInt(split[0]); y2 = Integer.parseInt(split[1]);
            points.add(new Coordinates(y2, x2));
            if (x1 == x2) verticalEdges.add(new VerticalEdge(x1, new InclusiveNumberRange(y1, y2)));
            else horizontalEdges.add( new HorizontalEdge(y1, new InclusiveNumberRange(x1, x2)));
            x1 = x2; y1 = y2;
        }
    }

    private static void solvePart1() {
        long area, height, width; int smallY, bigY, smallX, bigX;
        Coordinates p1, p2;
        for (int i = 0; i < points.size()-1; i++) {
            p1 = points.get(i); int y1 = p1.y, x1 = p1.x;
            for (int j = i; j < points.size(); j++) {
                p2 = points.get(j); int y2 = p2.y, x2 = p2.x;
                if (y1 < y2) { smallY = y1; bigY = y2; } else { smallY = y2; bigY = y1; }
                if (x1 < x2) { smallX = x1; bigX = x2; } else { smallX = x2; bigX = x1; }
                height = 1+bigY-smallY;
                width = 1+bigX-smallX;
                area = height*width;
                rectangles.add(new Rectangle(smallY, bigY, smallX, bigX, area));
            }
        }
        rectangles.sort(Comparator.comparingLong(Rectangle::area).reversed());
        System.out.println("\nPart 1 - largest area: "+rectangles.getFirst().area);
    }

    private static void solvePart2() {
        long area = -1;
        int i = 0;
        mainLoop: for (var rectangle : rectangles) {
            int sX1 = rectangle.smallX+1, bX1 = rectangle.bigX-1, sY1 = rectangle.smallY+1, bY1 = rectangle.bigY-1; i++;
            for (var edge : verticalEdges)
                if (edge.x >= sX1 && edge.x <= bX1 && (edge.yRange.inRange(sY1) || edge.yRange.inRange(bY1)))
                    continue mainLoop;
            for (var edge : horizontalEdges)
                if (edge.y >= sY1 && edge.y <= bY1 && (edge.xRange.inRange(sX1) || edge.xRange.inRange(bX1)))
                    continue mainLoop;
            boolean inside = false;
            for (var edge : verticalEdges)
                if (edge.x >= 0 && edge.x < sX1 && edge.yRange.inRange(sY1))
                    inside = !inside;
            if (inside) { area = rectangle.area; break; }
        }
        System.out.println("\nPart 2 - rectangles tried: "+i+"/"+rectangles.size()+"; largest valid area: "+area);
    }

    private record Rectangle(int smallY, int bigY, int smallX, int bigX, long area) {}

    private record VerticalEdge(int x, InclusiveNumberRange yRange) {}

    private record HorizontalEdge(int y, InclusiveNumberRange xRange) {}

}
