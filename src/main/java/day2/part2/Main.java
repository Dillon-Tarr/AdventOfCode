package day2.part2;

import java.io.*;

import java.util.ArrayList;

public class Main {
    static private final String INPUT_FILE_PATH = "input-files/day2input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);

    static private final ArrayList<String> gamesData = new ArrayList<>(100);
    static private final ArrayList<Integer> powers = new ArrayList<>(100);

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getGamesData();
        getPowers();
        sumPowers();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
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

    private static void getPowers() {
        for (String game : gamesData) powers.add(getPower(game));
    }

    private static int getPower(String game){
        String shrinkingString = getSubPullsString(game);
        int requiredRedCubes = 0, requiredGreenCubes = 0, requiredBlueCubes = 0;
        while (!shrinkingString.isEmpty()){
            int count;
            String color;

            int indexOfNextSpace = shrinkingString.indexOf(" ");
            count = Integer.parseInt(shrinkingString.substring(0, indexOfNextSpace));
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
                case "red" -> {if (count > requiredRedCubes) requiredRedCubes = count;}
                case "green" -> {if (count > requiredGreenCubes) requiredGreenCubes = count;}
                case "blue" -> {if (count > requiredBlueCubes) requiredBlueCubes = count;}
                default -> throw new RuntimeException("Invalid color string detected.");
            }
        }
        return requiredRedCubes*requiredGreenCubes*requiredBlueCubes;
    }

    private static String getSubPullsString(String gameString) {
        String stringOfPulls = gameString.substring(gameString.indexOf(":")+2);
        String commaSeparatedStringOfSubPulls = stringOfPulls.replaceAll(";", "");
        return commaSeparatedStringOfSubPulls.replaceAll(",", "");
    }

    private static void sumPowers() {
        int sumOfPowers = 0;
        System.out.println("Power of each game:");
        for (int power: powers) {
            System.out.println(power);
            sumOfPowers += power;
        }
        System.out.println("\nSUM OF POWER OF ALL GAMES:\n\n"+ sumOfPowers);
    }

}