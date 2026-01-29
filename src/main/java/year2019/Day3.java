package year2019;

import shared.Coordinates;
import shared.InclusiveNumberRange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day3 {
    static private final int DAY = 3;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private final ArrayList<Instruction> wire1Instructions = new ArrayList<>(), wire2Instructions = new ArrayList<>();
    static private final ArrayList<Segment> wire1VerticalSegments = new ArrayList<>(), wire2VerticalSegments = new ArrayList<>(),
            wire1HorizontalSegments = new ArrayList<>(), wire2HorizontalSegments = new ArrayList<>();
    static private final ArrayList<Coordinates> intersections = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        String[] ss1, ss2;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            ss1 = br.readLine().split(",");
            ss2 = br.readLine().split(",");
        } catch (IOException e) {throw new RuntimeException(e);}
        for (var s : ss1) wire1Instructions.add(new Instruction(s.charAt(0), Integer.parseInt(s.substring(1))));
        for (var s : ss2) wire2Instructions.add(new Instruction(s.charAt(0), Integer.parseInt(s.substring(1))));
    }

    private static void solvePart1() {
        markSegments(wire1Instructions, wire1VerticalSegments, wire1HorizontalSegments);
        markSegments(wire2Instructions, wire2VerticalSegments, wire2HorizontalSegments);
        int closestIntersectionDistance = Math.min(findClosestIntersection(wire1VerticalSegments, wire2HorizontalSegments),
                findClosestIntersection(wire2VerticalSegments, wire1HorizontalSegments));
        System.out.println("\nClosest intersection distance (part 1 answer): "+closestIntersectionDistance);
    }

    private static void markSegments(ArrayList<Instruction> instructions, ArrayList<Segment> vSegs, ArrayList<Segment> hSegs) {
        int x = 0, y = 0; char c = instructions.getFirst().directionChar;
        boolean vertical = c == 'U' || c == 'D';
        for (var instruction : instructions) {
            if (vertical) {
                int newY = y;
                if (instruction.directionChar == 'U') newY += instruction.distance; else newY -= instruction.distance;
                vSegs.add(new Segment(x, new InclusiveNumberRange(y, newY)));
                y = newY;
            } else {
                int newX = x;
                if (instruction.directionChar == 'R') newX += instruction.distance; else newX -= instruction.distance;
                hSegs.add(new Segment(y, new InclusiveNumberRange(x, newX)));
                x = newX;
            }
            vertical = !vertical;
        }
    }

    private static int findClosestIntersection(ArrayList<Segment> verticalSegments, ArrayList<Segment> horizontalSegments) {
        int closestIntersectionDistance = Integer.MAX_VALUE;
        for (var vSeg : verticalSegments) {
            var vRange = vSeg.range;
            int vX = vSeg.staticValue, vLowY = vRange.rangeStart, vHighY = vRange.inclusiveRangeEnd;
            for (var hSeg : horizontalSegments) {
                int hY = hSeg.staticValue;
                if (vX == 0 && hY == 0) continue;
                var hRange = hSeg.range;
                if (hY >= vLowY && hY <= vHighY && vX >= hRange.rangeStart && vX <= hRange.inclusiveRangeEnd) {
                    intersections.add(new Coordinates(hY, vX));
                    int distance = Math.abs(hY)+Math.abs(vX);
                    if (distance < closestIntersectionDistance) closestIntersectionDistance = distance;
                }
            }
        }
        return closestIntersectionDistance;
    }

    private static void solvePart2() {
        int closestCombinedStepDistance = Integer.MAX_VALUE;
        for (var intersection : intersections) {
            int distance = getStepsToIntersection(intersection, wire1Instructions, wire1VerticalSegments, wire1HorizontalSegments)
                    + getStepsToIntersection(intersection, wire2Instructions, wire2VerticalSegments, wire2HorizontalSegments);
            if (distance < closestCombinedStepDistance) closestCombinedStepDistance = distance;
        }
        System.out.println("\nClosest combined step distance to an intersection (part 2 answer): "+closestCombinedStepDistance);
    }

    private static int getStepsToIntersection(Coordinates coordinates, ArrayList<Instruction> instructions,
                                              ArrayList<Segment> verticalSegments, ArrayList<Segment> horizontalSegments) {
        char c = instructions.getFirst().directionChar; boolean vertical = c == 'U' || c == 'D';
        InclusiveNumberRange range;
        int goalX = coordinates.x, goalY = coordinates.y, x = 0, y = 0, v = 0, h = 0, i = 0, distance = 0;
        while (true) {
            if (vertical) {
                range = verticalSegments.get(v++).range;
                int rangeStart = range.rangeStart, rangeEnd = range.inclusiveRangeEnd;
                if (x == goalX && goalY >= rangeStart && goalY <= rangeEnd) return distance + Math.abs(goalY - y);
                else {
                    distance += instructions.get(i++).distance;
                    y = y == rangeStart ? rangeEnd : rangeStart;
                }
            } else {
                range = horizontalSegments.get(h++).range;
                int rangeStart = range.rangeStart, rangeEnd = range.inclusiveRangeEnd;
                if (y == goalY && goalX >= rangeStart && goalX <= rangeEnd) return distance + Math.abs(goalX - x);
                else {
                    distance += instructions.get(i++).distance;
                    x = x == rangeStart ? rangeEnd : rangeStart;
                }
            }
            vertical = !vertical;
        }
    }

    private record Instruction(char directionChar, int distance){}

    private record Segment(int staticValue, InclusiveNumberRange range){}

}
