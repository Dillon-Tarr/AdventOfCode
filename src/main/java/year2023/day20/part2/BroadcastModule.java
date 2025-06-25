package year2023.day20.part2;

class BroadcastModule extends Module {
    Pulse[] broadcastPulses;

    BroadcastModule(String name, String[] destinationModuleNames) {
        super(name, destinationModuleNames);
        broadcastPulses = new Pulse[destinationModuleNames.length];
        typeOfLastPulseSent = PulseType.LOW;
        for (int i = 0; i < destinationModuleNames.length; i++) broadcastPulses[i] = new Pulse(this.name, PulseType.LOW, destinationModuleNames[i]);
    }

    Pulse[] sendPulse() {
        recordPulses(PulseType.LOW, broadcastPulses.length);
        return broadcastPulses;
    }

}
