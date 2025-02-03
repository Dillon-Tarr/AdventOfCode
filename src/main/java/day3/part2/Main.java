package day3.part2;

import java.io.*;
import java.util.ArrayList;

public class Main {
    static private final String INPUT_FILE_PATH = "input-files/day3input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);
    static private final int SCHEMATIC_LINE_COUNT = getSchematicLineCount();
    static private final int SCHEMATIC_LINE_LENGTH = getSchematicLineLength();

    static private final char[][] schematicCharacters = new char[SCHEMATIC_LINE_COUNT][SCHEMATIC_LINE_LENGTH];
    static private final CharacterType[][] characterTypes = new CharacterType[SCHEMATIC_LINE_COUNT][SCHEMATIC_LINE_LENGTH];
    static private final ArrayList<PotentialGear> potentialGears = new ArrayList<>(1000);
    static private final ArrayList<int[]> allNumbers = new ArrayList<>(1000);

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getSchematicCharacters();
        getCharacterTypes();
        getAllNumbers();
        getAllPotentialGearHits();
        sumGearRatios();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static int getSchematicLineCount() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {

            int schematicLineCount = 0;
            String currentLine = br.readLine();
            while (currentLine != null) {
                currentLine = br.readLine();
                schematicLineCount++;
            }
            return schematicLineCount;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static int getSchematicLineLength() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String currentLine = br.readLine();
            return currentLine.length();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getSchematicCharacters() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            System.out.println("\nObtaining lines from engine schematic.\n\nLines:");

            String currentLine = br.readLine();
            int i = 0;
            while (currentLine != null) {
                schematicCharacters[i] = currentLine.toCharArray();
                System.out.println(schematicCharacters[i]);
                currentLine = br.readLine();
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getCharacterTypes() {
        for (int y = 0; y < SCHEMATIC_LINE_COUNT; y++) { for (int x = 0; x < SCHEMATIC_LINE_LENGTH; x++) {
            char character = schematicCharacters[y][x];
            getCharacterType(character, y, x);
        }}
        System.out.println("\nTotal number of potential gears: "+potentialGears.size());
    }

    private static void getCharacterType(char character, int y, int x) {
        switch (character){
            case '*':
                System.out.println("Potential gear at line "+y+", character "+x);
                characterTypes[y][x] = CharacterType.GEAR;
                potentialGears.add(new PotentialGear(x, y)); break;
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                characterTypes[y][x] = CharacterType.NUMERAL; break;
            default:
                characterTypes[y][x] = CharacterType.NULL; break;
        }
    }

    private static void getAllNumbers() {
        String currentNumberBuild = "";
        int currentNumberYIndex = -1;
        int currentNumberXIndex = -1;
        CharacterType previousCharacterType;
        CharacterType currentCharacterType;
        CharacterType nextCharacterType;
        for (int y = 0; y < SCHEMATIC_LINE_COUNT; y++) { for (int x = 0; x < SCHEMATIC_LINE_LENGTH; x++) {
            //Get current character type.
            currentCharacterType = characterTypes[y][x];
            //Care about current character only if it is a numeral...
            if (currentCharacterType == CharacterType.NUMERAL) {
                if (x == 0) previousCharacterType = CharacterType.NULL;
                else previousCharacterType = characterTypes[y][x-1];
                if (x == SCHEMATIC_LINE_LENGTH -1) nextCharacterType = CharacterType.NULL;
                else nextCharacterType = characterTypes[y][x+1];
                //If previous character is not a numeral. Indicate that this is the beginning of a new number.
                if (previousCharacterType != CharacterType.NUMERAL) {
                    currentNumberYIndex = y; currentNumberXIndex = x;
                    currentNumberBuild = "";
                }
                //Whether starting new number or continuing one, add current character.
                currentNumberBuild+=schematicCharacters[y][x];

                //If this character is the last character of a number, add it and its indices to the allNumbers ArrayList.
                if (nextCharacterType != CharacterType.NUMERAL) {
                    allNumbers.add(new int[]{Integer.parseInt(currentNumberBuild), currentNumberYIndex, currentNumberXIndex});
                    currentNumberBuild = "";
                }
            }
        }}
        System.out.println("\nAll numbers collected. All numbers:");
        for (int[] number : allNumbers) {
            System.out.println(number[0]);
        }
        System.out.println("\nTotal number count: "+allNumbers.size());
    }

    private static void getAllPotentialGearHits() {
        for (int[] number: allNumbers) {
            getPotentialGearHitsForNumber(number[1], number[2], number[0]);
        }
    }

    private static void getPotentialGearHitsForNumber(int yIndex, int xIndex, int number) {
        int numberLength = Integer.toString(number).length();
        int lastDigitXIndex = xIndex+numberLength-1;
        if (yIndex > 0) {
            if (xIndex > 0 && characterTypes[yIndex-1][xIndex-1] == CharacterType.GEAR)
                updatePotentialGear(xIndex-1, yIndex-1, number); //Check above-left
            for (int i = 0; i < numberLength; i++) if (characterTypes[yIndex-1][xIndex+i] == CharacterType.GEAR)
                updatePotentialGear(xIndex+i, yIndex-1, number); //Check above each digit
            if (lastDigitXIndex < SCHEMATIC_LINE_LENGTH -1 && characterTypes[yIndex-1][lastDigitXIndex+1] == CharacterType.GEAR)
                updatePotentialGear(lastDigitXIndex+1, yIndex-1, number); //Check above-right
        }
        if (xIndex > 0 && characterTypes[yIndex][xIndex-1] == CharacterType.GEAR)
            updatePotentialGear(xIndex-1, yIndex, number); //Check left
        if (lastDigitXIndex+1 < SCHEMATIC_LINE_LENGTH -1 && characterTypes[yIndex][lastDigitXIndex+1] == CharacterType.GEAR)
            updatePotentialGear(lastDigitXIndex+1, yIndex, number); //Check right
        if (yIndex < SCHEMATIC_LINE_COUNT -1) {
            if (xIndex > 0) if (characterTypes[yIndex+1][xIndex-1] == CharacterType.GEAR)
                updatePotentialGear(xIndex-1, yIndex+1, number); //Check below-left
            for (int i = 0; i < numberLength; i++) if (characterTypes[yIndex+1][xIndex+i] == CharacterType.GEAR)
                updatePotentialGear(xIndex+i, yIndex+1, number); // Check below each digit
            if (lastDigitXIndex < SCHEMATIC_LINE_COUNT -1 && characterTypes[yIndex+1][lastDigitXIndex+1] == CharacterType.GEAR)
                updatePotentialGear(lastDigitXIndex+1, yIndex+1, number); // Check below-right
        }
    }

    private static void updatePotentialGear(int gearXIndex, int gearYIndex, int adjacentNumber) {
        for (PotentialGear gear : potentialGears) {
            if (gear.getXIndex() == gearXIndex && gear.getYIndex() == gearYIndex) {
                gear.addAdjacentNumberHit(adjacentNumber);
                break;
            }
        }
    }

    private static void sumGearRatios() {
        int gearCount = 0;
        int sumOfGearRatios = 0;
        System.out.println("\nAll gears and gear ratios found. Gear ratios:");
        for (PotentialGear potentialGear: potentialGears) {
            if (potentialGear.isAGear()) {
                gearCount++;
                int gearRatio = potentialGear.getGearRatio();
                sumOfGearRatios += gearRatio;
                System.out.println(gearRatio);
            }
        }
        System.out.println("\nOf "+potentialGears.size()+" potential gears, "+gearCount+" were actually gears.");
        System.out.println("As a reminder, a gear is a * with exactly two adjacent (including diagonally adjacent) numbers.");
        System.out.println("\nAll gear ratios summed.\n\nSUM OF GEAR RATIOS:\n\n"+sumOfGearRatios);
    }

}