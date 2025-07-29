package year2023.day18.part1;

import shared.CardinalDirection;

class Instruction {
    final CardinalDirection direction;
    final int steps;

    Instruction(String instructionString) {
        String[] instructionParts = instructionString.split(" ");

        this.direction = switch (instructionParts[0].charAt(0)) {
            case 'U' -> CardinalDirection.NORTH;
            case 'D' -> CardinalDirection.SOUTH;
            case 'L' -> CardinalDirection.WEST;
            case 'R' -> CardinalDirection.EAST;
            default -> throw new IllegalStateException("Unexpected value: " + instructionParts[0]);
        };
        this.steps = Integer.parseInt(instructionParts[1]);
    }

}
