package year2023.day20.part1;

abstract class Module {
    static long buttonPressCount = 0;
    static long highPulsesSent = 0;
    static long lowPulsesSent = 0;

    static void pressButtonModuleButton() {buttonPressCount++; lowPulsesSent++;}
    static long getProductOfHighAndLowPulseCounts() {return lowPulsesSent*highPulsesSent;}

    String name;
    String[] destinationModuleNames;

    Module (String name, String[] destinationModuleNames) {
        this.name = name;
        this.destinationModuleNames = destinationModuleNames;
    }

}
