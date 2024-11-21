package day3.puzzle1;

import java.io.*;
import java.util.ArrayList;

public class Main {
    static private final String INPUT_FILE_PATH = "input-files/day3input.txt";
    static private final File INPUT_FILE = new File(INPUT_FILE_PATH);

    static private final char[][] schematicCharacters = new char[140][140];
    static private final CharacterType[][] characterTypes = new CharacterType[140][140];
    static private final ArrayList<Character> symbolList = new ArrayList<>(100);
    static private final ArrayList<int[]> symbolIndices = new ArrayList<>(1000);
    static private final ArrayList<int[]> allNumbers = new ArrayList<>(1000);
    static private final ArrayList<Integer> partNumbers = new ArrayList<>(1000);

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getSchematic();
        getCharacterTypes();
        getAllNumbers();
        determineWhichNumbersArePartNumbers();
        sumPartNumbers();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getSchematic() {
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
        for (int y = 0; y < 140; y++) { for (int x = 0; x < 140; x++) {
            char character = schematicCharacters[y][x];
            getCharacterType(character, y, x);
        }}
        System.out.println("\nSymbol list: "+symbolList);
        System.out.println("\nTotal number of symbols: "+symbolIndices.size());
    }

    private static void getCharacterType(char character, int y, int x) {
        switch (character){
            case '.':
                characterTypes[y][x] = CharacterType.NULL; break;
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                characterTypes[y][x] = CharacterType.NUMERAL; break;
            default:
                if (!symbolList.contains(character)) symbolList.add(character);
                characterTypes[y][x] = CharacterType.SYMBOL;
                symbolIndices.add(new int[]{x, y});
                System.out.println("Symbol at line "+y+", character "+x);
        }
    }

    private static void getAllNumbers() {
        String currentNumberBuild = "";
        int currentNumberYIndex = -1;
        int currentNumberXIndex = -1;
        CharacterType previousCharacterType;
        CharacterType currentCharacterType;
        CharacterType nextCharacterType;
        for (int y = 0; y < 140; y++) { for (int x = 0; x < 140; x++) {
            //Get current character type.
            currentCharacterType = characterTypes[y][x];
            //Care about current character only if it is a numeral...
            if (currentCharacterType == CharacterType.NUMERAL) {
                if (x == 0) previousCharacterType = CharacterType.NULL;
                else previousCharacterType = characterTypes[y][x-1];
                if (x == 139) nextCharacterType = CharacterType.NULL;
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

    private static void determineWhichNumbersArePartNumbers() {
        for (int[] number: allNumbers) {
            if (getIsNumberAPartNumber(number[1], number[2], String.valueOf(number[0]).length())) {
                partNumbers.add(number[0]);
            }
        }
        System.out.println("\nAll part numbers determined. Part numbers:");
        for (int partNumber : partNumbers) {
            System.out.println(partNumber);
        }
        System.out.println("\nTotal part number count: "+partNumbers.size());
    }

    private static boolean getIsNumberAPartNumber(int yIndex, int xIndex, int numberLength) {
        int lastDigitXIndex = xIndex+numberLength-1;
        if (yIndex > 0) {
            if (xIndex > 0 && characterTypes[yIndex-1][xIndex-1] == CharacterType.SYMBOL) return true; //Check above-left
            for (int i = 0; i < numberLength; i++) if (characterTypes[yIndex-1][xIndex+i] == CharacterType.SYMBOL) return true; //Check above each digit
            if (lastDigitXIndex < 139 && characterTypes[yIndex-1][lastDigitXIndex+1] == CharacterType.SYMBOL) return true; //Check above-right
        }
        if (xIndex > 0 && characterTypes[yIndex][xIndex-1] == CharacterType.SYMBOL) return true; //Check left
        if (lastDigitXIndex+1 < 139 && characterTypes[yIndex][lastDigitXIndex+1] == CharacterType.SYMBOL) return true; //Check right
        if (yIndex < 139) {
            if (xIndex > 0) if (characterTypes[yIndex+1][xIndex-1] == CharacterType.SYMBOL) return true; //Check below-left
            for (int i = 0; i < numberLength; i++) if (characterTypes[yIndex+1][xIndex+i] == CharacterType.SYMBOL) return true; // Check below each digit
            if (lastDigitXIndex < 139 && characterTypes[yIndex+1][lastDigitXIndex+1] == CharacterType.SYMBOL) return true; // Check below-right
        }
        return false;
    }

    private static void sumPartNumbers() {
        int sumOfPartNumbers = 0;
        for (int partNumber: partNumbers) {
            sumOfPartNumbers += partNumber;
        }
        System.out.println("\nPart numbers summed.\n\nSUM OF PART NUMBERS:\n\n"+sumOfPartNumbers);
    }

}