package day18.part2;

import shared.CardinalDirection;

public class Instruction {
    final CardinalDirection direction;
    final long steps;

    Instruction(String instructionString) {
        String[] instructionParts = instructionString.split(" ");

        this.direction = switch (instructionParts[2].charAt(7)) {
            case '0' -> CardinalDirection.EAST;
            case '1' -> CardinalDirection.SOUTH;
            case '2' -> CardinalDirection.WEST;
            case '3' -> CardinalDirection.NORTH;
            default -> throw new IllegalStateException("Unexpected value: " + instructionParts[2].charAt(7));
        };
        this.steps = Long.parseLong(instructionParts[2].substring(2, 7), 16);
    }

}
