package year2023.day20.part2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static shared.math.LCMFinders.findLCMWithPrimes;

public class Main {
    static private final int DAY = 20;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private BroadcastModule broadcastModule;
    static private final HashMap<String, Module> modules = new HashMap<>();
    static private final ArrayList<String> rxInputModuleNames = new ArrayList<>();
    static private final LinkedList<Pulse> pulses = new LinkedList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        initializeInputModules();
        findMultipleInputGeneration();
        sendPulses();
        getLCMOfConditionPeriods();

        System.out.println("\nExecution time in seconds: " + ((double) (System.nanoTime() - startTime) / 1000000000));
    }

    private static void processInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String cl = br.readLine();
            while (cl != null) {
                int spaceIndex = cl.indexOf(" ");
                String moduleName = cl.substring(1, spaceIndex);
                String[] destinationModuleNames = cl.substring(spaceIndex + 4).split(", ");
                if (cl.charAt(0) == 'b') broadcastModule = new BroadcastModule(moduleName, destinationModuleNames);
                else if (cl.charAt(0) == '%') modules.put(moduleName, new FlipFlopModule(moduleName, destinationModuleNames));
                else modules.put(moduleName, new ConjunctionModule(moduleName, destinationModuleNames));
                cl = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void initializeInputModules() {
        Collection<Module> actualModules = modules.values();
        for (Module inputModule : actualModules)
            for (String destinationModuleName : inputModule.destinationModuleNames)
                if (destinationModuleName.equals("rx")) rxInputModuleNames.add(inputModule.name);
                else modules.get(destinationModuleName).addInputModule(inputModule.name);
    }

    private static void findMultipleInputGeneration() {
        ArrayList<String> modulesToCheck = new ArrayList<>(rxInputModuleNames);
        int generation = 1;
        while(modulesToCheck.size() == 1) {
            Module module = modules.get(modulesToCheck.remove(0));
            module.generationsAboveRx = generation++;
            modulesToCheck.addAll(module.inputModulesWithTypeOfLastReceivedPulse.keySet());
        }
        for (String moduleName : modulesToCheck) modules.get(moduleName).generationsAboveRx = generation;
        setConditions(modulesToCheck);
    }

    private static void setConditions(ArrayList<String> moduleNames) {
        for (String moduleName : moduleNames) {
            Module module = modules.get(moduleName);
            module.addCondition(new Condition(module.name, PulseType.HIGH));
        }
    }

    private static void sendPulses() {
        while (Condition.checkConditionsAndReturnWhetherIncompleteConditionsRemain()) {
            Module.pressButtonModuleButton();
            pulses.addAll(Arrays.asList(broadcastModule.sendPulse()));
            while (!pulses.isEmpty()) {
                Pulse pulse = pulses.poll();
                String destinationModuleName = pulse.destinationModule();
                Module destinationModule = modules.get(destinationModuleName);
                Collection<Pulse> newPulses;
                if (destinationModule instanceof FlipFlopModule flipFlopModule) newPulses = Arrays.asList(flipFlopModule.receivePulseAndSendPulses(pulse.type()));
                else if (destinationModule instanceof ConjunctionModule conjunctionModule) newPulses = Arrays.asList(conjunctionModule.receivePulseAndSendPulses(pulse.inputModule(), pulse.type()));
                else continue;
                pulses.addAll(newPulses);
            }
        }
    }

    private static void getLCMOfConditionPeriods() {
        long LCM = findLCMWithPrimes(Condition.getFirstOccurrenceOfEachCondition());
        System.out.println(LCM);
    }

}