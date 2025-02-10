package day22.part1;

import java.util.ArrayList;
import java.util.HashSet;

class Brick implements Comparable<Brick> {
    int lowX, highX, lowY, highY, lowZ, highZ;
    ArrayList<XyzCoordinates> occupiedCoordinates = new ArrayList<>();
    HashSet<Brick> overBricks = new HashSet<>();
    HashSet<Brick> underBricks = new HashSet<>();

    Brick(String inputString) {
        String[] strings = inputString.split("[,~]");
        int x1 = Integer.parseInt(strings[0]);
        int y1 = Integer.parseInt(strings[1]);
        int z1 = Integer.parseInt(strings[2]);
        int x2 = Integer.parseInt(strings[3]);
        int y2 = Integer.parseInt(strings[4]);
        int z2 = Integer.parseInt(strings[5]);
        boolean x1IsLow = x1 <= x2;
        boolean y1IsLow = y1 <= y2;
        boolean z1IsLow = z1 <= z2;
        lowX = x1IsLow ? x1 : x2;
        lowY = y1IsLow ? y1 : y2;
        lowZ = z1IsLow ? z1 : z2;
        highX = x1IsLow ? x2 : x1;
        highY = y1IsLow ? y2 : y1;
        highZ = z1IsLow ? z2 : z1;

        for (int x = lowX; x <= highX; x++) {
            for (int y = lowY; y <= highY; y++) {
                for (int z = lowZ; z <= highZ; z++) {
                    occupiedCoordinates.add(new XyzCoordinates(x, y, z));
                }
            }
        }

        // Code related to confirming things about the input to satisfy my curiosity:
        // As it turns out, checking which x, y, and z values are lower/higher is unnecessary*, because the first
        // value to appear is always the lower value, if the values are not the same. Also, the bricks are
        // always sticks or cubes (i.e. only ever one dimension of a brick may occupy more than 1 unit).
        // *But I had to check these things at least once to be sure! Now I can approach the problem properly.
        int diffCounter = 0;
        if (x1 != x2) diffCounter++;
        if (y1 != y2) diffCounter++;
        if (z1 != z2) diffCounter++;
        if (diffCounter != 1) System.out.println("Brick with input string "+inputString+" has "+diffCounter+" difference(s).");
        // DiffCounter is always 0 or 1 (not 2 or 3), which surprised me.

        if (x2 < x1) System.out.println("For brick with input string "+inputString+": x2 < x1"); // Never true for actual bricks.
        if (y2 < y1) System.out.println("For brick with input string "+inputString+": y2 < y1"); // Never true for actual bricks.
        if (z2 < z1) System.out.println("For brick with input string "+inputString+": z2 < z1"); // Never true for actual bricks.
    }

    void dropLowAndHighZ() {
        lowZ--;
        highZ--;
    }

    @Override
    public String toString() {
        return "{x:"+lowX+"-"+highX+"; y:"+lowY+"-"+highY+"; z:"+lowZ+"-"+highZ+"}";
    }

    @Override
    public int compareTo(Brick otherBrick) {
        return Integer.compare(lowZ, otherBrick.lowZ);
    }
}
