package year2017.day7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Part1Only {
    static private final int DAY = 7;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        findBottomProgramName();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void findBottomProgramName() {
        ArrayList<String> programNames = new ArrayList<>();
        ArrayList<String> childProgramNames = new ArrayList<>();
        int spaceIndex;
        for (int i = 0; i < inputStrings.size(); i++) {
            String s = inputStrings.get(i);
            spaceIndex = s.indexOf(' ');
            programNames.add(s.substring(0, spaceIndex));
            int arrowIndex = s.indexOf('>');
            if (arrowIndex == -1) continue;
            String[] children = s.substring(arrowIndex+2).split(", ");
            childProgramNames.addAll(List.of(children));
        }
        programNames.removeAll(childProgramNames);
        if (programNames.size() > 1) throw new RuntimeException("Somehow more than one program was found to be parentless");
        System.out.println("\nName of bottom program (only program with no parent program): "+programNames.getFirst());
    }

}
