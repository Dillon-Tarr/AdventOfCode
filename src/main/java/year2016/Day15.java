package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day15 {
    static private final int DAY = 15;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final ArrayList<Disc> discs = new ArrayList<>();

    static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        processInputData();
        if (args.length != 0) discs.add(new Disc((discs.size()+1), 11, 0)); // Part 2 mode
        findWhenToPressButton();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processInputData() {
        int discNumber = 0;
        for (String s : inputStrings) {
            discNumber++;
            int pIndex = s.indexOf("p");
            int size = Integer.parseInt(s.substring(12, pIndex-1));
            int startPosition = Integer.parseInt(s.substring(s.lastIndexOf(' ')+1, s.length()-1));
            discs.add(new Disc(discNumber, size, startPosition));
        }
    }

    private static void findWhenToPressButton() {
        discs.sort(Disc::compareTo); // Sorting largest to smallest to check least likely events first in the loop.
        int t = -1;
        mainLoop: while (true) {
            t++;
            for (Disc d : discs) if ((t+d.startPosition+d.number)%d.size != 0) continue mainLoop;
            break;
        }
        System.out.println("\nWhen to press button to get a capsule: "+t);
    }

    private static class Disc implements Comparable <Disc> {
        int number, size, startPosition;

        Disc(int number, int size, int startPosition) {
            this.number = number;
            this.size = size;
            this.startPosition = startPosition;
        }

        @Override
        public int compareTo(Disc other) {
            return Integer.compare(other.size, size);
        }

    }

}
