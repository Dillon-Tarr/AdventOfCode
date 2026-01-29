package year2015.day3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

class Part2 {
    static private final int DAY = 3;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");

    static private String inputString;
    static private int santaX = 0, santaY = 0, roboSantaX = 0, roboSantaY = 0;
    static private final HashSet<String> visitedCoordinatesData = new HashSet<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        deliverPresents();

        System.out.println("\nNumber of house changes: "+inputString.length());
        System.out.println("\nNumber of houses visited at least once: "+ visitedCoordinatesData.size());

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void deliverPresents() {
        deliverPresent(santaX, santaY);

        boolean santasTurn = true;
        for (int i = 0; i < inputString.length(); i++) {
            if (santasTurn) {
                switch (inputString.charAt(i)) {
                    case '>' -> santaX++;
                    case '<' -> santaX--;
                    case 'v' -> santaY++;
                    case '^' -> santaY--;
                }
                deliverPresent(santaX, santaY);
            }
            else {
                switch (inputString.charAt(i)) {
                    case '>' -> roboSantaX++;
                    case '<' -> roboSantaX--;
                    case 'v' -> roboSantaY++;
                    case '^' -> roboSantaY--;
                }
                deliverPresent(roboSantaX, roboSantaY);
            }
            santasTurn = !santasTurn;
        }
    }

    private static void deliverPresent(int x, int y) {
        visitedCoordinatesData.add(x+","+y);
    }

}
