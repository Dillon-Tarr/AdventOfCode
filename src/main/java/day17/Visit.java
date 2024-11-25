package day17;

import shared.CardinalDirection;

record Visit (CardinalDirection fromDirection, int consecutiveStepsInOneDirection) {

    boolean equals(Visit otherVisit) {
        return this.fromDirection == otherVisit.fromDirection && this.consecutiveStepsInOneDirection == otherVisit.consecutiveStepsInOneDirection;
    }

}
