package year2019;

import shared.CardinalDirection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static shared.CardinalDirection.*;

class Day17 {
    static private final int DAY = 17;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private long[] origVals;
    static private int height, width, startY, startX;
    static private CardinalDirection orientation;
    static private char[][] view;
    static private final StringBuilder fullInstructions = new StringBuilder();
    static private final StringBuilder constrainedInstructionSequence = new StringBuilder();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        runProgram(false);
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

    private static void runProgram(boolean part2Mode) {
        var vals = new ExtendedLongArray(Arrays.copyOf(origVals, origVals.length));
        if (part2Mode) {
            vals.set(0, 2);
            System.out.println("\nRunning part 2 program instructions.");
        }
        int part2InstructionIndex = 0;
        long pos = 0, relativeBase = 0, inputValue = 0, outputValue = -1;
        var outputList = new StringBuilder();
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
                case '3' -> {
                    if (part2Mode && part2InstructionIndex < constrainedInstructionSequence.length()) {
                        inputValue = constrainedInstructionSequence.charAt(part2InstructionIndex++);
                        }
                    vals.set(p1Pos, inputValue);
                }
                case '4' -> {
                    outputValue = vals.get(p1Pos);
                    if (!part2Mode) outputList.append((char)outputValue);
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
        var rows = outputList.toString().split("\n");
        if (!part2Mode) {
            height = rows.length;
            view = new char[height][];
            width = rows[0].length();
            for (int i = 0; i < rows.length; i++) {
                var row = rows[i];
                if (row.length() != width) throw new RuntimeException("Rows aren't all the same length??");
                view[i] = row.toCharArray();
                System.out.println(row);
            }
            System.out.println("Program loop completed. ^^output^^");
        } else System.out.println("Dust collected by vacuum robot (part 2 answer): "+outputValue);
    }

    private static void solvePart1() {
        int sum = 0;
        char[] aboveRow, row = view[0];
        for (int y = 1; y < height-1; y++) {
            aboveRow = row; row = view[y];
            char leftCh, ch = row[0];
            for (int x = 1; x < width-1; x++) {
                leftCh = ch; ch = row[x];
                if (ch == '#' && leftCh == '#' && row[x+1] == '#' && aboveRow[x] == '#') sum += x * y;
            }
        }
        System.out.println("Sum of alignment parameters (part 1 answer): "+ sum);
    }

    private static void solvePart2() {
        findStartingPointAndDirection();
        generateFullInstructions();
        generateConstrainedInstructions();
        runProgram(true);
    }

    private static void findStartingPointAndDirection() {
        startY = -1;
        outerLoop: for (int y = 0; y < height; y++) {
            var row = view[y];
            for (int x = 0; x < width; x++) {
                switch(row[x]) {
                    case '<', '>', '^', 'v' -> {
                        startY = y;
                        startX = x;
                        break outerLoop;
                    }
                }
            }
        }
        if (startY < 0) throw new RuntimeException("Starting point not found.");
        orientation = switch (view[startY][startX]) {
            case '<' -> WEST;
            case '>' -> EAST;
            case '^' -> NORTH;
            case 'v' -> SOUTH;
            default -> throw new RuntimeException("This should not be possible.");
        };
    }

    private static void generateFullInstructions() {
        int y = startY, x = startX;
        if (y > 0 && view[y-1][x] == '#') {
            switch(orientation) {
                case SOUTH -> fullInstructions.append("L,L");
                case WEST -> fullInstructions.append("R,");
                case EAST -> fullInstructions.append("L,");
            }
            orientation = NORTH;
        }
        else if (y < height-1 && view[y+1][x] == '#') {
            switch(orientation) {
                case NORTH -> fullInstructions.append("L,L");
                case WEST -> fullInstructions.append("L,");
                case EAST -> fullInstructions.append("R,");
            }
            orientation = SOUTH;
        }
        else if (x > 0 && view[y][x-1] == '#') {
            switch(orientation) {
                case NORTH -> fullInstructions.append("L,");
                case SOUTH -> fullInstructions.append("R,");
                case EAST -> fullInstructions.append("L,L");
            }
            orientation = WEST;
        }
        else if (x < width-1 && view[y][x+1] == '#') {
            switch(orientation) {
                case NORTH -> fullInstructions.append("R,");
                case SOUTH -> fullInstructions.append("L,");
                case WEST -> fullInstructions.append("L,L");
            }
            orientation = EAST;
        }
        else throw new RuntimeException("This should not be possible.");
        int stepCount = 0;
        mainLoop: while (true) {
            switch(orientation) {
                case NORTH -> {
                    if (y > 0 && view[y-1][x] == '#') { y--; stepCount++; }
                    else if (x > 0 && view[y][x-1] == '#') {
                        orientation = WEST; fullInstructions.append(stepCount).append(",L,");
                        x--; stepCount = 1;
                    } else if (x < width-1 && view[y][x+1] == '#') {
                        orientation = EAST; fullInstructions.append(stepCount).append(",R,");
                        x++; stepCount = 1;
                    } else { fullInstructions.append(stepCount).append(','); break mainLoop; }
                }
                case SOUTH -> {
                    if (y < height-1 && view[y+1][x] == '#') { y++; stepCount++; }
                    else if (x < width-1 && view[y][x+1] == '#') {
                        orientation = EAST; fullInstructions.append(stepCount).append(",L,");
                        x++; stepCount = 1;
                    } else if (x > 0 && view[y][x-1] == '#') {
                        orientation = WEST; fullInstructions.append(stepCount).append(",R,");
                        x--; stepCount = 1;
                    } else { fullInstructions.append(stepCount).append(','); break mainLoop; }
                }
                case WEST -> {
                    if (x > 0 && view[y][x-1] == '#') { x--; stepCount++; }
                    else if (y < height-1 && view[y+1][x] == '#') {
                        orientation = SOUTH; fullInstructions.append(stepCount).append(",L,");
                        y++; stepCount = 1;
                    } else if (y > 0 && view[y-1][x] == '#') {
                        orientation = NORTH; fullInstructions.append(stepCount).append(",R,");
                        y--; stepCount = 1;
                    } else { fullInstructions.append(stepCount).append(','); break mainLoop; }
                }
                case EAST -> {
                    if (x < width-1 && view[y][x+1] == '#') { x++; stepCount++; }
                    else if (y > 0 && view[y-1][x] == '#') {
                        orientation = NORTH; fullInstructions.append(stepCount).append(",L,");
                        y--; stepCount = 1;
                    } else if (y < height-1 && view[y+1][x] == '#') {
                        orientation = SOUTH; fullInstructions.append(stepCount).append(",R,");
                        y++; stepCount = 1;
                    } else { fullInstructions.append(stepCount).append(','); break mainLoop; }
                }
            }
        }
    }

    private static void generateConstrainedInstructions() {
        Pattern constrainingPattern = Pattern.compile(
                "^(.{1,21})\\1*(.{1,21})(?:\\1|\\2)*(.{1,21})(?:\\1|\\2|\\3)*$");
        Matcher m = constrainingPattern.matcher(fullInstructions);
        if (!m.matches()) throw new RuntimeException("No solution");
        String A = m.group(1);
        String B = m.group(2);
        String C = m.group(3);
        constrainedInstructionSequence.append(fullInstructions.toString()
                .replaceAll(A, "A,")
                .replaceAll(B, "B,")
                .replaceAll(C, "C,")).setLength(constrainedInstructionSequence.length()-1);
        A = A.substring(0, A.length()-1)+(char)10;
        B = B.substring(0, B.length()-1)+(char)10;
        C = C.substring(0, C.length()-1)+(char)10;
        constrainedInstructionSequence.append((char)10).append(A).append(B).append(C).append('n').append((char)10);
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
