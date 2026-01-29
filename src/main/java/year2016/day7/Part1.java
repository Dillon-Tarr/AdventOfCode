package year2016.day7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part1 {
    static private final int DAY = 7;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> IPAddresses = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        countIPsWhichSupportTLS();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
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
        boolean foundAbbaInBrackets, foundAbbaOutsideBrackets, identifiedCurrentFindType;
        char iChar, iMinusOneChar;
        ArrayList<Integer> indicesOfAbbaFinds = new ArrayList<>();
        for (String address : IPAddresses) {
            indicesOfAbbaFinds.clear();
            for (int i = 3; i < address.length(); i++) {
                iChar = address.charAt(i);
                iMinusOneChar = address.charAt(i-1);
                if (iChar == '[' || iChar == ']' || iChar == iMinusOneChar) continue;
                if (iChar == address.charAt(i-3) && iMinusOneChar == address.charAt(i-2)) indicesOfAbbaFinds.add(i-3);
            }
            foundAbbaInBrackets = false; foundAbbaOutsideBrackets = false;
            for (int index : indicesOfAbbaFinds) {
                identifiedCurrentFindType = false;
                for (int i = index; i >= 0; i--) { // search backward for brackets
                    switch (address.charAt(i)) {
                        case '[' -> {identifiedCurrentFindType = true; foundAbbaInBrackets = true;}
                        case ']' -> {identifiedCurrentFindType = true; foundAbbaOutsideBrackets = true;}
                    }
                    if (identifiedCurrentFindType) break;
                }
                if (foundAbbaInBrackets) break;
                if (identifiedCurrentFindType) continue;
                for (int i = index+4; i < address.length(); i++) { // search forward for brackets
                    switch (address.charAt(i)) {
                        case ']' -> {identifiedCurrentFindType = true; foundAbbaInBrackets = true;}
                        case '[' -> {identifiedCurrentFindType = true; foundAbbaOutsideBrackets = true;}
                    }
                    if (identifiedCurrentFindType) break;
                }
                if (!identifiedCurrentFindType) throw new RuntimeException("ABBA found with no brackets in String??");
                if (foundAbbaInBrackets) break;
            } // After identifying all find types or finding one in brackets:
            if (!foundAbbaInBrackets && foundAbbaOutsideBrackets) count++;
        }
        System.out.println("\nNumber of IPs which support TLS: "+count);
    }

}
