package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day16 {
    static private final int DAY = 16;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final int length = 16; // of programs string; example: 5 -> "abcde"
    static private final int numberOfDancesToDo = 1_000_000_000;
    static private int numberOfDancesCompleted = 0;
    static private final StringBuilder currentPositions = new StringBuilder();
    static private final ArrayList<String> positionsAfterIndexDances = new ArrayList<>();
    static private String[] moveStrings;
    static private Move[] danceMoves;

    static void main() {
        long startTime = System.nanoTime();

        setInitialProgramPositions();
        getInputData();
        prepDanceMoves();
        doDanceMoves();
        System.out.println("\nPart 1 answer (program positions after one dance): "+currentPositions+"\n");
        while (numberOfDancesCompleted < numberOfDancesToDo) {
            System.out.println(numberOfDancesCompleted +" dances done. Current positions: "+ currentPositions);
            doDanceMoves();
        }
        System.out.println("\nProgram positions after "+numberOfDancesToDo+" dances (part 2 answer): "+currentPositions);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void setInitialProgramPositions() {
        if (length > 26) throw new RuntimeException("Length too long: "+length);
        for (int i = 0; i < length; i++) currentPositions.append((char) (97+i)); // 97 == 'a'
        System.out.println("\nPrograms placed in initial positions: "+ currentPositions);
        positionsAfterIndexDances.add(currentPositions.toString());
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            moveStrings = br.readLine().split(",");
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void prepDanceMoves() {
        danceMoves = new Move[moveStrings.length];
        for (int i = 0; i < moveStrings.length; i++) {
            String s = moveStrings[i];
            switch (s.charAt(0)) {
                case 's' -> danceMoves[i] = new Spin(Integer.parseInt(s.substring(1)));
                case 'x' -> {
                    int slashIndex = s.indexOf("/");
                    danceMoves[i] = new Exchange(Integer.parseInt(s.substring(1, slashIndex)), Integer.parseInt(s.substring(slashIndex+1)));
                }
                case 'p' -> danceMoves[i] = new Partner(s.charAt(1), s.charAt(3));
            }
        }
    }

    private static void doDanceMoves() {
        for (Move move : danceMoves) move.doMove();
        numberOfDancesCompleted++;
        String currentString = currentPositions.toString();
        for (int i = 0; i < positionsAfterIndexDances.size(); i++) {
            if (currentString.equals(positionsAfterIndexDances.get(i))) {
                int period = numberOfDancesCompleted - i;
                System.out.println("\nPeriod found after completing "+ numberOfDancesCompleted +" dances.");
                System.out.println("Period of length "+period+" begins at "+i+" dance(s); Positions at period beginning/end: "+currentString);
                while (numberOfDancesCompleted < numberOfDancesToDo) numberOfDancesCompleted += period;
                numberOfDancesCompleted -= period;
                int distanceFromGoal = numberOfDancesToDo - numberOfDancesCompleted;
                System.out.println("Repeatedly adding the period brings us to "+ numberOfDancesCompleted +" completed dances. Distance from goal: "+distanceFromGoal);
                System.out.println("The positions after "+distanceFromGoal+
                        " more dances would be the same as the positions after (beginningOfPeriod ("+i+") plus distanceFromGoal ("+distanceFromGoal+") == "+(distanceFromGoal+i)+") dances.");
                currentPositions.setLength(0);
                currentPositions.append(positionsAfterIndexDances.get(i+distanceFromGoal));
                numberOfDancesCompleted = numberOfDancesToDo;
                return;
            }
        }
        positionsAfterIndexDances.add(currentString);
    }

    private static abstract class Move {
        abstract void doMove();
    }

    private static class Spin extends Move {
        int spinDistance;

        Spin(int spinDistance) {
            this.spinDistance = spinDistance;
        }

        void doMove() {
            currentPositions.insert(0, currentPositions.substring(length-spinDistance));
            currentPositions.delete(length, length+spinDistance);
        }

    }

    private static class Exchange extends Move {
        int i1, i2;

        Exchange(int index1, int index2) {
            i1 = index1;
            i2 = index2;
        }

        void doMove() {
            char c1 = currentPositions.charAt(i1);
            char c2 = currentPositions.charAt(i2);
            currentPositions.setCharAt(i1, c2);
            currentPositions.setCharAt(i2, c1);
        }

    }

    private static class Partner extends Move {
        char c1, c2;
        String c1String, c2String;

        Partner(char char1, char char2) {
            c1 = char1;
            c2 = char2;
            c1String = c1+"";
            c2String = c2+"";
        }

        void doMove() {
            int i1 = currentPositions.indexOf(c1String);
            int i2 = currentPositions.indexOf(c2String);
            currentPositions.setCharAt(i1, c2);
            currentPositions.setCharAt(i2, c1);
        }
    }

}
