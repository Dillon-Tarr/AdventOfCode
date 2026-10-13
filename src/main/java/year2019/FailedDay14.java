package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// This file was an old attempt at the Day 14 puzzle, just part 1. Saving now in case I want to reference it when I make a proper solution.
// I got stuck and stopped trying.
// To actually do this right, I think I need to keep in mind:
// Almost every chemical is produced by exactly one reaction; the only exception, ORE, is the raw material input to the entire process and is not produced by a reaction.

class FailedDay14 {
    static private final int DAY = 14;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private final ArrayList<Reaction> allReactions = new ArrayList<>();
    static private final HashMap<String, Reaction> reactionsByOutputType = new HashMap<>();
    static private final HashMap<String, Integer> chemicalToTierMap = new HashMap<>();
    static private final HashMap<String, ArrayList<String>> outputToInputTypesMap = new HashMap<>();
    static private final HashMap<String, HashSet<String>> outputToUpstreamTypesMap = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        tierChemicals();
        recordUpstreamChemicals();
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
        var set = new HashSet<String>();
        for (var s : inputRows) {
            var ss = s.split(" => ");
            var outPart = ss[1].split(" ");
            var outputType = outPart[1];
            if (!set.add(outputType)) throw new RuntimeException("The problem is more complex than originally thought.");
            var output = new ReactionPart(outputType, Integer.parseInt(outPart[0]));
            var inParts = ss[0].split(", ");
            var inputs = new ReactionPart[inParts.length];
            var inputTypes = new ArrayList<String>();
            for (int i = 0; i < inputs.length; i++) {
                var inPart = inParts[i].split(" "); var inputType = inPart[1];
                inputs[i] = new ReactionPart(inputType, Integer.parseInt(inPart[0]));
                inputTypes.add(inputType);
            }
            outputToInputTypesMap.put(outputType, inputTypes);
            Reaction r = new Reaction(inputs, output);
            allReactions.add(r);
            reactionsByOutputType.put(outputType, r);
        }
    }

    private static void tierChemicals() {
        chemicalToTierMap.put("ORE", 0);
        var untieredOutputs = new ArrayDeque<>(reactionsByOutputType.values());
        while (!untieredOutputs.isEmpty()) {
            var reaction = untieredOutputs.remove();
            var outputType = reaction.output.type;
            int highestTier = 0;
            for (var input : reaction.inputs) {
                Integer inputTier = chemicalToTierMap.get(input.type);
                if (inputTier == null) { untieredOutputs.add(reaction); break; }
                if (inputTier > highestTier) highestTier = inputTier;
            }
            chemicalToTierMap.put(outputType, highestTier+1);
        }
    }

    private static void recordUpstreamChemicals() {
        var chemicalNameList = new ArrayList<String>();
        for (var reaction : allReactions) chemicalNameList.add(reaction.output.type);
        for (var outputChemicalName : chemicalNameList) outputToUpstreamTypesMap.put(outputChemicalName, new HashSet<>());
        for (var outputChemicalName : chemicalNameList) { // populate outputToUpstreamTypesMap
            var inputTypes = outputToInputTypesMap.get(outputChemicalName);
            var visited = new HashSet<>(inputTypes);
            var stack = new ArrayDeque<>(inputTypes);
            while (!stack.isEmpty()) {
                String type = stack.removeLast();
                var typeInputs = outputToUpstreamTypesMap.get(type);
                if (visited.add(type) && typeInputs != null) stack.addAll(typeInputs);
            }
            outputToUpstreamTypesMap.put(outputChemicalName, visited);
        }
        outputToUpstreamTypesMap.put("ORE", new HashSet<>());
    }

    private static void solve() {
        var neededAmounts = new HashMap<String, Integer>();
        neededAmounts.put("FUEL", 1);
        var queue = new PriorityQueue<>(new ElementOrderComparator());
        queue.add("FUEL");
        // This queue needs to always select the MOST DOWNSTREAM (i.e. the element with nothing downstream of it in queue).

        var seenOutputTypes = new HashSet<String>();
        while (!queue.isEmpty()) {
            var outputType = queue.remove();
            if (!seenOutputTypes.add(outputType))
                System.out.println("Saw output type again: "+outputType);
            if (outputType.equals("ORE")) break;
            var outputAmount = neededAmounts.get(outputType);
            var reaction = reactionsByOutputType.get(outputType);
            int perOutputVolume = reaction.output.volume;
            int numberOfReactions = getNumberOfReactionsNeeded(outputAmount, perOutputVolume);
            var inputs = reaction.inputs;
            for (var input : inputs) {
                var inputType = input.type;
                int additionalNeededInputVolume = numberOfReactions * input.volume;
                neededAmounts.put(inputType, neededAmounts.getOrDefault(inputType, 0) + additionalNeededInputVolume);
                if (!queue.contains(inputType)) queue.add(inputType);
                if (inputType.equals("ORE"))
                    System.out.println("Ore needed to produce "+outputAmount+" "+outputType+": "+additionalNeededInputVolume);
            }
        }
        int oreUsed = neededAmounts.get("ORE");
        System.out.println("\nOre used: "+oreUsed);
        // too high: 27_624_821, 22_384_775
    }

    private static int getNumberOfReactionsNeeded(int neededAmount, int perReactionAmount) {
        int reactionsNeeded = neededAmount / perReactionAmount;
        if (neededAmount != reactionsNeeded * perReactionAmount) reactionsNeeded++;
        return reactionsNeeded;
    }

    private static class ElementOrderComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            if (s1.equals(s2)) return 0;
            int tier1 = chemicalToTierMap.get(s1);
            int tier2 = chemicalToTierMap.get(s2);
            if (tier1 != tier2) return Integer.compare(tier2, tier1);
            if (outputToUpstreamTypesMap.get(s1).contains(s2)) return -1;
            if (outputToUpstreamTypesMap.get(s2).contains(s1)) return 1;
            return 0;
        }
    }

    private record ReactionPart(String type, int volume) {
        @Override
        public String toString() { return type+"x"+ volume; }
    }

    private record Reaction(ReactionPart[] inputs, ReactionPart output) {
        @Override
        public String toString() {
            return "Reaction{inputs:"+Arrays.deepToString(inputs)+"; output:["+output+"]}";
        }
    }

}
