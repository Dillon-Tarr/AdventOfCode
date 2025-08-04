package year2016.day7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part2 {
    static private final int DAY = 7;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> IPAddresses = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        countIPsWhichSupportTLS();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                IPAddresses.add(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void countIPsWhichSupportTLS() {
        int count = 0;
        boolean identifiedCurrentFindType, foundMatch;
        char iChar;
        ArrayList<Integer> allFinds = new ArrayList<>();
        ArrayList<Integer> outsideBracketsFinds = new ArrayList<>();
        ArrayList<Integer> insideBracketsFinds = new ArrayList<>();
        for (String address : IPAddresses) {
            allFinds.clear();
            insideBracketsFinds.clear();
            outsideBracketsFinds.clear();
            for (int i = 2; i < address.length(); i++) { // find aba/bab indices
                iChar = address.charAt(i);
                if (iChar == '[' || iChar == ']' || iChar == address.charAt(i-1)) continue;
                if (iChar == address.charAt(i-2)) allFinds.add(i-2);
            }
            for (int findIndex : allFinds) { // group finds by whether in brackets
                identifiedCurrentFindType = false;
                for (int i = findIndex; i >= 0; i--) { // search backward for brackets
                    switch (address.charAt(i)) {
                        case '[' -> {identifiedCurrentFindType = true; insideBracketsFinds.add(findIndex);}
                        case ']' -> {identifiedCurrentFindType = true; outsideBracketsFinds.add(findIndex);}
                    }
                    if (identifiedCurrentFindType) break;
                }
                if (!identifiedCurrentFindType)
                    for (int i = findIndex+3; i < address.length(); i++) { // search forward for brackets
                        switch (address.charAt(i)) {
                            case ']' -> {identifiedCurrentFindType = true; insideBracketsFinds.add(findIndex);}
                            case '[' -> {identifiedCurrentFindType = true; outsideBracketsFinds.add(findIndex);}
                        }
                        if (identifiedCurrentFindType) break;
                    }
                if (!identifiedCurrentFindType) throw new RuntimeException("ABA found with no brackets in String??");
            }
            foundMatch = false;
            for (int abaIndex : outsideBracketsFinds) {
                for (int babIndex : insideBracketsFinds) {
                    if (address.charAt(abaIndex) == address.charAt(babIndex+1) && address.charAt(babIndex) == address.charAt(abaIndex+1)) {
                        foundMatch = true;
                        break;
                    }
                }
                if (foundMatch) break;
            }
            if (foundMatch) count++;
        }
        System.out.println("\nNumber of IPs which support SSL: "+count);
    }

}
