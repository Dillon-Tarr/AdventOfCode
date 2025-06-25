package year2023.day20.part2;

class FlipFlopModule extends Module {
    boolean isOn = false;
    Pulse[] highPulses;
    Pulse[] lowPulses;
    Pulse[] noPulses = new Pulse[0];

    FlipFlopModule (String name, String[] destinationModuleNames) {
        super(name, destinationModuleNames);
        highPulses = new Pulse[destinationModuleNames.length];
        lowPulses = new Pulse[destinationModuleNames.length];
        for (int i = 0; i < destinationModuleNames.length; i++) {
            highPulses[i] = new Pulse(this.name, PulseType.HIGH, destinationModuleNames[i]);
            lowPulses[i] = new Pulse(this.name, PulseType.LOW, destinationModuleNames[i]);
        }
    }

    Pulse[] receivePulseAndSendPulses(PulseType pulseType) {
        if (pulseType == PulseType.LOW) {
            isOn = !isOn;
            PulseType typeToSend = isOn ? PulseType.HIGH : PulseType.LOW;
            if (typeToSend == PulseType.HIGH) {
                recordPulses(PulseType.HIGH, highPulses.length);
                return highPulses;
            } else {
                recordPulses(PulseType.LOW, lowPulses.length);
                totalLowPulsesSent += lowPulses.length;
                return lowPulses;
            }
        } else return noPulses;
    }

}
