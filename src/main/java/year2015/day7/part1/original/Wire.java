package year2015.day7.part1.original;

import java.util.regex.Pattern;

class Wire {
    static Pattern containsADigitPattern = Pattern.compile("\\d+");

    String name;
    String[] inputWireStrings;
    Wire[] inputWires;
    String sourceString, operand1String, operand2String;
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
        int inputWireCount = 0;
        boolean operand1IsAWire = false, operand2IsAWire = false;
        if (!containsADigitPattern.matcher(operand1String).matches()) {
            operand1IsAWire = true;
            inputWireCount++;
        }
        if (operand2String!=null && !containsADigitPattern.matcher(operand2String).matches()) {
            operand2IsAWire = true;
            inputWireCount++;
        }
        inputWireStrings = new String[inputWireCount];
        inputWires = new Wire[inputWireCount];
        if (operand1IsAWire) {
            inputWireStrings[0] = operand1String;
            if (operand2IsAWire) inputWireStrings[1] = operand2String;
        } else if (operand2IsAWire) inputWireStrings[0] = operand2String;
    }

    void connectWires(Wire[] wires) {
        if (wires.length!=inputWires.length) return;
        for (int i = 0; i < inputWires.length; i++) {
            inputWires[i] = wires[i];
        }
    }

    boolean attemptActivation() {
        for (int i = 0; i < inputWires.length; i++) {
            if (!inputWires[i].isActive) return false;
        }
        activate();
        return true;
    }

    void activate() {
        switch (instructionType) {
            case 'N' -> signal = BitwiseCharOperator.not(inputWires[0].signal);
            case 'O' -> {
                if (inputWires.length == 2) {
                    signal = BitwiseCharOperator.or(inputWires[0].signal, inputWires[1].signal);
                } else {
                    char signal1, signal2;
                    if (inputWires.length == 1) {
                        if (containsADigitPattern.matcher(operand1String).matches()) {
                            signal1 = (char) Integer.parseInt(operand1String);
                            signal2 = inputWires[0].signal;
                        } else {
                            signal1 = inputWires[0].signal;
                            signal2 = (char) Integer.parseInt(operand1String);
                        }
                        signal = BitwiseCharOperator.or(signal1, signal2);
                    } else {
                        signal = BitwiseCharOperator.or((char)Integer.parseInt(operand1String), (char)Integer.parseInt(operand2String));
                    }
                }
            }
            case 'L' -> signal = BitwiseCharOperator.lshift(inputWires[0].signal, Integer.parseInt(operand2String));
            case 'A' -> {
                if (inputWires.length == 2) {
                    signal = BitwiseCharOperator.and(inputWires[0].signal, inputWires[1].signal);
                } else {
                    char signal1, signal2;
                    if (inputWires.length == 1) {
                        if (containsADigitPattern.matcher(operand1String).matches()) {
                            signal1 = (char) Integer.parseInt(operand1String);
                            signal2 = inputWires[0].signal;
                        } else {
                            signal1 = inputWires[0].signal;
                            signal2 = (char) Integer.parseInt(operand1String);
                        }
                        signal = BitwiseCharOperator.and(signal1, signal2);
                    } else {
                        signal = BitwiseCharOperator.and((char)Integer.parseInt(operand1String), (char)Integer.parseInt(operand2String));
                    }
                }
            }
            case 'R' -> signal = BitwiseCharOperator.rshift(inputWires[0].signal, Integer.parseInt(operand2String));
            case 'D' -> signal = inputWires.length == 0 ? (char) Integer.parseInt(operand1String) : inputWires[0].signal;
            default -> throw new IllegalStateException("Unexpected value: " + instructionType);
        }
        isActive = true;
    }

}
