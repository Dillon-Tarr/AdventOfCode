package year2023.day6.part1;

import java.io.*;
import java.util.ArrayList;

class Main {
    static private final int DAY = 6;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private final ArrayList<Race> races = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputDataAndCreateRaces();
        multiplyWaysToWinRaces();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void multiplyWaysToWinRaces() {
        int productOfWaysToBeatRecordOfAllRaces = 1;
        for (Race race: races) productOfWaysToBeatRecordOfAllRaces *= race.getNumberOfWaysToBeatTheRecord();

        System.out.println("\nProduct of ways to beat the record of all races:\n\n"+productOfWaysToBeatRecordOfAllRaces);
    }

    private static void getInputDataAndCreateRaces() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            ArrayList<Integer> durations = getNumbersFromLine(br.readLine().toCharArray());
            ArrayList<Integer> distanceRecords = getNumbersFromLine(br.readLine().toCharArray());
            if (durations.size() != distanceRecords.size())
                throw new RuntimeException("\"Time\" and \"Distance\" number counts in input file differ.");
            for (int i = 0; i < durations.size(); i++) races.add(new Race(durations.get(i), distanceRecords.get(i)));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static ArrayList<Integer> getNumbersFromLine(char[] charArray) {
        ArrayList<Integer> numbers = new ArrayList<>();

        boolean currentlyBuildingANumber = false;
        StringBuilder currentNumber = new StringBuilder();

        for (char character : charArray) {
            if (currentlyBuildingANumber) {
                if (Character.isDigit(character))
                    currentNumber.append(character);
                else { // !isDigit
                    numbers.add(Integer.parseInt(currentNumber.toString()));
                    currentNumber.setLength(0);
                    currentlyBuildingANumber = false;
                }
            } else { // !currentlyBuildingANumber
                if (Character.isDigit(character)) {
                    currentlyBuildingANumber = true;
                    currentNumber.append(character);
                }
            }
        }

        if (!currentNumber.isEmpty()) numbers.add(Integer.parseInt(currentNumber.toString()));
        return numbers;
    }

}