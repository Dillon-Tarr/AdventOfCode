package year2015.day7.part1.cleaner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Main {
    static private final int DAY = 7;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final ArrayList<String> instructionStrings = new ArrayList<>();
    static private final HashMap<String, Wire> inactiveWires = new HashMap<>();
    static private final HashMap<String, Wire> activeWires = new HashMap<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        createWires();
        connectWires();
        activateWires();

        System.out.println("Signal from 'a' wire: "+(int)activeWires.get("a").signal);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                instructionStrings.add(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void createWires() {
        String s;
        String wireName;
        for (int i = 0; i < instructionStrings.size(); i++) {
            s = instructionStrings.get(i);
            wireName = s.substring(s.lastIndexOf(" ")+1);
            inactiveWires.put(wireName, new Wire(s, wireName));
        }
    }

    private static void connectWires() {
        inactiveWires.forEach((name, wire) -> {
            Wire[] inputWires = new Wire[wire.inputWireStrings.size()];
            for (int i = 0; i < inputWires.length; i++) {
                inputWires[i] = inactiveWires.get(wire.inputWireStrings.get(i));
            }
            wire.connectWires(inputWires);
        });
    }

    private static void activateWires() {
        ArrayList<Wire> transitioningWires = new ArrayList<>();
        Wire tWire;
        while (!inactiveWires.isEmpty()) {
            inactiveWires.forEach((name, wire) -> {
                if (wire.attemptActivation()) {
                    transitioningWires.add(wire);
                }
            });
            while (!transitioningWires.isEmpty()) {
                tWire = transitioningWires.remove(0);
                inactiveWires.remove(tWire.name);
                activeWires.put(tWire.name, tWire);
            }
        }
    }

}
