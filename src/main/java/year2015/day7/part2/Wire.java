package year2015.day7.part2;

import shared.BitwiseCharOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class Wire {
    static Pattern containsADigitPattern = Pattern.compile("\\d+");

    String name;
    ArrayList<String> inputWireStrings = new ArrayList<>(2);
    ArrayList<Wire> inputWires = new ArrayList<>(2);
    String sourceString, operand1String, operand2String;
    boolean operand1IsAWire = false, operand2IsAWire = false;
    char instructionType;

    boolean isActive = false;
    char signal; // used because it holds 16 unsigned bits

    Wire (String s, String wireName) {
        name = wireName;
        sourceString = s.substring(0, s.indexOf('-')-1);
        if (sourceString.charAt(0) == 'N') instructionType = 'N';
        else if (sourceString.contains("OR")) instructionType = 'O';
        else if (sourceString.contains("LSHIFT")) instructionType = 'L';
        else if (sourceString.contains("AND")) instructionType = 'A';
        else if (sourceString.contains("RSHIFT")) instructionType = 'R';
        else instructionType = 'D'; // Direct

        switch (instructionType) {
            case 'D' -> {
                operand1String = sourceString;
                operand2String = null;
            }
            case 'N' -> {
                operand1String = sourceString.substring(sourceString.indexOf(" ")+1);
                operand2String = null;
            }
            case 'O' -> {
                int spaceIndex = sourceString.indexOf(" ");
                operand1String = sourceString.substring(0, spaceIndex);
                operand2String = sourceString.substring(spaceIndex+4);
            }
            case 'A' -> {
                int spaceIndex = sourceString.indexOf(" ");
                operand1String = sourceString.substring(0, spaceIndex);
                operand2String = sourceString.substring(spaceIndex+5);
            }
            case 'L', 'R' -> {
                int spaceIndex = sourceString.indexOf(" ");
                operand1String = sourceString.substring(0, spaceIndex);
                operand2String = sourceString.substring(spaceIndex+8);
            }
        }
        if (!containsADigitPattern.matcher(operand1String).matches()) {
            operand1IsAWire = true;
            inputWireStrings.add(operand1String);
        }
        if (operand2String!=null && !containsADigitPattern.matcher(operand2String).matches()) {
            operand2IsAWire = true;
            inputWireStrings.add(operand2String);
        }
    }

    void connectWires(Wire[] wires) {
        if (wires.length!=inputWireStrings.size()) return;
        inputWires.addAll(List.of(wires));
    }

    boolean attemptActivation() {
        for (int i = 0; i < inputWires.size(); i++) {
            if (!inputWires.get(i).isActive) return false;
        }
        activate();
        return true;
    }

    void activate() {

        switch (instructionType) {
            case 'N' -> signal = BitwiseCharOperator.not(inputWires.get(0).signal);
            case 'L' -> signal = BitwiseCharOperator.lshift(inputWires.get(0).signal, Integer.parseInt(operand2String));
            case 'R' -> signal = BitwiseCharOperator.rshift(inputWires.get(0).signal, Integer.parseInt(operand2String));
            case 'D' -> signal = inputWires.isEmpty() ? (char) Integer.parseInt(operand1String) : inputWires.get(0).signal;
            case 'A', 'O' -> {
                char signal1, signal2;
                if (inputWires.size() == 2) {
                    signal1 = inputWires.get(0).signal;
                    signal2 = inputWires.get(1).signal;
                } else if (inputWires.size() == 1) {
                    if (containsADigitPattern.matcher(operand1String).matches()) {
                        signal1 = (char) Integer.parseInt(operand1String);
                        signal2 = inputWires.get(0).signal;
                    } else {
                        signal1 = inputWires.get(0).signal;
                        signal2 = (char) Integer.parseInt(operand1String);
                    }
                } else {
                    signal1 = (char)Integer.parseInt(operand1String);
                    signal2 = (char)Integer.parseInt(operand2String);
                }
                    signal = instructionType == 'A' ?
                            BitwiseCharOperator.and(signal1, signal2) :
                            BitwiseCharOperator.or(signal1, signal2);
            }
            default -> throw new IllegalStateException("Unexpected value: " + instructionType);
        }
        isActive = true;
    }

    void deactivate() {
        isActive = false;
    }

}
