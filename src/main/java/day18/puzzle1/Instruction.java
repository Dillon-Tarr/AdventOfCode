package day18.puzzle1;

import shared.CardinalDirection;

import java.awt.*;

public class Instruction {
    final CardinalDirection direction;
    final int steps;
    final Color color;

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
        this.color = Color.decode(instructionParts[2].substring(1, 8));
    }

}
