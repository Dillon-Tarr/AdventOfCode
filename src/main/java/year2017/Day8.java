package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Day8 {
    static private final int DAY = 8;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final HashMap<String, Integer> registerNameToValueMap = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        executeInstructions();
        findGreatestRegisterValueAfterRunningAllInstructions();

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

    private static void executeInstructions() {
        int greatestSeen = Integer.MIN_VALUE;
        String[] words;
        String changeRegister, conditionRegister;
        Integer conditionRegisterValue, changeRegisterValue;
        int compareValue, changeAmount, newValue;
        for (String s : inputStrings) {
            words = s.split(" ");
            conditionRegister = words[4];
            conditionRegisterValue = registerNameToValueMap.putIfAbsent(conditionRegister, 0);
            if (conditionRegisterValue == null) conditionRegisterValue = 0;
            compareValue = Integer.parseInt(words[6]);
            switch (words[5]) {
                case ">" -> { if (conditionRegisterValue <= compareValue) continue; }
                case ">=" -> { if (conditionRegisterValue < compareValue) continue; }
                case "<" -> { if (conditionRegisterValue >= compareValue) continue; }
                case "<=" -> { if (conditionRegisterValue > compareValue) continue; }
                case "==" -> { if (conditionRegisterValue != compareValue) continue; }
                case "!=" -> { if (conditionRegisterValue == compareValue) continue; }
                default -> throw new RuntimeException("Input is invalid or we missed comparison type. s:  "+s);
            }
            changeRegister = words[0];
            changeAmount = Integer.parseInt(words[2]);
            changeRegisterValue = registerNameToValueMap.putIfAbsent(changeRegister, 0);
            if (changeRegisterValue == null) changeRegisterValue = 0;
            if (words[1].charAt(0) == 'i') {
                newValue = changeRegisterValue+changeAmount;
                if (newValue > greatestSeen) greatestSeen = newValue;
            } else newValue = changeRegisterValue-changeAmount;
            registerNameToValueMap.put(changeRegister, newValue);
        }
        System.out.println("\nGreatest value held in any register at any point during execution of instructions (part 2 answer): "+ greatestSeen);
    }

    private static void findGreatestRegisterValueAfterRunningAllInstructions() {
        int greatestSeen = Integer.MIN_VALUE, value;
        for (Map.Entry<String, Integer> entry : registerNameToValueMap.entrySet()) {
            value = entry.getValue();
            if (value > greatestSeen) greatestSeen = value;
        }
        System.out.println("\nGreatest value held in any register AFTER COMPLETING input instructions (part 1 answer): "+ greatestSeen);
    }

}
