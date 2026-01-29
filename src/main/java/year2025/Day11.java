package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Day11 {
    static private final int DAY = 11;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final HashMap<String, ArrayList<String>> keyToValsMap = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        var inputStrings = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (var s : inputStrings) {
            var split = s.split(": ");
            String key = split[0]; String[] vals = split[1].split(" ");
            keyToValsMap.put(key, new ArrayList<>(List.of(vals)));
        }
    }

    private static void solvePart1() {
        int count = 0;
        var stack = new ArrayList<>(keyToValsMap.get("you"));
        while (!stack.isEmpty()) { var s = stack.removeLast(); if (s.equals("out")) count++; else stack.addAll(keyToValsMap.get(s)); }
        System.out.println("\nPart 1 path count: "+count);
    }

    private static void solvePart2() {
        var keyToDownstreamValsMap = new HashMap<String, ArrayList<String>>();
        var keyList = new ArrayList<>(keyToValsMap.keySet());
        for (var key : keyList) { // populate keyToDownstreamNodesMap
            var visited = new HashSet<String>(); visited.add(key);
            var stack = new ArrayDeque<>(keyToValsMap.get(key));
            while (!stack.isEmpty()) {
                String current = stack.removeLast();
                var vals = keyToValsMap.get(current);
                if (visited.add(current) && vals != null) stack.addAll(vals);
            }
            visited.remove(key);
            keyToDownstreamValsMap.put(key, new ArrayList<>(visited));
        } // determine which of dac and fft is first/second:
        String first, second;
        if (keyToDownstreamValsMap.get("dac").contains("fft")) { first = "dac"; second = "fft"; } else { first = "fft"; second = "dac"; }
        HashSet<String> keysFromSvrToFirst = new HashSet<>(), keysFromFirstToSecond = new HashSet<>();
        for (var key : keyList) { // group keys by segment of journey:
            var downstreamSet = keyToDownstreamValsMap.get(key);
            if (downstreamSet.contains(first)) keysFromSvrToFirst.add(key);
            else if (downstreamSet.contains(second)) keysFromFirstToSecond.add(key);
        }
        long svrToFirstCount = 0, firstToSecondCount = 0, secondToOutCount = 0;
        var stack = new ArrayDeque<String>(); stack.add("svr");
        String s; // get number of paths from svr to first:
        while (!stack.isEmpty()) {
            s = stack.removeLast();
            if (s.equals(first)) svrToFirstCount++; else if (keysFromSvrToFirst.contains(s)) stack.addAll(keyToValsMap.get(s));
        } // get number of paths from first to second:
        stack.add(first);
        while (!stack.isEmpty()) {
            s = stack.removeLast();
            if (s.equals(second)) firstToSecondCount++; else if (keysFromFirstToSecond.contains(s)) stack.addAll(keyToValsMap.get(s));
        } // get number of paths from second to out:
        stack.add(second);
        while (!stack.isEmpty()) {
            s = stack.removeLast();
            if (s.equals("out")) secondToOutCount++; else stack.addAll(keyToValsMap.get(s));
        }
        System.out.println("\n\t  Part 2 path: svr > " +first+" > "+second+" > out"+
                "\nKey count per section: "+ keysFromSvrToFirst.size()+", "+keysFromFirstToSecond.size()+", "+keyToDownstreamValsMap.get(second).size()+
                "\nPath count per section: "+svrToFirstCount+", "+firstToSecondCount+", "+secondToOutCount+
                "\nNumber of paths from svr to out which pass through dac and fft (part 2 answer): "+(svrToFirstCount*firstToSecondCount*secondToOutCount));
    }

}
