package day2.puzzle1;

import java.io.*;

import java.util.ArrayList;


public class Main {
    static private final String INPUT_FILE_PATH = "src/day1/input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);

    static private final ArrayList<String> gamesData = new ArrayList<>(100);
    static private final ArrayList<String> possibleGameIds = new ArrayList<>(100);
    static private final int hypotheticalRedCubeCount = 12;
    static private final int hypotheticalGreenCubeCount = 13;
    static private final int hypotheticalBlueCubeCount = 14;


    public static void main(String[] args) {
        getGamesData();
        determinePossibleGames();
        sumPossibleGameIds();
    }

    private static void getGamesData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))){
            String currentLine = br.readLine();

            while (currentLine != null) {
                gamesData.add(currentLine);
                currentLine = br.readLine();
            }
            System.out.println("\nData from all games obtained from file.\n");
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private static void determinePossibleGames() {
        for (int i = 0; i < gamesData.size(); i++) {
            String game = gamesData.get(i);

            if (returnPossible(game)) possibleGameIds.add(""+(i+1));
        }
    }

    private static boolean returnPossible(String game) {
        String stringOfPulls = game.substring(game.indexOf(":")+2);
        String commaSeparatedStringOfSubPulls = stringOfPulls.replaceAll(";", "");
        String stringOfSubPulls = commaSeparatedStringOfSubPulls.replaceAll(",", "");
        return allSubPullsArePossibleCheck(stringOfSubPulls);
    }

    private static boolean allSubPullsArePossibleCheck(String stringOfSubPulls) {
        String shrinkingString = stringOfSubPulls;
        while (!shrinkingString.isEmpty()){
                String count;
                String color;

                int indexOfNextSpace = shrinkingString.indexOf(" ");
                count = shrinkingString.substring(0, indexOfNextSpace);
                shrinkingString = shrinkingString.substring(indexOfNextSpace+1);

                indexOfNextSpace = shrinkingString.indexOf(" ");
                if (indexOfNextSpace == -1) {
                    color = shrinkingString;
                    shrinkingString = "";
                } else {
                    color = shrinkingString.substring(0, indexOfNextSpace);
                    shrinkingString = shrinkingString.substring(indexOfNextSpace+1);
                }

                switch (color) {
                    case "red" -> {if (Integer.parseInt(count) > hypotheticalRedCubeCount) return false;}
                    case "green" -> {if (Integer.parseInt(count) > hypotheticalGreenCubeCount) return false;}
                    case "blue" -> {if (Integer.parseInt(count) > hypotheticalBlueCubeCount) return false;}
                    default -> throw new RuntimeException("Invalid color string detected.");
                }
            }
        return true;
    }

    private static void sumPossibleGameIds() {
        int sumOfPossibleGames = 0;
        System.out.println("Possible game IDs:");
        for (String id: possibleGameIds) {
            System.out.println(id);
            sumOfPossibleGames += Integer.parseInt(id);
        }
        System.out.println("\nSUM OF THE IDS OF ALL POSSIBLE GAMES:\n\n"+ sumOfPossibleGames);
    }

}