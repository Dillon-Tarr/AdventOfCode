package day17.puzzle1.PriorityQueueMethod;

record QueuedStep(int y, int x, int heatLost, int consecutiveStepsInOneDirection, char lastStepDirection) implements Comparable<QueuedStep> {

    @Override
    public int compareTo(QueuedStep otherStep) {
        if (heatLost < otherStep.heatLost) return -1;
        else if (heatLost > otherStep.heatLost) return 1;
        else return Integer.compare(y+x, otherStep.y+otherStep.x);
    }
}
