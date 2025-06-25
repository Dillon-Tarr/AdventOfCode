package year2023.day20.part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    static private final int DAY = 20;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private BroadcastModule broadcastModule;
    static private final HashMap<String, Module> modules = new HashMap<>();
    static private final LinkedList<Pulse> pulses = new LinkedList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        initializeInputModulesInConjunctionModules();
        sendPulses();

        System.out.println("\nExecution time in seconds: " + ((double) (System.nanoTime() - startTime) / 1000000000));
    }

    private static void processInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String cl = br.readLine();
            while (cl != null) {
                int spaceIndex = cl.indexOf(" ");
                String moduleName = cl.substring(1, spaceIndex);
                String[] destinationModuleNames = cl.substring(spaceIndex+4).split(", ");
                if (cl.charAt(0) == 'b') broadcastModule = new BroadcastModule(moduleName, destinationModuleNames);
                else if (cl.charAt(0) == '%') modules.put(moduleName, new FlipFlopModule(moduleName, destinationModuleNames));
                else modules.put(moduleName, new ConjunctionModule(moduleName, destinationModuleNames));
                cl = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void initializeInputModulesInConjunctionModules() {
        Collection<Module> actualModules = modules.values();
        for (Module inputModule : actualModules) for (String destinationModuleName : inputModule.destinationModuleNames)
            if (modules.get(destinationModuleName) instanceof ConjunctionModule conjunctionModule) conjunctionModule.addInputModule(inputModule.name);
    }

    private static void sendPulses() {
        while (Module.buttonPressCount < 1000) {
            Module.pressButtonModuleButton();
            pulses.addAll(Arrays.asList(broadcastModule.sendPulse()));
            while (!pulses.isEmpty()) {
                Pulse pulse = pulses.poll();
                Module destinationModule = modules.get(pulse.destinationModule());
                if (destinationModule instanceof FlipFlopModule flipFlopModule) pulses.addAll(Arrays.asList(flipFlopModule.receivePulseAndSendPulses(pulse.type())));
                else if (destinationModule instanceof ConjunctionModule conjunctionModule) pulses.addAll(Arrays.asList(conjunctionModule.receivePulseAndSendPulses(pulse.inputModule(), pulse.type())));
            }
        }
        System.out.println("Product of high and low pulse counts: "+ Module.getProductOfHighAndLowPulseCounts());
    }

}