package year2015.day13;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Part2 {
    static private final int DAY = 13;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final HashMap<String, Integer> peopleMap = new HashMap<>();
    static private final HashMap<Integer, HashMap<Integer, Integer>> pairToIndividualHappinessMap = new HashMap<>();
    static private final HashMap<Integer, HashMap<Integer, Integer>> pairToNetHappinessMap = new HashMap<>();

    static private int greatestTotalHappinessEncountered = Integer.MIN_VALUE;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        mapPeopleToNumbers();
        mapIndividualHappinessData();
        mapNetHappinessData();
        getAndTestPossibilities();

        System.out.println("Greatest possible net happiness change: "+ greatestTotalHappinessEncountered);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                inputStrings.add(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void mapPeopleToNumbers() {
            for (String inputString : inputStrings) {
                peopleMap.putIfAbsent(inputString.substring(0, inputString.indexOf(' ')), peopleMap.size());
            }
    }

    private static void mapIndividualHappinessData() {
        int firstSpaceIndex, hapIndex, lastSpaceIndex, person1, person2;
        String possibleNegativityString, numberString;
        for (String inputString : inputStrings) {
            firstSpaceIndex = inputString.indexOf(' ');
            hapIndex = inputString.indexOf("hap");
            lastSpaceIndex = inputString.lastIndexOf(' ');

            person1 = peopleMap.get(inputString.substring(0, firstSpaceIndex));
            possibleNegativityString = inputString.charAt(firstSpaceIndex + 7) == 'g' ? "" : "-";
            numberString = inputString.substring(firstSpaceIndex + 12, hapIndex - 1);
            person2 = peopleMap.get(inputString.substring(lastSpaceIndex + 1, inputString.length() - 1));

            pairToIndividualHappinessMap.putIfAbsent(person1, new HashMap<>());
            pairToNetHappinessMap.putIfAbsent(person1, new HashMap<>());
            pairToIndividualHappinessMap.get(person1).putIfAbsent(person2, Integer.parseInt(possibleNegativityString+numberString));
        }
    }

    private static void mapNetHappinessData() {
        int netHappiness;
        HashMap<Integer, Integer> p1NetMap, p2NetMap, p1IndMap, p2IndMap;
        for (int p1 = 0; p1 < peopleMap.size(); p1++) {
            for (int p2 = 0; p2 < peopleMap.size(); p2++) {
                if (p1 == p2) continue;
                p1NetMap = pairToNetHappinessMap.get(p1);
                if (p1NetMap.containsKey(p2)) continue;
                p2NetMap = pairToNetHappinessMap.get(p2);
                p1IndMap = pairToIndividualHappinessMap.get(p1);
                p2IndMap = pairToIndividualHappinessMap.get(p2);
                netHappiness = p1IndMap.get(p2)+p2IndMap.get(p1);
                p1NetMap.put(p2, netHappiness);
                p2NetMap.put(p1, netHappiness);
            }
        }
    }

    private static void getAndTestPossibilities() {
        ArrayList<String> possibilities = PossibilityChecker.getMirrorlessPossibilities(peopleMap.size());
        for (String s : possibilities) {
            int netHappiness = 0;
            char c1 , c2 = s.charAt(0);
            for (int i = 1; i < s.length(); i++) {
                c1 = c2;
                c2 = s.charAt(i);
                netHappiness += pairToNetHappinessMap.get(c1-'0').get(c2-'0');
            }
            if (netHappiness > greatestTotalHappinessEncountered) greatestTotalHappinessEncountered = netHappiness;
        }
    }

}
