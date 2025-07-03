package year2015.day7.part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static private final int DAY = 7;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final ArrayList<String> instructionStrings = new ArrayList<>();
    static private final HashMap<String, Wire> wires = new HashMap<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        createWires();
        connectWires();


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
        for (int i = 0; i < instructionStrings.size(); i++) {
            s = instructionStrings.get(i);
            wires.put(s.substring(s.lastIndexOf(" ")+1), new Wire(s));
        }
    }
    //TODO: connect wires
    private static void connectWires() {
        for (int i = 0; i < wires.size(); i++) {
            // Connect wires
        }
    }

}
