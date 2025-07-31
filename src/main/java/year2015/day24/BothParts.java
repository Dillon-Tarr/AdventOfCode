package year2015.day24;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class BothParts {
    static private final int DAY = 24;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final ArrayList<Integer> presentWeights = new ArrayList<>(28);
    static private int neededPerGroupWeight, numberOfGroups;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        numberOfGroups = args.length == 0 ? 3 : 4; // No args for part 1 (3 groups), otherwise part 2 (4 groups).

        getPresentWeights();
        testConfigurations();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getPresentWeights() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            ArrayList<Integer> leastToGreatestPresentWeights = new ArrayList<>();
            int sumOfAllPresentWeights = 0, presentWeight;
            while (inputString != null) {
                presentWeight = Integer.parseInt(inputString);
                leastToGreatestPresentWeights.add(presentWeight);
                sumOfAllPresentWeights += presentWeight;
                inputString = br.readLine();
            }
            if (sumOfAllPresentWeights % numberOfGroups != 0) throw new RuntimeException("Dude doesn't know how to design a puzzle if you see this.");
            leastToGreatestPresentWeights.sort(Integer::compareTo);
            for (int i = leastToGreatestPresentWeights.size()-1; i >= 0; i--) presentWeights.add(leastToGreatestPresentWeights.get(i));
            System.out.println("\nPresent weights: "+presentWeights);
            System.out.println("Sum of all present weights: "+sumOfAllPresentWeights);
            System.out.println("Number of groups: "+numberOfGroups);
            neededPerGroupWeight = sumOfAllPresentWeights/numberOfGroups;
            System.out.println("Needed per-group weight: "+ neededPerGroupWeight);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void testConfigurations() {
        int lowestValidCount = presentWeights.size();
        long lowestValidProduct = Long.MAX_VALUE;
        ArrayList<Integer> frontGroup;
        int frontGroupWeightsSum, frontGroupSize, presentWeight;
        long frontGroupWeightsProduct;

        for (int intAsBinary = 1; intAsBinary < 1<<presentWeights.size(); intAsBinary++) {
            frontGroupSize = 0;
            for (int i = 0; i < presentWeights.size(); i++) if ((intAsBinary & (1<<i)) != 0) frontGroupSize++;
            if (frontGroupSize > lowestValidCount) continue;

            frontGroup = new ArrayList<>(lowestValidCount);
            frontGroupWeightsSum = 0;
            for (int i = 0; i < presentWeights.size(); i++) if ((intAsBinary & (1<<i)) != 0) {
                presentWeight = presentWeights.get(i);
                frontGroup.add(presentWeight);
                frontGroupWeightsSum += presentWeight;
            }
            if (frontGroupWeightsSum != neededPerGroupWeight) continue;
            if (!checkIfRemainingPresentsCanBeEvenlyDividedByWeight(frontGroup)) continue;
            frontGroupWeightsProduct = 1;
            for (int present : frontGroup) frontGroupWeightsProduct*=present;
            if (frontGroupSize < lowestValidCount) {
                lowestValidCount = frontGroupSize;
                lowestValidProduct = frontGroupWeightsProduct;
            } else if (frontGroupWeightsProduct < lowestValidProduct) {
                lowestValidProduct = frontGroupWeightsProduct;
            }
        }
        System.out.println("Least number of front group presents possible: "+lowestValidCount);
        System.out.println("Least possible product of front group weights with least number of presents in front group: "+lowestValidProduct);
    }

    private static boolean checkIfRemainingPresentsCanBeEvenlyDividedByWeight (ArrayList<Integer> alreadyAccountedForPresents) {
        ArrayList<Integer> remainingPresents = new ArrayList<>(presentWeights);
        remainingPresents.removeAll(alreadyAccountedForPresents);
        int groupWeightsSum;
        for (int intAsBinary = 1; intAsBinary < 1<< remainingPresents.size(); intAsBinary++) {
            groupWeightsSum = 0;
            for (int i = 0; i < remainingPresents.size(); i++)
                if ((intAsBinary & (1<<i)) != 0) {
                    groupWeightsSum += remainingPresents.get(i);
                    if (groupWeightsSum > neededPerGroupWeight) break;
                }
            if (groupWeightsSum == neededPerGroupWeight) {
                if (numberOfGroups == 3) return true;
                else { // numberOfGroups == 4
                    ArrayList<Integer> remainingRemainingPresents = new ArrayList<>(remainingPresents);
                    for (int i = 0; i < remainingPresents.size(); i++)
                        if ((intAsBinary & (1<<i)) != 0) remainingRemainingPresents.remove(remainingPresents.get(i));
                    for (int intAsBinary2 = 1; intAsBinary2 < 1<< remainingPresents.size(); intAsBinary2++) {
                        groupWeightsSum = 0;
                        for (int i = 0; i < remainingRemainingPresents.size(); i++)
                            if ((intAsBinary2 & (1<<i)) != 0) {
                                groupWeightsSum += remainingPresents.get(i);
                                if (groupWeightsSum > neededPerGroupWeight) break;
                            }
                        if (groupWeightsSum == neededPerGroupWeight) return true;
                    }
                }
            }
        }
        return false;
    }

}
