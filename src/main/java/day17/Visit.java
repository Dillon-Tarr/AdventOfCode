package day17;

record Visit (CardinalDirection fromDirection, int consecutiveStepsInOneDirection) {

    boolean equals(Visit otherVisit) {
        return this.fromDirection == otherVisit.fromDirection && this.consecutiveStepsInOneDirection == otherVisit.consecutiveStepsInOneDirection;
    }

}
