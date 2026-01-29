package year2017;

import shared.CardinalDirection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import static shared.CardinalDirection.*;

class Day3 {
    static private final int DAY = 3;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private int goal;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        findGoalDistance(); // part 1
        findFirstValueAboveGoalNumber(); // part 2

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            goal = Integer.parseInt(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void findGoalDistance() {
        if (goal < 1) throw new IllegalArgumentException("Illegal goal: "+goal);
        int x = 0, y = 0, n = 1, stepSize = 0;
        CardinalDirection direction = EAST;
        while (true) {
            switch (direction) { case EAST, WEST -> stepSize++; }
            switch (direction) {
                case EAST -> x+=stepSize;
                case NORTH -> y+=stepSize;
                case WEST -> x-=stepSize;
                case SOUTH -> y-=stepSize;
                default -> throw new IllegalArgumentException("Illegal direction: "+direction);
            }
            n+=stepSize;
            if (n > goal) {
                int overshotDistance = n-goal;
                switch (direction) {
                    case EAST -> x-=overshotDistance;
                    case NORTH -> y-=overshotDistance;
                    case WEST -> x+=overshotDistance;
                    case SOUTH -> y+=overshotDistance;
                }
                break;
            } else direction = direction.left();
        }
        int distance = Math.abs(x)+Math.abs(y);
        System.out.println("\nManhattan distance from square 1 to square "+goal+" (part 1 solution): "+distance);
    }

    private static void findFirstValueAboveGoalNumber() {
        if (goal < 1) throw new IllegalArgumentException("Illegal goal: "+goal);
        Integer a = null, b = null, c = null, // -1,1;  0,1;  1,1;   +^+  <---x+++>
                d = null, /* n=1 */ e = null, // -1,0;  0,0;  1,0; y: |
                f = null, g = null, h = null; // -1,-1; 0,-1; 1,-1;  -v-
        int n = 1, xM1 = -1, x = 0, xP1 = 1, yM1 = -1, y = 0, yP1 = 1,
                stepsPerDirection = 0, stepsTakenInDirection = 0;
        CardinalDirection direction = SOUTH;
        HashMap<String, Integer> coordsToVals = new HashMap<>();
        while (n <= goal) {
            coordsToVals.put(x+","+y, n);
            if (stepsTakenInDirection >= stepsPerDirection) {
                direction = direction.left();
                stepsTakenInDirection = 0;
                switch (direction) { case EAST, WEST -> stepsPerDirection++; }
            }
            stepsTakenInDirection++;
            switch (direction) {
/*  ------>  */ case EAST -> {
/*  A  B  C  */     x++; xM1++; xP1++;
/*  D  n  e  */     a = b; b = c; c = coordsToVals.get(xP1+","+yP1); d = n;
/*  f  g  h  */     if (a!=null) { n+=a; if (b!=null) { n+=b; if (c!=null) n+=c; } }
                }
/* ^ A  b  c */ case NORTH -> {
/* | D  n  e */     y++; yM1++; yP1++;
/* | F  G  h */     f = d; d = a; a = coordsToVals.get(xM1+","+yP1); g = n;
                    if (f!=null) { n+=f; if (d!=null) { n+=d; if (a!=null) n+=a; } }
                }
/*  a  b  c  */ case WEST -> {
/*  d  n  E  */     x--; xM1--; xP1--;
/*  F  G  H  */     h = g; g = f; f = coordsToVals.get(xM1+","+yM1); e = n;
/*  <------  */     if (h!=null) { n+=h; if (g!=null) { n+=g; if (f!=null) n+=f; } }
                }
/* a  B  C | */ case SOUTH -> {
/* d  n  E | */     y--; yM1--; yP1--;
/* f  g  H V */     c = e; e = h; h = coordsToVals.get(xP1+","+yM1); b = n;
                    if (c!=null) { n+=c; if (e!=null) { n+=e; if (h!=null) n+=h; } }
                }
                default -> throw new IllegalArgumentException("Illegal direction: "+direction);
            }
        }
        System.out.println("\nFirst value written greater than "+goal+": "+n);
    }

}
