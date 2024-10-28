package day6.puzzle2;

import java.io.*;

public class Main {
    static private final String INPUT_FILE_PATH = "input-files/day6input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    static private Race race;

    public static void main(String[] args) {
        getInputDataAndCreateRace();
        long numberOfWays = race.getNumberOfWaysToBeatTheRecord();
        System.out.println("\nNumber of ways to beat the record of the long races:\n\n"+numberOfWays);
    }

    private static void getInputDataAndCreateRace() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            long duration = getNumberFromLine(br.readLine().toCharArray());
            long distanceRecord = getNumberFromLine(br.readLine().toCharArray());
            race = new Race(duration, distanceRecord);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static long getNumberFromLine(char[] charArray) {
        StringBuilder number = new StringBuilder();
        for (char character : charArray) {
            if (Character.isDigit(character))
                number.append(character);
        }
        return Long.parseLong(number.toString());
    }

}