package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class Day12 {
    static private final int DAY = 12;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final ArrayList<HashSet<Integer>> allGroups = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solve();

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

    private static void solve() {
        ArrayList<String> idStrings; HashSet<Integer> ids;
        for (String s : inputStrings) {
            idStrings = new ArrayList<>(); idStrings.add(s.substring(0, s.indexOf(' '))); // left id
            idStrings.addAll(List.of(s.substring(s.indexOf('>')+2).split(", "))); // right-side id(s)
            ids = new HashSet<>(); for (String idString : idStrings) ids.add(Integer.parseInt(idString));
            sortIDsIntoGroups(ids);
        }
        System.out.println("\nAll groups: "+allGroups);
        int size = -1;
        for (var group : allGroups) if (group.contains(0)) { size = group.size(); break; }
        System.out.println("\nSize of group containing ID 0 (part 1 answer): "+size);
        System.out.println("\nNumber of groups (part 2 answer): "+allGroups.size());
    }

    private static void sortIDsIntoGroups(HashSet<Integer> elements) {
        HashSet<Integer> firstMatch = null;
        ArrayList<HashSet<Integer>> groupsToMergeIntoFirstMatch = new ArrayList<>();
        for (var group : allGroups) for (Integer element : elements) {
            if (group.contains(element)) {
                if (firstMatch == null) { firstMatch = group; break; }
                else groupsToMergeIntoFirstMatch.add(group);
            }
        }
        if (firstMatch == null) { allGroups.add(elements); return; }
        firstMatch.addAll(elements);
        for (var group : groupsToMergeIntoFirstMatch) {
            allGroups.remove(group);
            firstMatch.addAll(group);
        }
    }

}
