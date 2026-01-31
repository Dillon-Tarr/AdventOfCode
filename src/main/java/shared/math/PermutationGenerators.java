package shared.math;

import java.util.ArrayList;

public class PermutationGenerators {

    public static ArrayList<int[]> generatePermutations(int numberOfDigits){
        return getSubPermutations(numberOfDigits, new int[]{});
    }

    private static ArrayList<int[]> getSubPermutations(int neededDigitCount, int[] existingDigits) {
        ArrayList<int[]> subPermutations = new ArrayList<>();
        int existingDigitCount = existingDigits.length;
        int[] workingDigits = new int[existingDigitCount+1];
        for (int i = 0; i < existingDigitCount; i++) workingDigits[i] = existingDigits[i];
        boolean digitAlreadyUsed, thisIsTheLastDigit = existingDigitCount == neededDigitCount-1;
        for (int i = 0; i < neededDigitCount; i++) {
            digitAlreadyUsed = false;
            for (int d = 0; d < existingDigitCount; d++)
                if (existingDigits[d] == i) { digitAlreadyUsed = true; break; }
            if (!digitAlreadyUsed) {
                workingDigits[existingDigitCount] = i;
                if (!thisIsTheLastDigit) subPermutations.addAll(getSubPermutations(neededDigitCount, workingDigits));
                else subPermutations.add(workingDigits);
            }
        }
        return subPermutations;
    }

}
