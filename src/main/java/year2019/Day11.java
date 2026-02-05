package year2019;

import shared.Coordinates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static shared.CardinalDirection.*;

class Day11 {
    static private final int DAY = 11;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private long[] origVals;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var ss = br.readLine().split(","); origVals = new long[ss.length];
            for (int i = 0; i < ss.length; i++) origVals[i] = Long.parseLong(ss[i]);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solvePart1() {
        var vals = new ExtendedLongArray(Arrays.copyOf(origVals, origVals.length));
        boolean paintMode = true; int x = 0, y = 0; String posString = "0,0";
        var direction = NORTH;
        HashSet<String> paintedAtLeastOnce = new HashSet<>(), currentlyWhite = new HashSet<>();
        long pos = 0, relativeBase = 0, inputValue = 0, outputValue;
        while (true) {
            var s = Long.toString(vals.get(pos++));
            int sLength = s.length();
            if (sLength < 5) {
                switch (sLength) {
                    case 1 -> s = "0000"+s;
                    case 2 -> s = "000"+s;
                    case 3 -> s = "00"+s;
                    case 4 -> s = "0"+s;
                }
                sLength = 5;
            }
            char lastChar = s.charAt(s.length()-1);
            if (lastChar == '9' && s.charAt(sLength-2) == '9') break;
            char p1Char = s.charAt(sLength-3);
            long p1Pos = switch (p1Char) {
                case '0' -> vals.get(pos++);
                case '1' -> pos++;
                default -> relativeBase+vals.get(pos++);
            };
            switch (lastChar) {
                case '3' -> vals.set(p1Pos, inputValue);
                case '4' -> {
                    outputValue = vals.get(p1Pos);
                    if (paintMode) {
                        if (outputValue == 0) currentlyWhite.remove(posString);
                        else { paintedAtLeastOnce.add(posString); currentlyWhite.add(posString); }
                    } else {
                        direction = outputValue == 0 ? direction.left() : direction.right();
                        switch (direction) { case NORTH -> y--; case SOUTH -> y++; case EAST -> x++; case WEST -> x--; }
                        posString = x+","+y;
                        inputValue = currentlyWhite.contains(posString) ? 1 : 0;
                    }
                    paintMode = !paintMode;
                }
                case '9' -> relativeBase += vals.get(p1Pos);
                default -> {
                    long p2Val = vals.get(switch (s.charAt(sLength-4)) {
                        case '0' -> vals.get(pos++);
                        case '1' -> pos++;
                        default -> relativeBase+vals.get(pos++);
                    });
                    switch (lastChar) {
                        case '5' -> { if (vals.get(p1Pos) != 0) pos = p2Val; }
                        case '6' -> { if (vals.get(p1Pos) == 0) pos = p2Val; }
                        default -> {
                            long p3Pos = s.charAt(s.length()-5) == '0' ? vals.get(pos++) : relativeBase+vals.get(pos++);
                            switch (lastChar) {
                                case '1' -> vals.set(p3Pos, vals.get(p1Pos)+p2Val);
                                case '2' -> vals.set(p3Pos, vals.get(p1Pos)*p2Val);
                                case '7' -> vals.set(p3Pos, vals.get(p1Pos) < p2Val ? 1 : 0);
                                case '8' -> vals.set(p3Pos, vals.get(p1Pos) == p2Val ? 1 : 0);
                                default -> throw new RuntimeException("Invalid final opCode character: "+lastChar);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("\nNumber of panels painted at least once (part 1 answer): "+paintedAtLeastOnce.size());
    }

    private static void solvePart2() {
        var vals = new ExtendedLongArray(origVals);
        boolean paintMode = true; int x = 0, y = 0; String posString = "0,0";
        var direction = NORTH; var currentlyWhite = new HashSet<String>(); currentlyWhite.add(posString);
        long pos = 0, relativeBase = 0, inputValue = 1, outputValue;
        while (true) {
            var s = Long.toString(vals.get(pos++));
            int sLength = s.length();
            if (sLength < 5) {
                switch (sLength) {
                    case 1 -> s = "0000"+s;
                    case 2 -> s = "000"+s;
                    case 3 -> s = "00"+s;
                    case 4 -> s = "0"+s;
                }
                sLength = 5;
            }
            char lastChar = s.charAt(s.length()-1);
            if (lastChar == '9' && s.charAt(sLength-2) == '9') break;
            char p1Char = s.charAt(sLength-3);
            long p1Pos = switch (p1Char) {
                case '0' -> vals.get(pos++);
                case '1' -> pos++;
                default -> relativeBase+vals.get(pos++);
            };
            switch (lastChar) {
                case '3' -> vals.set(p1Pos, inputValue);
                case '4' -> {
                    outputValue = vals.get(p1Pos);
                    if (paintMode) {
                        if (outputValue == 0) currentlyWhite.remove(posString);
                        else currentlyWhite.add(posString);
                    } else {
                        direction = outputValue == 0 ? direction.left() : direction.right();
                        switch (direction) { case NORTH -> y--; case SOUTH -> y++; case EAST -> x++; case WEST -> x--; }
                        posString = x+","+y;
                        inputValue = currentlyWhite.contains(posString) ? 1 : 0;
                    }
                    paintMode = !paintMode;
                }
                case '9' -> relativeBase += vals.get(p1Pos);
                default -> {
                    long p2Val = vals.get(switch (s.charAt(sLength-4)) {
                        case '0' -> vals.get(pos++);
                        case '1' -> pos++;
                        default -> relativeBase+vals.get(pos++);
                    });
                    switch (lastChar) {
                        case '5' -> { if (vals.get(p1Pos) != 0) pos = p2Val; }
                        case '6' -> { if (vals.get(p1Pos) == 0) pos = p2Val; }
                        default -> {
                            long p3Pos = s.charAt(s.length()-5) == '0' ? vals.get(pos++) : relativeBase+vals.get(pos++);
                            switch (lastChar) {
                                case '1' -> vals.set(p3Pos, vals.get(p1Pos)+p2Val);
                                case '2' -> vals.set(p3Pos, vals.get(p1Pos)*p2Val);
                                case '7' -> vals.set(p3Pos, vals.get(p1Pos) < p2Val ? 1 : 0);
                                case '8' -> vals.set(p3Pos, vals.get(p1Pos) == p2Val ? 1 : 0);
                                default -> throw new RuntimeException("Invalid final opCode character: "+lastChar);
                            }
                        }
                    }
                }
            }
        }
        int lowX = Integer.MAX_VALUE, lowY = Integer.MAX_VALUE, highX = Integer.MIN_VALUE, highY = Integer.MIN_VALUE;
        ArrayList<Coordinates> whiteCoordinates = new ArrayList<>();
        for (var s : currentlyWhite) {
            var ss = s.split(",");
            x = Integer.parseInt(ss[0]); y = Integer.parseInt(ss[1]);
            if (x < lowX) lowX = x; if (y < lowY) lowY = y;
            if (x > highX) highX = x; if (y > highY) highY = y;
            whiteCoordinates.add(new Coordinates(y, x));
        }
        highX -= lowX; highY -= lowY;
        boolean[][] isWhiteArray = new boolean[highY+1][highX+1];
        for (var c : whiteCoordinates) isWhiteArray[c.y-lowY][c.x-lowX] = true;
        System.out.println("\nMessage from painting with a white start location (part 2 answer): ");
        var sb = new StringBuilder();
        for (var row : isWhiteArray) {
            for (var isWhite : row) sb.append(isWhite ? '#' : ' ');
            System.out.println(sb);
            sb.setLength(0);
        }
    }

    private static class ExtendedLongArray {
        long[] baseArray;
        int baseLength;
        HashMap<Long, Long> extendedMap = new HashMap<>();

        ExtendedLongArray(long[] baseArray) {
            this.baseArray = baseArray;
            baseLength = baseArray.length;
        }

        private void set(long index, long value) {
            if (index < 0 || index >= baseLength) extendedMap.put(index, value);
            else baseArray[(int)index] = value;
        }

        private long get(long index) {
            return index < 0 || index >= baseLength ? extendedMap.getOrDefault(index, 0L) : baseArray[(int)index];
        }

    }

}
