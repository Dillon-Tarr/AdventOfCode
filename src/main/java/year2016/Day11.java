package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Day11 {
    static private final int DAY = 11;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final HashMap<String, Byte> elementNameToNumberMap = new HashMap<>();
    static private Configuration initialConfiguration;
    static private byte totalItemCount = 0, pairCount;
    static private int reEncounterCount = 0;
    static private ArrayList<byte[]> waysToDifferentlyMapDigits;
    static private final PriorityQueue<Configuration> configurationQueue = new PriorityQueue<>();
    static private final HashSet<String> pastConfigurations = new HashSet<>(); // Must prune equivalent states even of different elements.

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData(args.length != 0); // No args for part 1 mode, any other args for part 2 mode.
        processInputData();
        calculateFewestStepsToGetEverythingToTheFourthFloor();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData(boolean part2Mode) {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString;
            for (int i = 0; i < 3; i++) {
                inputString = br.readLine();
                if (!inputString.startsWith("The first floor contains ") && !inputString.startsWith("The second floor contains ")
                        && !inputString.startsWith("The third floor contains ")) throw new RuntimeException("Bad input string: "+inputString);
                else inputStrings.add(inputString);
            }
            if (part2Mode) inputStrings.add("The first floor also contains an elerium generator, an elerium-compatible microchip, a dilithium generator, and a dilithium-compatible microchip.");

        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processInputData() {
        ArrayList<String> firstThreeFloorsItemStrings = new ArrayList<>();
        for (String s : inputStrings) {
            int i = switch (s.charAt(4)) {
                case 'f' -> 0;
                case 's' -> 1;
                case 't' -> 2;
                default -> throw new RuntimeException("Supplied string must start with \"The \" first, second, or third floor.");
            };
            int precedingSpaceIndex, spaceBeforeGenIndex = s.indexOf(" gen"), hyphenIndex = s.indexOf('-');
            String sub = s, elementString;
            while (spaceBeforeGenIndex != -1) {
                precedingSpaceIndex = sub.substring(0, spaceBeforeGenIndex).lastIndexOf(' ');
                elementString = sub.substring(precedingSpaceIndex+1, spaceBeforeGenIndex);
                elementNameToNumberMap.putIfAbsent(elementString, (byte) elementNameToNumberMap.size());
                firstThreeFloorsItemStrings.add(i+"g"+elementString);
                totalItemCount++;
                sub = sub.substring(spaceBeforeGenIndex+1);
                spaceBeforeGenIndex = sub.indexOf(" gen");
            }
            sub = s;
            while (hyphenIndex != -1) {
                precedingSpaceIndex = sub.substring(0, hyphenIndex).lastIndexOf(' ');
                elementString = sub.substring(precedingSpaceIndex+1, hyphenIndex);
                elementNameToNumberMap.putIfAbsent(elementString, (byte) elementNameToNumberMap.size());
                firstThreeFloorsItemStrings.add(i+"m"+elementString);
                totalItemCount++;
                sub = sub.substring(hyphenIndex+1);
                hyphenIndex = sub.indexOf('-');
            }
        }
        initialConfiguration = new Configuration(firstThreeFloorsItemStrings);
        pairCount = (byte)(totalItemCount/2);
        waysToDifferentlyMapDigits = getWaysToDifferentlyMapDigits(pairCount);
    }

    private static ArrayList<byte[]> getWaysToDifferentlyMapDigits(byte numberOfDigits){
        ArrayList<byte[]> ways = getSubWays(numberOfDigits, new byte[]{});
        ways.remove(0); // Removes mapping the digit at each index to the same index (e.g. 01234)
        System.out.println("Number of ways to differently map "+numberOfDigits+" digits: "+ways.size());
        return ways;
    }

    private static ArrayList<byte[]> getSubWays(byte neededDigitCount, byte[] existingDigits) {
        ArrayList<byte[]> subWays = new ArrayList<>();
        byte existingDigitCount = (byte) existingDigits.length;
        byte[] workingBytes = new byte[existingDigitCount +1];
        for (int i = 0; i < existingDigitCount; i++) workingBytes[i] = existingDigits[i];
        boolean thisIsTheLastDigit = existingDigitCount == neededDigitCount -1;
        boolean digitAlreadyUsed;
        for (byte i = 0; i < neededDigitCount; i++) {
            digitAlreadyUsed = false;
            for (int d = 0; d < existingDigitCount; d++) if (existingDigits[d] == i) { digitAlreadyUsed = true; break; }
            if (!digitAlreadyUsed) {
                workingBytes[existingDigitCount] = i;
                if (!thisIsTheLastDigit) subWays.addAll(getSubWays(neededDigitCount, workingBytes));
                else subWays.add(workingBytes);
            }
        }
        return subWays;
    }

    private static void calculateFewestStepsToGetEverythingToTheFourthFloor() {
        int numberOfConfigurationsTried = -1;
        configurationQueue.add(initialConfiguration);
        Configuration configuration, newConfiguration;
        byte currentFloorNumber, otherFloorNumber, otherFloorSize, numberOfMovingItems;
        ArrayList<Item> currentFloorItems, otherFloorItems, movingItems = new ArrayList<>(), unmovingItems = new ArrayList<>();
        ArrayList<Byte> otherFloorGeneratorElements = new ArrayList<>(), otherFloorMicrochipElements = new ArrayList<>(),
                movingGeneratorElements = new ArrayList<>(), movingMicrochipElements = new ArrayList<>(),
                combinedNewGeneratorElements = new ArrayList<>(), combinedNewMicrochipElements = new ArrayList<>(),
                unmovingGeneratorElements = new ArrayList<>(), unmovingMicrochipElements = new ArrayList<>();
        String stateString = initialConfiguration.getStateString();
        Item movingItem, unmovingItem, otherFloorItem;
        boolean solved = false;
        pastConfigurations.add(stateString);
        pastConfigurations.addAll(getEquivalentStateStrings(stateString));
        do {
            configuration = configurationQueue.remove();
            numberOfConfigurationsTried++;
            if (configuration.floors.get(3).size() == totalItemCount) { printStatusUpdate(configuration, numberOfConfigurationsTried); solved = true; break; }
            else if (numberOfConfigurationsTried % 250 == 0) printStatusUpdate(configuration, numberOfConfigurationsTried);

            currentFloorNumber = configuration.currentFloorNumber;
            currentFloorItems = configuration.floors.get(currentFloorNumber);

            for (byte f = -1; f < 2; f+=2) {
                otherFloorNumber = (byte)(currentFloorNumber+f);
                if (otherFloorNumber < 0 || otherFloorNumber > 3) continue;
                otherFloorItems = configuration.floors.get(otherFloorNumber); otherFloorSize = (byte)otherFloorItems.size();
                otherFloorGeneratorElements.clear(); otherFloorMicrochipElements.clear();
                for (int o = 0; o < otherFloorSize; o++) {
                    otherFloorItem = otherFloorItems.get(o);
                    if (otherFloorItems.get(o).isGenerator) otherFloorGeneratorElements.add(otherFloorItem.elementNumber);
                    else otherFloorMicrochipElements.add(otherFloorItem.elementNumber);
                }
                for (int intAsBinary = 1; intAsBinary < 1<<currentFloorItems.size(); intAsBinary++) {
                    numberOfMovingItems = 0;
                    for (int i = 0; i < currentFloorItems.size(); i++) if ((intAsBinary & 1<<i) != 0) numberOfMovingItems++;
                    if (numberOfMovingItems > 2) continue;
                    movingItems.clear(); movingGeneratorElements.clear(); movingMicrochipElements.clear();
                    for (int i = 0; i < currentFloorItems.size(); i++) if ((intAsBinary & 1<<i) != 0) {
                        movingItem = currentFloorItems.get(i);
                        movingItems.add(movingItem);
                        if (movingItem.isGenerator) movingGeneratorElements.add(movingItem.elementNumber);
                        else movingMicrochipElements.add(movingItem.elementNumber);
                    }
                    combinedNewGeneratorElements.clear(); combinedNewMicrochipElements.clear();
                    combinedNewGeneratorElements.addAll(movingGeneratorElements); combinedNewGeneratorElements.addAll(otherFloorGeneratorElements);
                    combinedNewMicrochipElements.addAll(movingMicrochipElements); combinedNewMicrochipElements.addAll(otherFloorMicrochipElements);
                    if (!combinedNewGeneratorElements.isEmpty() && !combinedNewGeneratorElements.containsAll(combinedNewMicrochipElements)) continue; // Moving and other floor microchips will remain safe.
                    if (!(movingGeneratorElements.size() == 1 && movingMicrochipElements.size() == 1 // If same-element pair is moving, don't need to check current floor.
                            && Objects.equals(movingGeneratorElements.get(0), movingMicrochipElements.get(0)))) { // Current floor will remain safe.
                        unmovingItems.clear(); unmovingItems.addAll(currentFloorItems); unmovingItems.removeAll(movingItems);
                        unmovingGeneratorElements.clear(); unmovingMicrochipElements.clear();
                        for (int u = 0; u < unmovingItems.size(); u++) {
                            unmovingItem = unmovingItems.get(u);
                            if (unmovingItem.isGenerator) unmovingGeneratorElements.add(unmovingItem.elementNumber);
                            else unmovingMicrochipElements.add(unmovingItem.elementNumber);
                        }
                        if (!unmovingGeneratorElements.isEmpty() && !unmovingGeneratorElements.containsAll(unmovingMicrochipElements))
                            continue;
                    }

                    newConfiguration = configuration.getConfigurationCopy();
                    newConfiguration.floors.get(currentFloorNumber).removeAll(movingItems);
                    newConfiguration.moves.add(new Move(currentFloorNumber, otherFloorNumber, movingItems.get(0), movingItems.size()==2 ? movingItems.get(1) : null));
                    newConfiguration.currentFloorNumber = otherFloorNumber;
                    newConfiguration.floors.get(otherFloorNumber).addAll(movingItems);
                    newConfiguration.stepCount++;

                    stateString = newConfiguration.getStateString();
                    if (pastConfigurations.add(stateString)) {
                        pastConfigurations.addAll(getEquivalentStateStrings(stateString));
                        configurationQueue.add(newConfiguration);
                    } else reEncounterCount++;
                }
            }
        } while (!configurationQueue.isEmpty());
        if (solved) System.out.println("Fewest possible steps to move all items to floor 4: "+configuration.stepCount);
        else System.out.println("-=-=-=-=-\n!WARNING!\n-=-=-=-=-\nQueue was completed without reaching a configuration with all items on floor 4.-=-=-=-=-\n!WARNING!\n-=-=-=-=-\n");
    }

    private static void printStatusUpdate(Configuration configuration, int numberOfConfigurationsTried) {
        System.out.println("\nConfiguration after trying "+ numberOfConfigurationsTried +" configurations: "+configuration
                +"\nQueue size: "+configurationQueue.size()
                +"\nNumber of steps taken in current configuration: "+configuration.stepCount
                +"\nPast configuration count: "+pastConfigurations.size()
                +"\nRe-encounter count: "+reEncounterCount
                +"\nMoves taken: "+configuration.moves);
    }

    private static HashSet<String> getEquivalentStateStrings(String stateString) {
        ArrayList<ArrayList<Integer>> indicesPerValue = new ArrayList<>();
        for (int d = 0; d < pairCount; d++) {
            ArrayList<Integer> indices = new ArrayList<>();
            char dChar = Character.forDigit(d, 10);
            for (int i = 0; i < stateString.length(); i++) if (stateString.charAt(i) == dChar) indices.add(i);
            indicesPerValue.add(indices);
        }
        HashSet<String> stateStrings = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        for (byte[] way : waysToDifferentlyMapDigits) {
            sb.setLength(0);
            sb.append(stateString);
            for (int d = 0; d < pairCount; d++) {
                String digitString = ""+d;
                ArrayList<Integer> indices = indicesPerValue.get(way[d]);
                for (int index : indices) sb.replace(index, index+1, digitString);
            }
            stateStrings.add(sb.toString());
        }
        return stateStrings;
    }


    private static class Configuration implements Comparable<Configuration> {
        byte currentFloorNumber;
        int stepCount;
        ArrayList<ArrayList<Item>> floors = new ArrayList<>();
        ArrayList<Move> moves;

        Configuration(ArrayList<String> strings) { // Representing floors 1-4 as floors 0-3.
            currentFloorNumber = 0; stepCount = 0;
            ArrayList<Item> floor;
            for (int i = 0; i < 4; i++) floors.add(new ArrayList<>());
            for (String s : strings) {
                floor = floors.get(Integer.parseInt(s.substring(0, 1)));
                floor.add(new Item(s.charAt(1) == 'g', elementNameToNumberMap.get(s.substring(2))));
            }
            moves = new ArrayList<>();
        }

        private Configuration (Configuration configurationToCopy) {
            currentFloorNumber = configurationToCopy.currentFloorNumber;
            stepCount = configurationToCopy.stepCount;
            for (int i = 0; i < 4; i++) floors.add(new ArrayList<>(configurationToCopy.floors.get(i)));
            moves = new ArrayList<>(configurationToCopy.moves);
        }

        private Configuration getConfigurationCopy () {return new Configuration(this);}

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = floors.size()-1; i >= 0; i--) sb.append(System.lineSeparator()).append("Floor ").append(i+1).append(": ").append(floors.get(i));
            return sb.toString();
        }

        private String getStateString() {
            StringBuilder sb = new StringBuilder(switch (currentFloorNumber) {
                case 0 -> "o";
                case 1 -> "t";
                case 2 -> "h";
                case 3 -> "f";
                default -> throw new RuntimeException("Invalid floor number: "+currentFloorNumber);
            });
            ArrayList<Item> floor;
            for (int f = 0; f < floors.size(); f++) {
                floor = floors.get(f);
                floor.sort(Item::compareTo);
                for (int i = 0; i < floor.size(); i++) sb.append(floor.get(i));
                sb.append('.');
            }
            return sb.toString();
        }

        @Override
        public int compareTo(Configuration other) {return Integer.compare(stepCount, other.stepCount);}

    }

    private static class Item implements Comparable<Item> {
        boolean isGenerator; // otherwise is a microchip
        byte elementNumber;
        String stringRepresentation;

        Item(boolean isGenerator, byte elementNumber) {
            this.isGenerator = isGenerator;
            this.elementNumber = elementNumber;
            stringRepresentation = elementNumber+(isGenerator?"G":"M");
        }

        @Override
        public String toString() {
            return stringRepresentation;
        }

        @Override
        public int compareTo(Item otherItem) {
            if (this == otherItem) return 0;
            if (elementNumber == otherItem.elementNumber) return isGenerator ? -1 : 1;
            return Integer.compare(elementNumber, otherItem.elementNumber);
        }
    }

    private record Move (byte sourceFloor, byte destinationFloor, Item item1, Item item2) {

        @Override
        public String toString() {
            char directionChar = destinationFloor > sourceFloor ? '^' : 'v';
            StringBuilder sb = new StringBuilder();
            sb.append(sourceFloor+1).append(directionChar).append(destinationFloor+1).append(":{").append(item1);
            if (item2 != null) sb.append("&").append(item2);
            sb.append("}");
            return sb.toString();
        }
    }

}
