package year2017.day7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class BothParts {
    static private final int DAY = 7;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private String bottomProgramName;
    static private final HashMap<String, Program> nameToProgramMap = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        findBottomProgramNameAndInstantiatePrograms(); // part 1 answer
        connectPrograms(); // needed for part 2 solve
        findCorrectedWeightOfIncorrectProgram(); // part 2 answer

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
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

    private static void findBottomProgramNameAndInstantiatePrograms() {
        ArrayList<String> programNames = new ArrayList<>();
        ArrayList<String> childProgramNames = new ArrayList<>();
        int spaceIndex, closeIndex, weight;
        String name;
        for (int i = 0; i < inputStrings.size(); i++) {
            String s = inputStrings.get(i);
            spaceIndex = s.indexOf(' '); closeIndex = s.indexOf(')', spaceIndex+2);
            name = s.substring(0, spaceIndex); weight = Integer.parseInt(s.substring(spaceIndex+2, closeIndex));
            programNames.add(name);
            int arrowIndex = s.indexOf('>');
            if (arrowIndex == -1) {
                nameToProgramMap.put(name, new Program(name, weight));
                continue;
            }
            String[] children = s.substring(arrowIndex+2).split(", ");
            childProgramNames.addAll(List.of(children));
            nameToProgramMap.put(name, new Program(name, weight, children));
        }
        programNames.removeAll(childProgramNames);
        if (programNames.size() > 1) throw new RuntimeException("Somehow more than one program was found to be parentless");
        bottomProgramName = programNames.getFirst();
        System.out.println("\nName of bottom program (only program with no parent program, part 1 answer): "+bottomProgramName);
    }

    private static void connectPrograms() {
        Program parent, child;
        String[] childNames;
        for (HashMap.Entry<String, Program> entry : nameToProgramMap.entrySet()) {
            parent = entry.getValue();
            childNames = parent.childNames;
            if (childNames == null) continue;
            for (int i = 0; i < childNames.length; i++) {
                child = nameToProgramMap.get(childNames[i]);
                parent.children[i] = child;
                child.parent = parent;
            }
        }
    }

    private static void findCorrectedWeightOfIncorrectProgram() {
        Program program = nameToProgramMap.get(bottomProgramName);
        program.findCombinedWeightOfSelfAndAllChildren();
        int resultIndex = program.getWrongChildIndex(), wrongChildIndex = -1;
        while (resultIndex != -1) {
            program = program.children[resultIndex];
            wrongChildIndex = resultIndex;
            resultIndex = program.getWrongChildIndex();
        }
        int correctedWeight = calculateCorrectedWeight(program, wrongChildIndex);
        System.out.println("\nCorrected weight of incorrectly weighted program (part 2 answer): "+correctedWeight);
    }

    private static int calculateCorrectedWeight(Program program, int wrongChildIndex) {
        Program[] children = program.parent.children;
        int rightCombinedWeight, wrongCombinedWeight;
        if (wrongChildIndex == 0) {
            rightCombinedWeight = children[1].combinedWeight;
            wrongCombinedWeight = children[0].combinedWeight;
        } else {
            rightCombinedWeight = children[0].combinedWeight;
            wrongCombinedWeight = children[wrongChildIndex].combinedWeight;
        }
        return program.weight + (rightCombinedWeight-wrongCombinedWeight);
    }

    private static class Program {
        Program parent = null;
        String name;
        int weight, combinedWeight;
        String[] childNames;
        Program[] children;

        private Program(String name, int weight) {
            this.name = name;
            this.weight = weight;
            childNames = null;
            children = null;
        }

        Program(String name, int weight, String[] childNames) {
            this.name = name;
            this.weight = weight;
            this.childNames = childNames;
            children = new Program[childNames.length];
        }

        private int findCombinedWeightOfSelfAndAllChildren() {
            if (children == null) {
                combinedWeight = weight;
                return weight;
            }
            int sum = weight;
            for (int i = 0; i < children.length; i++) {
                sum += children[i].findCombinedWeightOfSelfAndAllChildren();
            }
            combinedWeight = sum;
            return sum;
        }

        private int getWrongChildIndex() {
            Program[] children = this.children;
            if (children == null || children.length < 3) return -1;
            int a = children[0].combinedWeight, b = children[1].combinedWeight;
            if (a != b) return children[2].combinedWeight == b ? 0 : 1;
            for (int i = 2; i < children.length; i++) if (children[i].combinedWeight != a) return i;
            return -1;
        }

    }

}
