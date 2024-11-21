package day17.puzzle1;

import day17.CardinalDirection;

record Visit (CardinalDirection fromDirection, int consecutiveStepsInOneDirection) {

    boolean equals(Visit otherVisit) {
        return this.fromDirection == otherVisit.fromDirection && this.consecutiveStepsInOneDirection == otherVisit.consecutiveStepsInOneDirection;
    }

}
