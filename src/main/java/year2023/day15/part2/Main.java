package year2023.day15.part2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static private final int DAY = 15;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private String[] steps;
    static private final List<ArrayList<String>> boxes = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        instantiateBoxes();
        fillBoxes();
        getFocusingPowerOfAllLenses();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            StringBuilder sb = new StringBuilder();
            String currentLine = br.readLine();
            while(!(currentLine == null)) {
                sb.append(currentLine);
                currentLine = br.readLine();
            }
            steps = sb.toString().split(",");
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void instantiateBoxes() {
        for (int i = 0; i < 256; i++) boxes.add(new ArrayList<>());
    }

    private static void fillBoxes() {
        for (String step : steps) {
            char operator; int operatorIndex;
            if (step.contains("-")) operator = '-';
            else operator = '=';
            operatorIndex = step.indexOf(operator);

            String lensLabel = step.substring(0, operatorIndex);
            int boxNumber = getBoxNumberUsingHashAlgorithm(lensLabel);
            ArrayList<String> box = boxes.get(boxNumber);

            if (operator == '=') {
                int focalLength = Character.getNumericValue(step.charAt(operatorIndex+1));
                String lens = lensLabel+" "+focalLength;
                boolean existingSameLabelLensFound = false;
                for (int i = 0; i < box.size(); i++) {
                    if (box.get(i).contains(lensLabel)) {
                        box.set(i, lens);
                        existingSameLabelLensFound = true;
                        break;
                    }
                }
                if (!existingSameLabelLensFound) box.add(lens);
            } else {
                for (int i = 0; i < box.size(); i++) {
                    if (box.get(i).contains(lensLabel)) {box.remove(i); break;}
                }
            }
        }
    }

    private static int getBoxNumberUsingHashAlgorithm(String encodedBoxNumber) {
        int value = 0;
        for (int i = 0; i < encodedBoxNumber.length(); i++) {
            value = (value+encodedBoxNumber.charAt(i))*17%256;
        }
        return value;
    }

    private static void getFocusingPowerOfAllLenses() {
        int sumOfFocusingPowers = 0;
        for (int realBoxNumber = 1; realBoxNumber < boxes.size()+1; realBoxNumber++) {
            ArrayList<String> box = boxes.get(realBoxNumber-1);
            for (int realLensNumber = 1; realLensNumber < box.size()+1; realLensNumber++) {
                String lens = box.get(realLensNumber-1);
                int lensFocalLength = Character.getNumericValue(lens.charAt(lens.length()-1));
                sumOfFocusingPowers += realBoxNumber*realLensNumber*lensFocalLength;
            }
        }
        System.out.println("\nSum of focusing powers:\n\n"+sumOfFocusingPowers);
    }

}
