package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

class Day17 {
    static private final int DAY = 17;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private long[] origVals;
    static private int height, width;
    static private char[][] view;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        runProgram();
        solvePart1();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var ss = br.readLine().split(","); origVals = new long[ss.length];
            for (int i = 0; i < ss.length; i++) origVals[i] = Long.parseLong(ss[i]);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void runProgram() {
        var vals = new ExtendedLongArray(Arrays.copyOf(origVals, origVals.length));
        long pos = 0, relativeBase = 0, inputValue = 0, outputValue;
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
                case '3' -> vals.set(p1Pos, inputValue);
                case '4' -> {
                    outputValue = vals.get(p1Pos);
                    outputList.append((char)outputValue);
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
        height = rows.length;
        view = new char[height][];
        width = rows[0].length();
        for (int i = 0; i < height; i++) {
            var row = rows[i];
            if (row.length() != width) throw new RuntimeException("Rows aren't all the same length??");
            view[i] = row.toCharArray();
            System.out.println(row);
        }
        System.out.println("Program loop completed. ^^output^^");
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
