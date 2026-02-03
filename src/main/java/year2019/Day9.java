package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

class Day9 {
    static private final int DAY = 9;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private long[] origVals;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        System.out.println("\nBOOST keycode (part 1 answer): "+
                runProgram(1, new ExtendedLongArray(Arrays.copyOf(origVals, origVals.length))));
        System.out.println("\nDistress signal coordinates (part 2 answer): "+
                runProgram(2, new ExtendedLongArray(origVals)));

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var ss = br.readLine().split(","); origVals = new long[ss.length];
            for (int i = 0; i < ss.length; i++) origVals[i] = Long.parseLong(ss[i]);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static long runProgram(int inputValue, ExtendedLongArray vals) {
        long pos = 0, relativeBase = 0, outputValue = 1;
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
                case '4' -> outputValue = vals.get(p1Pos);
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
        return outputValue;
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
