package year2023.day20.part1;

import java.util.HashMap;

class ConjunctionModule extends Module {
    final HashMap<String, PulseType> inputModulesWithTypeOfLastReceivedPulse = new HashMap<>();
    Pulse[] highPulses;
    Pulse[] lowPulses;

    ConjunctionModule (String name, String[] destinationModuleNames) {
        super(name, destinationModuleNames);
        highPulses = new Pulse[destinationModuleNames.length];
        lowPulses = new Pulse[destinationModuleNames.length];
        for (int i = 0; i < destinationModuleNames.length; i++) {
            highPulses[i] = new Pulse(this.name, PulseType.HIGH, destinationModuleNames[i]);
            lowPulses[i] = new Pulse(this.name, PulseType.LOW, destinationModuleNames[i]);
        }
    }

    Pulse[] receivePulseAndSendPulses(String inputModuleName, PulseType pulseType) {
        inputModulesWithTypeOfLastReceivedPulse.put(inputModuleName, pulseType);
        PulseType typeToSend = inputModulesWithTypeOfLastReceivedPulse.containsValue(PulseType.LOW) ? PulseType.HIGH : PulseType.LOW;
        if (typeToSend == PulseType.HIGH) {
            highPulsesSent += highPulses.length;
            return highPulses;
        } else {
            lowPulsesSent += lowPulses.length;
            return lowPulses;
        }
    }

    void addInputModule(String inputModuleName) {
        inputModulesWithTypeOfLastReceivedPulse.put(inputModuleName, PulseType.LOW);
    }

}
