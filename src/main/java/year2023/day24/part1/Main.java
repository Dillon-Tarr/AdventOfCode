package year2023.day24.part1;

import java.io.*;
import java.util.ArrayList;

class Main {
    static private final int DAY = 24;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private final ArrayList<Hailstone> hailstones = new ArrayList<>();


    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        checkForCollisions();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void processInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line = br.readLine();
            String[] splitLine;
            long[] longs = new long[6];
            while (line != null) {
                splitLine = line.split("[,@]");
                for (int i = 0; i < splitLine.length; i++) longs[i] = Long.parseLong(splitLine[i].trim());
                hailstones.add(new Hailstone(longs[0], longs[1], longs[3], longs[4]));
                line = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void checkForCollisions() {
        int count = 0;
        long xAndYAreaLow = 200000000000000L, xAndYAreaHigh = 400000000000000L;
        Hailstone a, b;
        float intersectionX, intersectionY;
        for (int i = 0; i < hailstones.size() - 1; i++)
            for (int j = i + 1; j < hailstones.size(); j++) {
                a = hailstones.get(i);
                b = hailstones.get(j);
                if (a.slope == b.slope) continue;
                intersectionX = (b.yIntercept - a.yIntercept) / (a.slope - b.slope);
                if (intersectionX < xAndYAreaLow || intersectionX > xAndYAreaHigh) continue;
                intersectionY = a.yIntercept + (a.slope * intersectionX);
                if (intersectionY >= xAndYAreaLow && intersectionY <= xAndYAreaHigh
                        && a.hasPositiveXVelocity == intersectionX > a.xPosition && a.hasPositiveYVelocity == intersectionY > a.yPosition
                        && b.hasPositiveXVelocity == intersectionX > b.xPosition && b.hasPositiveYVelocity == intersectionY > b.yPosition)
                    count++;
            }
        System.out.println("\nCollision count: " + count);
    }

}
