package day17.puzzle1.PriorityQueueMethod;

class HeatLossIgnoringStep {
    private final int y;
    private final int x;
    private final char lastStepDirection;
    private final int consecutiveStepsInOneDirection;

    HeatLossIgnoringStep(QueuedStep step) {
        this.y = step.y();
        this.x = step.x();
        this.lastStepDirection = step.lastStepDirection();
        this.consecutiveStepsInOneDirection = step.consecutiveStepsInOneDirection();
    }

    boolean equals(HeatLossIgnoringStep otherStep) {
        return (y == otherStep.y && x == otherStep.x && lastStepDirection == otherStep.lastStepDirection && consecutiveStepsInOneDirection == otherStep.consecutiveStepsInOneDirection);
    }

}
