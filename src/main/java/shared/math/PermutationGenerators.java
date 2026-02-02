package shared.math;

import java.util.ArrayList;

public class PermutationGenerators {

    /**
     * @param numberOfNumbers the number of numbers, counting up from 0, for which to generate all permutations
     * @return an ArrayList&lt;int[]> of all the permutations of the digits from 0 up to (not including) <code>numberOfNumbers</code>
     */
    public static ArrayList<int[]> generatePermutations(int numberOfNumbers){
        return getSubPermutations(numberOfNumbers, new int[]{});
    }

    private static ArrayList<int[]> getSubPermutations(int neededNumberCount, int[] existingNumbers) {
        ArrayList<int[]> subPermutations = new ArrayList<>();
        int existingNumberCount = existingNumbers.length;
        int[] workingNumbers = new int[existingNumberCount+1];
        for (int i = 0; i < existingNumberCount; i++) workingNumbers[i] = existingNumbers[i];
        if (existingNumberCount == neededNumberCount-1) nLoop: for (int n = 0; n < neededNumberCount; n++) {
            for (int i = 0; i < existingNumberCount; i++) if (existingNumbers[i] == n) continue nLoop;
            workingNumbers[existingNumberCount] = n;
            subPermutations.add(workingNumbers);
            break;
        } else nLoop: for (int n = 0; n < neededNumberCount; n++) {
            for (int i = 0; i < existingNumberCount; i++) if (existingNumbers[i] == n) continue nLoop;
            workingNumbers[existingNumberCount] = n;
            subPermutations.addAll(getSubPermutations(neededNumberCount, workingNumbers));
        }
        return subPermutations;
    }

}
