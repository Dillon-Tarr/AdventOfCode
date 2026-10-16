package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day14 {
    static private final int DAY = 14;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private final ArrayList<Reaction> reactionsList = new ArrayList<>();
    static private final HashMap<String, Reaction> outputTypeToReactionMap = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        var inputRows = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var s = br.readLine();
            while (s != null) {
                inputRows.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (var row : inputRows) {
            var rowSplit = row.split(" => "); // inputs => output
            var inPairs = rowSplit[0].split(", ");
            var inputs = new ChemAmountPair[inPairs.length];
            for (int i = 0; i < inPairs.length; i++) {
                var pair = inPairs[i].split(" ");
                inputs[i] = new ChemAmountPair(pair[1], Integer.parseInt(pair[0]));
            } // inputs processed into pairs
            var outPair = rowSplit[1].split(" ");
            var outputName = outPair[1];
            var output = new ChemAmountPair(outputName, Integer.parseInt(outPair[0])); // output processed
            var reaction =  new Reaction(inputs, output);
            reactionsList.add(reaction);
            outputTypeToReactionMap.put(outputName, reaction);
        }
    }

    private static void solve() {
        var chemicalNameToInDegree = new HashMap<String, Integer>();
        for (var reaction : reactionsList) chemicalNameToInDegree.put(reaction.output.name, 0); // add every output
        for (var reaction : reactionsList) { // check initial in-degrees, i.e. direct dependency count -- how many outputs does this chemical feed?
            for (var input : reaction.inputs) {
                var name = input.name;
                if (name.equals("ORE")) continue; // No need to check for in-degree for ORE, as it is not produced.
                chemicalNameToInDegree.compute(name, (_,v) -> {
                    if (v == null) throw new RuntimeException("This should not be possible! All but ORE should be an output.");
                    return v + 1;
                });
            }
        }
        var topologicallySortedList = new ArrayList<String>();
        var queue = new LinkedList<String>(); // queue for Khan's algorithm, applicable to this directed acyclic graph (DAG)
        queue.add("FUEL"); // starts off with zero in-degree, since FUEL is not an input for any other chemicals
        while (!queue.isEmpty()) {
            var outputName = queue.remove();
            topologicallySortedList.add(outputName);
            var inChemicals = outputTypeToReactionMap.get(outputName).inputs;
            for (var input : inChemicals) {
                var name = input.name;
                if (name.equals("ORE")) continue; // ORE is never produced.
                int inDegree = chemicalNameToInDegree.get(name) - 1;
                if (inDegree > 0) chemicalNameToInDegree.put(name, inDegree);
                else {
                    chemicalNameToInDegree.remove(name);
                    queue.add(name);
                }
            }
        }
        // Now we have a full topologically sorted list. So let's go through the list and calculate needed ORE amount.
        var chemDemand = new HashMap<String, Integer>();
        chemDemand.put("FUEL", 1);
        for (var outputName : topologicallySortedList) {
            var reaction = outputTypeToReactionMap.get(outputName);
            int reactionCountNeeded = Math.ceilDiv(chemDemand.get(outputName), reaction.output.count);
            for (var input : reaction.inputs) {
                var name = input.name;
                chemDemand.put(name, chemDemand.getOrDefault(name, 0) + reactionCountNeeded * input.count);
            }
        }
        System.out.println("ORE demand for 1 FUEL (part 1 answer): "+chemDemand.get("ORE"));
    }

    private record Reaction(ChemAmountPair[] inputs, ChemAmountPair output) {}

    private record ChemAmountPair(String name, int count) {}

}
