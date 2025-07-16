package year2015.day19;

record QueuedReplacement (String startingString, String replacementString, int replacementStartIndex,
                          int replacementEndIndex, int completedReplacements) implements Comparable<QueuedReplacement> {

    @Override
    public int compareTo(QueuedReplacement otherQueuedReplacement) {
        return Integer.compare(otherQueuedReplacement.completedReplacements, completedReplacements);
    }

}
