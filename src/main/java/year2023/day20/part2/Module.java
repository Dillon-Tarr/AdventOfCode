package year2023.day20.part2;

import java.util.HashMap;

abstract class Module {
    static long buttonPressCount = 0;
    static long totalHighPulsesSent = 0;
    static long totalLowPulsesSent = 0;

    static void pressButtonModuleButton() {buttonPressCount++; totalLowPulsesSent++;}

    String name;
    String[] destinationModuleNames;
    int generationsAboveRx = -1;
    PulseType typeOfLastPulseSent = PulseType.NONE;
    long numberOfPulsesSent = 0;
    final HashMap<String, PulseType> inputModulesWithTypeOfLastReceivedPulse = new HashMap<>();

    Condition condition = null;

    Module (String name, String[] destinationModuleNames) {
        this.name = name;
        this.destinationModuleNames = destinationModuleNames;
    }

    void recordPulses(PulseType pulseType, int count) {
        typeOfLastPulseSent = pulseType;
        numberOfPulsesSent+=count;
        switch(pulseType) {
            case HIGH -> totalHighPulsesSent += count;
            case LOW -> totalLowPulsesSent += count;
            default -> throw new RuntimeException();
        }
        checkCondition();
    }

    void addInputModule(String inputModuleName) {
        inputModulesWithTypeOfLastReceivedPulse.put(inputModuleName, PulseType.LOW);
    }

    void addCondition(Condition condition) {
        this.condition = condition;
    }

    void checkCondition() {
        if (condition != null && typeOfLastPulseSent == condition.requiredPulseType) condition.logCompletion(buttonPressCount);
    }

}
