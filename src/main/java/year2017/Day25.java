package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Day25 {
    static private final int DAY = 25;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private char initialStateChar;
    static private int specifiedStepCount, currentPosition = 0;
    static private final ArrayList<String[]> inputBlocks = new ArrayList<>();
    static private final HashMap<Character, State> states = new HashMap<>();
    static private State currentState;
    static private final HashSet<Integer> setPositions = new HashSet<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        processInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine(); initialStateChar = s.charAt(15);
            s = br.readLine(); specifiedStepCount = Integer.parseInt(s.substring(36, s.lastIndexOf(' ')));
            br.readLine(); s = br.readLine();
            String[] stateInfo = new String[9]; int index = 0;
            while (s != null) {
                if (s.isEmpty()) {
                    inputBlocks.add(stateInfo);
                    stateInfo = new String[9];
                    index = 0;
                } else stateInfo[index++] = s;
                s = br.readLine();
            }
            inputBlocks.add(stateInfo);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processInputData() {
        char stateChar, nextState0, nextState1;
        boolean lineOneIsUnset, newVal0, newVal1, goRight0, goRight1;
        for (String[] info : inputBlocks) {
            stateChar = info[0].charAt(9);
            lineOneIsUnset = info[1].charAt(26) == '0';
            if (lineOneIsUnset) {
                newVal0 = info[2].charAt(22) == '1';
                goRight0 = info[3].charAt(27) == 'r';
                nextState0 = info[4].charAt(26);
                newVal1 = info[6].charAt(22) == '1';
                goRight1 = info[7].charAt(27) == 'r';
                nextState1 = info[8].charAt(26);
            } else {
                newVal1 = info[2].charAt(22) == '1';
                goRight1 = info[3].charAt(27) == 'r';
                nextState1 = info[4].charAt(26);
                newVal0 = info[6].charAt(22) == '1';
                goRight0 = info[7].charAt(27) == 'r';
                nextState0 = info[8].charAt(26);
            }
            states.put(stateChar, new State(newVal0, newVal1, goRight0, goRight1, nextState0, nextState1));
        }
        for (State s : states.values()) {
            s.unsetNextState = states.get(s.unsetNextStateChar);
            s.setNextState = states.get(s.setNextStateChar);
        }
        currentState = states.get(initialStateChar);
    }

    private static void solve() {
        for (int i = 0; i < specifiedStepCount; i++) executeCurrentStateInstructions();
        System.out.println("\nDiagnostic checksum value: "+setPositions.size());
    }

    private static void executeCurrentStateInstructions() {
        if (setPositions.contains(currentPosition)) {
            if (!currentState.setNewValue) setPositions.remove(currentPosition);
            if (currentState.setGoRight) currentPosition++; else currentPosition--;
            currentState = currentState.setNextState;
        } else {
            if (currentState.unsetNewValue) setPositions.add(currentPosition);
            if (currentState.unsetGoRight) currentPosition++; else currentPosition--;
            currentState = currentState.unsetNextState;
        }
    }

    private static class State {
        boolean unsetNewValue, setNewValue;
        boolean unsetGoRight, setGoRight;
        char unsetNextStateChar, setNextStateChar;
        State unsetNextState, setNextState;

        State(boolean nv0, boolean nv1, boolean gr0, boolean gr1, char ns0, char ns1) {
            unsetNewValue = nv0; setNewValue = nv1;
            unsetGoRight = gr0; setGoRight = gr1;
            unsetNextStateChar = ns0; setNextStateChar = ns1;
        }

    }

}
