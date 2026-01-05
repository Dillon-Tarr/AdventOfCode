package shared;

import java.util.ArrayList;
import java.util.Arrays;

public class SquareArrayOperations {

    public static ArrayList<boolean[][]> getAllOrientations(boolean[][] array) {
        int size = array.length;
        if (size != array[0].length) throw new RuntimeException("Not a square.");
        var arrays = new ArrayList<boolean[][]>();
        arrays.add(array);
        int inverseY, inverseX;
        boolean[][] alteredArray = new boolean[size][size]; // xFlip:
        for (int y = 0; y < size; y++) {
            inverseX = size;
            for (int x = 0; x < size; x++) alteredArray[y][x] = array[y][--inverseX];
        }
        addIfNew(alteredArray, arrays);
        alteredArray = new boolean[size][size]; //yFlip:
        for (int x = 0; x < size; x++) {
            inverseY = size;
            for (int y = 0; y < size; y++) alteredArray[y][x] = array[--inverseY][x];
        }
        addIfNew(alteredArray, arrays);
        alteredArray = new boolean[size][size]; // bottomLeftToTopRightAxisFlip:
        inverseY = size;
        for (int y = 0; y < size; y++) {
            inverseY--; inverseX = size;
            for (int x = 0; x < size; x++) alteredArray[--inverseX][inverseY] = array[y][x];
        }
        addIfNew(alteredArray, arrays);
        alteredArray = new boolean[size][size]; // topLeftToBottomRightAxisFlip:
        for (int y = 0; y < size; y++) for (int x = 0; x < size; x++) alteredArray[x][y] = array[y][x];
        addIfNew(alteredArray, arrays);
        alteredArray = new boolean[size][size]; // rotateRight1:
        inverseY = size;
        for (int y = 0; y < size; y++) {
            inverseY--;
            for (int x = 0; x < size; x++) alteredArray[x][inverseY] = array[y][x];
        }
        addIfNew(alteredArray, arrays);
        alteredArray = new boolean[size][size]; // rotateRight2:
        inverseY = size;
        for (int y = 0; y < size; y++) {
            inverseY--; inverseX = size;
            for (int x = 0; x < size; x++) {
                inverseX--;
                alteredArray[inverseY][inverseX] = array[y][x];
            }
        }
        addIfNew(alteredArray, arrays);
        alteredArray = new boolean[size][size]; // rotateRight3 (rotateLeft1):
        for (int y = 0; y < size; y++) {
            inverseX = size;
            for (int x = 0; x < size; x++) alteredArray[--inverseX][y] = array[y][x];
        }
        addIfNew(alteredArray, arrays);
        return arrays;
    }

    private static void addIfNew(boolean[][] array, ArrayList<boolean[][]> arrays) {
        for (var otherArray : arrays) if (Arrays.deepEquals(array, otherArray)) return;
        arrays.add(array);
    }

}
