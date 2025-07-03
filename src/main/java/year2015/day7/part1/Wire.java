package year2015.day7.part1;

public class Wire {
    String sourceString, operand1, operand2;
    char instructionType;

    boolean active = false;
    char signal; // used because it holds 16 unsigned bits

    Wire (String s) {
        sourceString = s.substring(0, s.indexOf('-')-1);
        if (sourceString.charAt(0) == 'N') instructionType = 'N';
        else if (sourceString.contains("OR")) instructionType = 'O';
        else if (sourceString.contains("LSHIFT")) instructionType = 'L';
        else if (sourceString.contains("AND")) instructionType = 'A';
        else if (sourceString.contains("RSHIFT")) instructionType = 'R';
        else instructionType = 'D'; // Direct

        switch (instructionType) {
            case 'D' -> {
                operand1 = sourceString;
                operand2 = null;
            }
            case 'N' -> {
                operand1 = sourceString.substring(sourceString.indexOf(" "));
                operand2 = null;
            }
            case 'O' -> {
                int spaceIndex = sourceString.indexOf(" ");
                operand1 = sourceString.substring(0, spaceIndex);
                operand2 = sourceString.substring(spaceIndex+4);
            }
            case 'A' -> {
                int spaceIndex = sourceString.indexOf(" ");
                operand1 = sourceString.substring(0, spaceIndex);
                operand2 = sourceString.substring(spaceIndex+5);
            }
            case 'L', 'R' -> {
                int spaceIndex = sourceString.indexOf(" ");
                operand1 = sourceString.substring(0, spaceIndex);
                operand2 = sourceString.substring(spaceIndex+8);
            }
        }
    }

}
