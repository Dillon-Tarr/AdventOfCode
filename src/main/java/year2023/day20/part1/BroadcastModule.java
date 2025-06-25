package year2023.day20.part1;

class BroadcastModule extends Module {
    Pulse[] broadcastPulses;

    BroadcastModule(String name, String[] destinationModuleNames) {
        super(name, destinationModuleNames);
        broadcastPulses = new Pulse[destinationModuleNames.length];
        for (int i = 0; i < destinationModuleNames.length; i++) broadcastPulses[i] = new Pulse(this.name, PulseType.LOW, destinationModuleNames[i]);
    }

    Pulse[] sendPulse() {
        lowPulsesSent += broadcastPulses.length;
        return broadcastPulses;
    }

}
